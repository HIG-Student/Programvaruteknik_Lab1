package se.hig.programvaruteknik;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;

import se.hig.programvaruteknik.data.CSVDataSource;

@SuppressWarnings("javadoc")
public class TestCSVDataSource
{
    @Test
    public void testCreate()
    {
	File csv = new File("data/smhi-gavle-regn-1954-2015.csv");
	assumeTrue(csv.exists());
	
	CSVDataSource smhi = new CSVDataSource(csv.getPath());
	assertEquals("Incorrect name", "Regn i Gävle", smhi.getName());
	assertEquals("Incorrect Unit", "mm", smhi.getUnit());
	Map<LocalDate, Double> data = smhi.getData();
	assertEquals("Incorrect size", new Integer(21896), new Integer(data.size()));
    }
}
