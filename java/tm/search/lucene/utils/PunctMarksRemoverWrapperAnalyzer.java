package tm.search.lucene.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Fieldable;

import java.io.Reader;
import java.io.IOException;

/**
 * Wraps given Analyzer with PunctMarksRemoverFilter.
 * Adapted from Lucene's LimitTokenCountAnalyzer
 *  @author Sergey Pariev
 */
public class PunctMarksRemoverWrapperAnalyzer extends Analyzer {
    private final Analyzer delegate;

  /**
   * Build an analyzer wrapped with filter.
   */
  public PunctMarksRemoverWrapperAnalyzer(Analyzer delegate) {
    this.delegate = delegate;
  }

  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new PunctMarksRemoverFilter(Version.LUCENE_30,
            delegate.tokenStream(fieldName, reader));
  }

  @Override
  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
    return new PunctMarksRemoverFilter(Version.LUCENE_30,
      delegate.reusableTokenStream(fieldName, reader));
  }

  @Override
  public int getPositionIncrementGap(String fieldName) {
    return delegate.getPositionIncrementGap(fieldName);
  }

  @Override
  public int getOffsetGap(Fieldable field) {
    return delegate.getOffsetGap(field);
  }

  @Override
  public String toString() {
    return "PunctMarksRemoverWrapperAnalyzer(" + delegate.toString() + ")";
  }


}
