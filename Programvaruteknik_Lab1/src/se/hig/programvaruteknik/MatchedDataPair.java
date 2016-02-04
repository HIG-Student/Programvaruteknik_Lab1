package se.hig.programvaruteknik;

public class MatchedDataPair
{
    private Double xValue;
    private Double yValue;

    public MatchedDataPair(Double xValue, Double yValue)
    {
	this.xValue = xValue;
	this.yValue = yValue;
    }

    public Double getXValue()
    {
	return xValue;
    }

    public Double getYValue()
    {
	return yValue;
    }

    @Override
    public String toString()
    {
	return "(" + xValue + " : " + yValue + ")";
    }
}
