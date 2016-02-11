package se.hig.programvaruteknik.data;

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
    YEAR("yyyy"),

    /**
     * Group into quarters of a year
     */
    QUARTER("yyyy-'Q'Q"),

    /**
     * Group into months
     */
    MONTH("yyyy-MM"),

    /**
     * Group into weeks
     */
    WEEK("YYYY-'W'w"),

    /**
     * Group into days
     */
    DAY("yyyy-MM-dd");

    private final ResolutionResolver resolver;

    Resolution(ResolutionResolver resolver)
    {
	this.resolver = resolver;
    }

    Resolution(String pattern)
    {
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	resolver = (date) -> date.format(formatter);
    }

    /**
     * Converts a {@link LocalDate} to a key
     * 
     * @param date
     *            the {@link LocalDate} to convert
     * @return The key
     */
    public String toKey(LocalDate date)
    {
	return resolver.resolve(date);
    }

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

    private interface ResolutionResolver
    {
	public String resolve(LocalDate date);
    }
}
