package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A builder for {@link DataCollection}s
 */
public class DataCollectionBuilder
{
    private MergeType xMergeType = MergeType.SUM;
    private MergeType yMergeType = MergeType.SUM;
    private String title = null;
    private DataSource xData;
    private DataSource yData;
    private Resolution resolution;
    private Map<String, List<MatchedDataPair>> resultData;
    private Map<String, MatchedDataPair> finalResult;

    /**
     * Creation of a builder that builds a {@link DataCollection}
     * 
     * @param xData
     *            The x values
     * @param yData
     *            The y values
     * @param resolution
     *            The resolution
     */
    public DataCollectionBuilder(DataSource xData, DataSource yData, Resolution resolution)
    {
	this.xData = xData;
	this.yData = yData;
	this.resolution = resolution;
    }

    /**
     * Sets the title
     * 
     * @param title
     *            The new title
     * @return this builder (for chaining)
     */
    public DataCollectionBuilder setTitle(String title)
    {
	this.title = title;
	return this;
    }

    /**
     * Gets the title
     * 
     * @return The title
     */
    public String getTitle()
    {
	return title == null ? (xData.getName() + " : " + yData.getName()) : title;
    }

    /**
     * Sets how to merge the x values
     * 
     * @param xMergeType
     *            The merge type
     * @return this builder (for chaining)
     */
    public DataCollectionBuilder setXMergeType(MergeType xMergeType)
    {
	this.xMergeType = xMergeType;
	return this;
    }

    /**
     * Sets how to merge the y values
     * 
     * @param yMergeType
     *            The merge type
     * @return this builder (for chaining)
     */
    public DataCollectionBuilder setYMergeType(MergeType yMergeType)
    {
	this.yMergeType = yMergeType;
	return this;
    }

    static String localDateToString(LocalDate date, Resolution resolution)
    {
	switch (resolution)
	{
	case DAY:
	    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	case WEEK:
	    return date.format(DateTimeFormatter.ofPattern("YYYY-'W'w"));
	case MONTH:
	    return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
	case QUARTER:
	    return date.format(DateTimeFormatter.ofPattern("yyyy-'Q'Q"));
	case YEAR:
	    return date.format(DateTimeFormatter.ofPattern("yyyy"));
	default:
	    throw new RuntimeException("Unknown resolution!");
	}
    }

    private void put(String key, MatchedDataPair pair)
    {
	if (!resultData.containsKey(key)) resultData.put(key, new LinkedList<>());

	resultData.get(key).add(pair);
    }

    static Double mergeData(List<MatchedDataPair> data, MergeType mergeType, boolean xValue)
    {
	Double value = 0d;
	if (mergeType == MergeType.MEDIAN)
	{
	    MatchedDataPair median = data.get((int) Math.floor(data.size() / 2));
	    value = xValue ? median.getXValue() : median.getYValue();
	}
	else
	{
	    for (MatchedDataPair pair : data)
	    {
		value += xValue ? pair.getXValue() : pair.getYValue();
	    }

	    if (mergeType == MergeType.AVERAGE)
	    {
		value /= data.size();
	    }
	}

	return value;
    }

    static MatchedDataPair mergeData(List<MatchedDataPair> data, MergeType xMergeType, MergeType yMergeType)
    {
	return new MatchedDataPair(mergeData(data, xMergeType, true), mergeData(data, yMergeType, false));
    }

    /**
     * Build the {@link DataCollection}
     * 
     * @return the resulting {@link DataCollection}
     */
    public DataCollection getResult()
    {
	resultData = new HashMap<String, List<MatchedDataPair>>();
	
	Set<Entry<LocalDate, Double>> yDataPairs = yData.getData().entrySet();

	for (Entry<LocalDate, Double> xData : xData.getData().entrySet())
	{
	    for (Entry<LocalDate, Double> yData : yDataPairs.stream().collect(Collectors.toList()))
	    {
		if (localDateToString(xData.getKey(), resolution).equals(localDateToString(yData.getKey(), resolution)))
		{
		    put(localDateToString(xData.getKey(), resolution), new MatchedDataPair(xData.getValue(), yData.getValue()));
		    yDataPairs.remove(yData);
		}
	    }
	}

	finalResult = new HashMap<String, MatchedDataPair>();
	
	for (Entry<String, List<MatchedDataPair>> node : resultData.entrySet())
	{
	    finalResult.put(node.getKey(), mergeData(node.getValue(), xMergeType, yMergeType));
	}

	return new DataCollection(getTitle(), xData.getUnit(), yData.getUnit(), finalResult);
    }
}
