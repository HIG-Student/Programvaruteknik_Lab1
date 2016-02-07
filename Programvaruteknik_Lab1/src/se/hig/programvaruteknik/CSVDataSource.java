package se.hig.programvaruteknik;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link DataSource} that can be serializable
 */
public class CSVDataSource implements DataSource
{
    private Map<LocalDate, Double> data;
    private String name;
    private String unit;

    /**
     * @param name
     *            The name of the data source
     * @param unit
     *            The unit of the data
     * @param filePath
     *            The file to read<br>
     *            The first line is considered a comment<br>
     *            The second line is "{collection name};{unit name}", the rest
     *            should be:
     *            <br>
     *            date;double[;whatever]<br>
     *            Where the data after the double is ignored
     * @throws RuntimeException
     *             If error occurs
     */
    public CSVDataSource(String filePath)
    {
	data = new HashMap<>();

	try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
	{
	    reader.readLine();

	    String line = reader.readLine();
	    String[] info = line.split(";");

	    name = info[0];
	    unit = info[1];

	    while ((line = reader.readLine()) != null)
	    {
		String[] collumn = line.split(";");
		LocalDate date = LocalDate.parse(collumn[0]);
		Double value = Double.parseDouble(collumn[1]);
		data.put(date, value);
	    }
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override
    public String getName()
    {
	return name;
    }

    @Override
    public String getUnit()
    {
	return unit;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@link Collections#unmodifiableMap Unmodifiable} Map
     */
    @Override
    public Map<LocalDate, Double> getData()
    {
	return Collections.unmodifiableMap(data);
    }
}
