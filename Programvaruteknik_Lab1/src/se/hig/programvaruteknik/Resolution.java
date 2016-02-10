package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

/**
 * How to group data
 */
public enum Resolution
{
    /**
     * Group into years
     */
    YEAR
    {
	@Override
	public String toKey(LocalDate date)
	{
	    return date.format(DateTimeFormatter.ofPattern("yyyy"));
	}
    },

    /**
     * Group into quarters of a year
     */
    QUARTER
    {
	@Override
	public String toKey(LocalDate date)
	{
	    return date.format(DateTimeFormatter.ofPattern("yyyy-'Q'Q"));
	}
    },

    /**
     * Group into months
     */
    MONTH
    {
	@Override
	public String toKey(LocalDate date)
	{
	    return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
	}
    },

    /**
     * Group into weeks
     */
    WEEK
    {
	@Override
	public String toKey(LocalDate date)
	{
	    return date.format(DateTimeFormatter.ofPattern("YYYY-'W'w"));
	}
    },

    /**
     * Group into days
     */
    DAY
    {
	@Override
	public String toKey(LocalDate date)
	{
	    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
    };

    /**
     * Converts a {@link LocalDate} to a key
     * 
     * @param date
     *            the {@link LocalDate} to convert
     * @return The key
     */
    public abstract String toKey(LocalDate date);

    /**
     * Converts a {@link Entry} to a key
     * 
     * @param entry
     *            The {@link Entry} to convert
     * @return The key
     */
    public String toKey(Entry<LocalDate, Double> entry)
    {
	return toKey(entry.getKey());
    }
}
