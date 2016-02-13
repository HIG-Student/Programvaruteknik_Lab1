package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link DataSource} where you can't modify the resulting data
 */
public abstract class UnmodifiableDataSource implements DataSource
{
    protected abstract Map<LocalDate, Double> getRawData();

    @Override
    public final Map<LocalDate, Double> getData()
    {
	return Collections.unmodifiableMap(getRawData());
    }
}
