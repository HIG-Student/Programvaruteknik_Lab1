package se.hig.programvaruteknik;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestDataCollectionBuilder
{
    @Test
    public void testLocalDateToString()
    {
	assertEquals(
		"Incorrect convertion",
		"2016-02-04",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 2, 4), Resolution.DAY));

	assertEquals(
		"Incorrect convertion",
		"2015-W53",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 3), Resolution.WEEK));

	assertEquals(
		"Incorrect convertion",
		"2016-W1",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 4), Resolution.WEEK));

	assertEquals(
		"Incorrect convertion",
		"2016-W1",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 10), Resolution.WEEK));

	assertEquals(
		"Incorrect convertion",
		"2016-W2",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 11), Resolution.WEEK));

	assertEquals(
		"Incorrect convertion",
		"2016-01",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.MONTH));

	assertEquals(
		"Incorrect convertion",
		"2016-03",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 3, 31), Resolution.MONTH));

	assertEquals(
		"Incorrect convertion",
		"2016-Q1",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.QUARTER));

	assertEquals(
		"Incorrect convertion",
		"2016-Q1",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 3, 31), Resolution.QUARTER));

	assertEquals(
		"Incorrect convertion",
		"2016-Q2",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 4, 1), Resolution.QUARTER));

	assertEquals(
		"Incorrect convertion",
		"2016",
		DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.YEAR));
    }

    @SuppressWarnings("serial")
    private static final LinkedList<LocalDate> dates = new LinkedList<LocalDate>()
    {
	{
	    // YEAR
	    {
		// QUARTER
		{
		    // MONTH
		    {
			// WEEK
			{
			    for (int i = 1; i <= 7; i++)
				add(LocalDate.of(2016, 2, i));
			}
			// WEEK
			{
			    for (int i = 8; i <= 14; i++)
				add(LocalDate.of(2016, 2, i));
			}
			// WEEK
			{
			    for (int i = 15; i <= 21; i++)
				add(LocalDate.of(2016, 2, i));
			}
		    }
		    // MONTH
		    {
			// WEEK
			{
			    for (int i = 7; i <= 13; i++)
				add(LocalDate.of(2016, 3, i));
			}
			// WEEK
			{
			    for (int i = 14; i <= 20; i++)
				add(LocalDate.of(2016, 3, i));
			}
		    }
		}
	    }

	    // YEAR
	    {
		// QUARTER
		{
		    // MONTH
		    {
			// WEEK
			{
			    for (int i = 2; i <= 8; i++)
				add(LocalDate.of(2017, 1, i));
			}
		    }
		}
	    }
	}
    };

    private DataSource getDataSource(String name, String unit, HashMap<LocalDate, Double> data)
    {
	return new DataSource()
	{
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

	    @Override
	    public Map<LocalDate, Double> getData()
	    {
		return data;
	    }
	};
    }

    @SuppressWarnings("serial")
    private DataSource getDataSourceA()
    {
	return getDataSource("sourceA", "A", new HashMap<LocalDate, Double>()
	{
	    {
		for (LocalDate date : dates)
		{
		    put(date, 1d);
		}
	    }
	});
    }

    @SuppressWarnings("serial")
    private DataSource getDataSourceB()
    {
	return getDataSource("sourceB", "B", new HashMap<LocalDate, Double>()
	{
	    {
		for (LocalDate date : dates)
		{
		    put(date, 2d);
		}
	    }
	});
    }

    private interface testCollectionCase
    {
	public void test(LocalDate date, Double x, Double y);
    }

    private void testCollection(Resolution resolution, MergeType mergeType, testCollectionCase testCase)
    {
	DataCollectionBuilder builder = new DataCollectionBuilder(getDataSourceA(), getDataSourceB(), resolution);
	builder.setXMergeType(mergeType);
	builder.setYMergeType(mergeType);
	Map<String, MatchedDataPair> data = builder.getResult().getData();
	for (LocalDate date : dates)
	{
	    String key = DataCollectionBuilder.localDateToString(date, resolution);
	    assertTrue("Missing key", data.containsKey(key));
	    MatchedDataPair pair = data.get(key);
	    testCase.test(date, pair.getXValue(), pair.getYValue());
	}
    }

    @Test
    public void testTitle()
    {
	DataCollectionBuilder builder = new DataCollectionBuilder(getDataSourceA(), getDataSourceB(), Resolution.DAY);
	assertEquals("sourceA : sourceB", builder.getTitle());
	assertEquals("sourceA : sourceB", builder.getResult().getTitle());

	builder.setTitle("Test New Title");

	assertEquals("Test New Title", builder.getTitle());
	assertEquals("Test New Title", builder.getResult().getTitle());
    }

    @Test
    public void testDay_SUM()
    {
	testCollection(Resolution.DAY, MergeType.SUM, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(1), x);
	    assertEquals("Incorrect value", new Double(2), y);
	});
    }

    @Test
    public void testWeek_SUM()
    {
	testCollection(Resolution.WEEK, MergeType.SUM, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(7), x);
	    assertEquals("Incorrect value", new Double(14), y);
	});
    }

    @Test
    public void testMonth_SUM()
    {
	testCollection(Resolution.MONTH, MergeType.SUM, (date, x, y) ->
	{
	    switch (date.getMonth())
	    {
	    case JANUARY:
		assertEquals(new Double(7), x);
		assertEquals(new Double(14), y);
		break;
	    case FEBRUARY:
		assertEquals(new Double(21), x);
		assertEquals(new Double(42), y);
		break;
	    case MARCH:
		assertEquals(new Double(14), x);
		assertEquals(new Double(28), y);
		break;
	    default:
		fail("Unexpected month");
		break;
	    }
	});
    }

    @Test
    public void testQuarter_SUM()
    {
	testCollection(Resolution.QUARTER, MergeType.SUM, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(35), x);
		assertEquals(new Double(70), y);
		break;
	    case 2017:
		assertEquals(new Double(7), x);
		assertEquals(new Double(14), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }

    @Test
    public void testYear_SUM()
    {
	testCollection(Resolution.YEAR, MergeType.SUM, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(35), x);
		assertEquals(new Double(70), y);
		break;
	    case 2017:
		assertEquals(new Double(7), x);
		assertEquals(new Double(14), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }

    @Test
    public void testDay_AVERAGE()
    {
	testCollection(Resolution.DAY, MergeType.AVERAGE, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(1), x);
	    assertEquals("Incorrect value", new Double(2), y);
	});
    }

    @Test
    public void testWeek_AVERAGE()
    {
	testCollection(Resolution.WEEK, MergeType.AVERAGE, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(1), x);
	    assertEquals("Incorrect value", new Double(2), y);
	});
    }

    @Test
    public void testMonth_AVERAGE()
    {
	testCollection(Resolution.MONTH, MergeType.AVERAGE, (date, x, y) ->
	{
	    switch (date.getMonth())
	    {
	    case JANUARY:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case FEBRUARY:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case MARCH:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected month");
		break;
	    }
	});
    }

    @Test
    public void testQuarter_AVERAGE()
    {
	testCollection(Resolution.QUARTER, MergeType.AVERAGE, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case 2017:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }

    @Test
    public void testYear_AVERAGE()
    {
	testCollection(Resolution.YEAR, MergeType.AVERAGE, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case 2017:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }

    @Test
    public void testDay_MEDIAN()
    {
	testCollection(Resolution.DAY, MergeType.MEDIAN, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(1), x);
	    assertEquals("Incorrect value", new Double(2), y);
	});
    }

    @Test
    public void testWeek_MEDIAN()
    {
	testCollection(Resolution.WEEK, MergeType.MEDIAN, (date, x, y) ->
	{
	    assertEquals("Incorrect value", new Double(1), x);
	    assertEquals("Incorrect value", new Double(2), y);
	});
    }

    @Test
    public void testMonth_MEDIAN()
    {
	testCollection(Resolution.MONTH, MergeType.MEDIAN, (date, x, y) ->
	{
	    switch (date.getMonth())
	    {
	    case JANUARY:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case FEBRUARY:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case MARCH:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected month");
		break;
	    }
	});
    }

    @Test
    public void testQuarter_MEDIAN()
    {
	testCollection(Resolution.QUARTER, MergeType.MEDIAN, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case 2017:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }

    @Test
    public void testYear_MEDIAN()
    {
	testCollection(Resolution.YEAR, MergeType.MEDIAN, (date, x, y) ->
	{
	    switch (date.getYear())
	    {
	    case 2016:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    case 2017:
		assertEquals(new Double(1), x);
		assertEquals(new Double(2), y);
		break;
	    default:
		fail("Unexpected year");
		break;
	    }
	});
    }
}
