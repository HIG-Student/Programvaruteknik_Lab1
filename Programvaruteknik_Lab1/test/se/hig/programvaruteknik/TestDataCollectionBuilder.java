package se.hig.programvaruteknik;

import static org.junit.Assert.*;

import java.time.LocalDate;

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

}
