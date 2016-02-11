package se.hig.programvaruteknik;

import java.util.Map;

/**
 * A collection of data
 */
public class DataCollection
{
    private Map<String, MatchedDataPair> data;
    private String title;
    private String xUnit;
    private String yUnit;

    /**
     * Populates a collection of data
     * 
     * @param title
     *            The title for this collection
     * @param xUnit
     *            The unit name for the x values
     * @param yUnit
     *            The unit name for the y values
     * @param data
     *            The data to put in this collection
     */
    public DataCollection(String title, String xUnit, String yUnit, Map<String, MatchedDataPair> data)
    {
	this.data = data;
	this.title = title;
	this.xUnit = xUnit;
	this.yUnit = yUnit;
    }

    /**
     * The title
     * 
     * @return The title
     */
    public String getTitle()
    {
	return title;
    }

    /**
     * Get the unit for the x values
     * 
     * @return The unit for the x values
     */
    public String getXUnit()
    {
	return xUnit;
    }

    /**
     * Get the unit for the y values
     * 
     * @return The unit for the y values
     */
    public String getYUnit()
    {
	return yUnit;
    }

    /**
     * Get the data in this collection
     * 
     * @return The data in this collection
     */
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
