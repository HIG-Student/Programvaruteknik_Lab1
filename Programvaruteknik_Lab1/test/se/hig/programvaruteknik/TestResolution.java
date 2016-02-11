package se.hig.programvaruteknik;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import se.hig.programvaruteknik.data.Resolution;

@SuppressWarnings("javadoc")
public class TestResolution
{
    @Test
    public void testDay()
    {
	assertEquals("2016-02-04", Resolution.DAY.toKey(LocalDate.of(2016, 2, 4)));
    }

    @Test
    public void testWEEK()
    {
	assertEquals("2015-W53", Resolution.WEEK.toKey(LocalDate.of(2016, 1, 3)));
	assertEquals("2016-W1", Resolution.WEEK.toKey(LocalDate.of(2016, 1, 4)));
	assertEquals("2016-W1", Resolution.WEEK.toKey(LocalDate.of(2016, 1, 10)));
	assertEquals("2016-W2", Resolution.WEEK.toKey(LocalDate.of(2016, 1, 11)));
    }

    @Test
    public void testMONTH()
    {
	assertEquals("2016-01", Resolution.MONTH.toKey(LocalDate.of(2016, 1, 1)));
	assertEquals("2016-03", Resolution.MONTH.toKey(LocalDate.of(2016, 3, 31)));
    }

    @Test
    public void testQUARTER()
    {
	assertEquals("2016-Q1", Resolution.QUARTER.toKey(LocalDate.of(2016, 1, 1)));
	assertEquals("2016-Q1", Resolution.QUARTER.toKey(LocalDate.of(2016, 3, 31)));
	assertEquals("2016-Q2", Resolution.QUARTER.toKey(LocalDate.of(2016, 4, 1)));
    }

    @Test
    public void testYEAR()
    {
	assertEquals("2016", Resolution.YEAR.toKey(LocalDate.of(2016, 1, 1)));
    }
}
