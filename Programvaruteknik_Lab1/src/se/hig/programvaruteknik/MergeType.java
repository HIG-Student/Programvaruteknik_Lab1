package se.hig.programvaruteknik;

import java.util.List;
import java.util.function.Function;

/**
 * Lambda that defines how to merge {@link MatchedDataPair MatchedDataPairs}
 */
public interface MergeType
{
    /**
     * Sum all values
     */
    public final static MergeType SUM = (list, extractor) ->
    {
	Double sum = 0d;
	for (MatchedDataPair pair : list)
	    sum += extractor.apply(pair);
	return sum;
    };

    /**
     * Take the average of all values
     */
    public final static MergeType AVERAGE = (list, extractor) ->
    {
	return SUM.merge(list, extractor) / list.size();
    };

    /**
     * Take the median of the values
     */
    public final static MergeType MEDIAN = (list, extractor) ->
    {
	return extractor.apply(list.get((int) Math.floor(list.size() / 2)));
    };

    /**
     * Merges {@link MatchedDataPair MatchedDataPairs}
     * 
     * @param data
     *            {@link MatchedDataPair MatchedDataPairs}
     * @param extractor
     *            {@link Function Lambda} that extracts
     *            the value form the
     *            {@link MatchedDataPair}
     * @return merged {@link MatchedDataPair}
     */
    public Double merge(List<MatchedDataPair> data, Function<MatchedDataPair, Double> extractor);
}
