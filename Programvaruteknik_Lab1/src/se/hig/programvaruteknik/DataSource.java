package se.hig.programvaruteknik;

import java.time.LocalDate;
import java.util.Map;

public interface DataSource
{
    public String getName();

    public String getUnit();

    public Map<LocalDate, Double> getData();
}
