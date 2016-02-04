package se.hig.programvaruteknik;

import java.util.Map;

public class DataCollectionBuilder
{
    private Map<String, MatchedDataPair> data;
    private String title;
    private String xUnit;
    private String yUnit;

    public DataCollectionBuilder(String title, String xUnit, String yUnit, Map<String, MatchedDataPair> data)
    {
	this.data = data;
	this.title = title;
	this.xUnit = xUnit;
	this.yUnit = yUnit;
    }

    public String getTitle()
    {
	return title;
    }

    public String getXUnit()
    {
	return xUnit;
    }

    public String getYUnit()
    {
	return yUnit;
    }

    public Map<String, MatchedDataPair> getData()
    {
	return data;
    }

    @Override
    public String toString()
    {
	return "[DataCollectionBuilder: " + title + "]";
    }
}
