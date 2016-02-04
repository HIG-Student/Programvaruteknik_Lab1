package se.hig.programvaruteknik;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

public class TestDataCollectionBuilder {
	@Test
	public void testLocalDateToString() {
		assertEquals("Incorrect convertion", "2016-02-04",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 2, 4), Resolution.DAY));

		assertEquals("Incorrect convertion", "2015-W53",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 3), Resolution.WEEK));

		assertEquals("Incorrect convertion", "2016-W1",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 4), Resolution.WEEK));

		assertEquals("Incorrect convertion", "2016-W1",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 10), Resolution.WEEK));

		assertEquals("Incorrect convertion", "2016-W2",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 11), Resolution.WEEK));

		assertEquals("Incorrect convertion", "2016-01",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.MONTH));

		assertEquals("Incorrect convertion", "2016-03",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 3, 31), Resolution.MONTH));

		assertEquals("Incorrect convertion", "2016-Q1",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.QUARTER));

		assertEquals("Incorrect convertion", "2016-Q1",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 3, 31), Resolution.QUARTER));

		assertEquals("Incorrect convertion", "2016-Q2",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 4, 1), Resolution.QUARTER));

		assertEquals("Incorrect convertion", "2016",
				DataCollectionBuilder.localDateToString(LocalDate.of(2016, 1, 1), Resolution.YEAR));
	}

	@Test
	public void testCreate() {
		LinkedList<LocalDate> dates = new LinkedList<>();

		for (int i = 1; i <= 20; i++)
			dates.add(LocalDate.of(2016, 2, i));
		for (int i = 1; i <= 20; i++)
			dates.add(LocalDate.of(2016, 3, i));

		DataSource sourceA = new DataSource() {

			@Override
			public String getName() {
				return "sourceA";
			}

			@Override
			public String getUnit() {
				return "A";
			}

			@SuppressWarnings("serial")
			@Override
			public Map<LocalDate, Double> getData() {
				return new HashMap<LocalDate, Double>() {
					{
						for (LocalDate date : dates) {
							put(date, 1d);
						}
					}
				};
			}

		};

		DataSource sourceB = new DataSource() {

			@Override
			public String getName() {
				return "sourceB";
			}

			@Override
			public String getUnit() {
				return "B";
			}

			@SuppressWarnings("serial")
			@Override
			public Map<LocalDate, Double> getData() {
				return new HashMap<LocalDate, Double>() {
					{
						for (LocalDate date : dates) {
							put(date, 2d);
						}
					}
				};
			}

		};

		{
			DataCollection coll = new DataCollectionBuilder(sourceA, sourceB, Resolution.DAY).getResult();
			Map<String, MatchedDataPair> data = coll.getData();
			for (LocalDate date : dates) {
				String key = DataCollectionBuilder.localDateToString(date, Resolution.DAY);
				assertTrue("Missing key", data.containsKey(key));
				MatchedDataPair pair = data.get(key);
				assertEquals("Incorrect value", new Double(1), pair.getXValue());
				assertEquals("Incorrect value", new Double(2), pair.getYValue());
			}
		}

		{
			DataCollection coll = new DataCollectionBuilder(sourceA, sourceB, Resolution.MONTH).getResult();
			Map<String, MatchedDataPair> data = coll.getData();

			assertEquals("Incorrect value", 2, data.size());
			assertEquals("Incorrect value", new Double(20), data.get("2016-02").getXValue());
			assertEquals("Incorrect value", new Double(40), data.get("2016-02").getYValue());

			assertEquals("Incorrect value", new Double(20), data.get("2016-03").getXValue());
			assertEquals("Incorrect value", new Double(40), data.get("2016-03").getYValue());
		}

	}

}
