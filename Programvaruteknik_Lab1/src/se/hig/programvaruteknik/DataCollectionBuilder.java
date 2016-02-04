package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    public DataCollectionBuilder(DataSource xData, DataSource yData, Resolution resolution)
    {
	this.xData = xData;
	this.yData = yData;
	this.resolution = resolution;
    }

    public DataCollectionBuilder setTitle(String title)
    {
	this.title = title;
	return this;
    }

    public String getTitle()
    {
	return title == null ? (xData.getName() + " : " + yData.getName()) : title;
    }

    public DataCollectionBuilder setXMergeType(MergeType xMergeType)
    {
	this.xMergeType = xMergeType;
	return this;
    }

    public DataCollectionBuilder setYMergeType(MergeType yMergeType)
    {
	this.yMergeType = yMergeType;
	return this;
    }

    private String localDateToString(LocalDate date)
    {
	switch (resolution)
	{
	case DAY:
	    return "" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
	case WEEK:
	    return "" + date.getYear() + "-W" + (int) Math.floor(date.getDayOfYear() / 7);
	case MONTH:
	    return "" + date.getYear() + "-" + date.getMonthValue();
	case QUARTER:
	    return "" + date.getYear() + "-Q" + date.getMonthValue() % 4;
	case YEAR:
	    return "" + date.getYear();
	default:
	    throw new RuntimeException("Unknown resolution!");
	}
    }

    private void put(String key, MatchedDataPair pair)
    {
	if (!resultData.containsKey(key)) resultData.put(key, new LinkedList<>());

	resultData.get(key).add(pair);
    }

    public DataCollection getResult()
    {
	for (Entry<LocalDate, Double> x : xData.getData().entrySet())
	{
	    for (Entry<LocalDate, Double> y : yData.getData().entrySet())
	    {
		if (localDateToString(x.getKey()).equals(localDateToString(y.getKey())))
		{
		    put(localDateToString(x.getKey()), new MatchedDataPair(x.getValue(), y.getValue()));
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
