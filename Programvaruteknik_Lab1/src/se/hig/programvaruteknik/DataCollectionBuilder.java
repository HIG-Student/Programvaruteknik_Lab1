package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private Map<String, List<MatchedDataPair>> resultData = new HashMap<String, List<MatchedDataPair>>();
    private Map<String, MatchedDataPair> finalResult = new HashMap<String, MatchedDataPair>();

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

    private class DataPair
    {
	public LocalDate key;
	public Double value;

	public DataPair(LocalDate key, Double value)
	{
	    this.key = key;
	    this.value = value;
	}
    }

    /**
     * Build the {@link DataCollection}
     * 
     * @return the resulting {@link DataCollection}
     */
    public DataCollection getResult()
    {
	List<DataPair> ys = yData
		.getData()
		.entrySet()
		.stream()
		.map((d) -> new DataPair(d.getKey(), d.getValue()))
		.collect(Collectors.toList());

	for (Entry<LocalDate, Double> x : xData.getData().entrySet())
	{
	    for (DataPair y : ys.toArray(new DataPair[0]))
	    {
		if (localDateToString(x.getKey(), resolution).equals(localDateToString(y.key, resolution)))
		{
		    put(localDateToString(x.getKey(), resolution), new MatchedDataPair(x.getValue(), y.value));
		    ys.remove(y);

		    // System.out.println(localDateToString(x.getKey(),
		    // resolution) + ": " + new MatchedDataPair(x.getValue(),
		    // y.value));
		}
	    }
	}

	for (Entry<String, List<MatchedDataPair>> node : resultData.entrySet())
	{

	    Double xSum = 0d;
	    for (MatchedDataPair pair : node.getValue())
	    {
		xSum += pair.getXValue();
	    }

	    switch (xMergeType)
	    {
	    case AVERAGE:
		xSum /= node.getValue().size();
		break;
	    case SUM:
		break;
	    }

	    Double ySum = 0d;
	    for (MatchedDataPair pair : node.getValue())
	    {
		ySum += pair.getYValue();
	    }

	    switch (yMergeType)
	    {
	    case AVERAGE:
		ySum /= node.getValue().size();
		break;
	    case SUM:
		break;
	    }

	    finalResult.put(node.getKey(), new MatchedDataPair(xSum, ySum));
	}

	return new DataCollection(getTitle(), xData.getUnit(), yData.getUnit(), finalResult);
    }
}
