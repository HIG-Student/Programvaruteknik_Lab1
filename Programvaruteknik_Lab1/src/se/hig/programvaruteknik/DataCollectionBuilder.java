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
    private Map<String, List<MatchedDataPair>> resultData;// TODO: Ask to remove
    private Map<String, MatchedDataPair> finalResult;// TODO: Ask to remove

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

    static MatchedDataPair mergeData(List<MatchedDataPair> data, MergeType xMergeType, MergeType yMergeType)
    {
	return new MatchedDataPair(
		xMergeType.merge(data, (pair) -> pair.getXValue()),
		yMergeType.merge(data, (pair) -> pair.getYValue()));
    }

    static boolean isSameDate(Entry<LocalDate, Double> xEntry, Entry<LocalDate, Double> yEntry, Resolution resolution)
    {
	return localDateToString(xEntry.getKey(), resolution).equals(localDateToString(yEntry.getKey(), resolution));
    }

    static List<Entry<LocalDate, Double>> cloneSetToList(Set<Entry<LocalDate, Double>> set)
    {
	return set.stream().collect(Collectors.toList());
    }

    static Map<String, MatchedDataPair> mergeMatchedData(Map<String, List<MatchedDataPair>> matchedData, MergeType xMergeType, MergeType yMergeType)
    {
	Map<String, MatchedDataPair> result = new HashMap<String, MatchedDataPair>();

	for (Entry<String, List<MatchedDataPair>> node : matchedData.entrySet())
	{
	    result.put(node.getKey(), mergeData(node.getValue(), xMergeType, yMergeType));
	}

	return result;
    }

    static void addPair(Map<String, List<MatchedDataPair>> map, Entry<LocalDate, Double> xEntry, Entry<LocalDate, Double> yEntry, Resolution resolution)
    {
	String key = localDateToString(xEntry.getKey(), resolution);

	if (!map.containsKey(key)) map.put(key, new LinkedList<>());
	map.get(key).add(new MatchedDataPair(xEntry.getValue(), yEntry.getValue()));
    }

    static Map<String, List<MatchedDataPair>> matchPairsUsingResolution(DataSource xSource, DataSource ySource, Resolution resolution)
    {
	Map<String, List<MatchedDataPair>> result = new HashMap<String, List<MatchedDataPair>>();

	Set<Entry<LocalDate, Double>> unMatchedYEntries = ySource.getData().entrySet();

	for (Entry<LocalDate, Double> xEntry : xSource.getData().entrySet())
	{
	    for (Entry<LocalDate, Double> yEntry : cloneSetToList(unMatchedYEntries))
	    {
		if (isSameDate(xEntry, yEntry, resolution))
		{
		    addPair(result, xEntry, yEntry, resolution);
		    unMatchedYEntries.remove(yEntry);
		}
	    }
	}

	return result;
    }

    /**
     * Build the {@link DataCollection}
     * 
     * @return the resulting {@link DataCollection}
     */
    public DataCollection getResult()
    {
	return new DataCollection(
		getTitle(),
		xData.getUnit(),
		yData.getUnit(),
		mergeMatchedData(matchPairsUsingResolution(xData, yData, resolution), xMergeType, yMergeType));
    }
}
