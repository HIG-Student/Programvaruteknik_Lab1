package se.hig.programvaruteknik;

import java.util.List;
import java.util.Map;

public class DataCollection
{
    private String title = null;
    private DataSource xData;
    private DataSource yData;
    private Resolution resolution;
    private Map<String, List<MatchedDataPair>> resultData;
    private Map<String, MatchedDataPair> finalResult;

    public DataCollection(DataSource xData, DataSource yData, Resolution resolution)
    {
	this.xData = xData;
	this.yData = yData;
	this.resolution = resolution;
    }

    public DataCollection setTitle(String title)
    {
	this.title = title;
	return this;
    }

    public String getTitle()
    {
	return title == null ? (xData.getName() + " : " + yData.getName()) : title;
    }

    public DataCollectionBuilder getResult()
    {
	return null;// TODO: implement
    }
}
