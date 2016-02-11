package se.hig.programvaruteknik.data;

import java.time.LocalDate;
import java.util.Map;

/**
 * Interface representing an source of data
 */
public interface DataSource
{
    /**
     * Get the name of the data source
     * 
     * @return The name
     */
    public String getName();

    /**
     * Get the unit of the data source
     * 
     * @return The unit
     */
    public String getUnit();

    /**
     * Get the data in the data source
     * 
     * @return The data
     */
    public Map<LocalDate, Double> getData();
}
