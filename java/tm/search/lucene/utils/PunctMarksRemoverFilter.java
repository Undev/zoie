package tm.search.lucene.utils;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;


/**
 * Lucene filter that replaces punctuation marks with spaces.
 *
 *  @author Sergey Pariev
 */
public final class PunctMarksRemoverFilter extends TokenFilter{

 	private final TermAttribute termAtt = addAttribute(TermAttribute.class);

    private static char[] punctuationMarks;
    static {
        punctuationMarks = new char[]{'-','(', ')', '[', ']', '_', ',', '`', '\'','=', '>', '<', '"'};
        Arrays.sort(punctuationMarks);
    }
 	/**
    *
 	* Create a new PunctMarksRemoverFilter filter that replaces punctuation marks with spaces.
 	*
 	* @param matchVersion See <a href="#version">above</a>
 	* @param in TokenStream to filter
 	*/
 	public PunctMarksRemoverFilter(Version matchVersion, TokenStream in) {
 	    super(in);
 	}

 	@Override
 	public final boolean incrementToken() throws IOException {
 	if (input.incrementToken()) {
     	final char[] buffer = termAtt.termBuffer();
 	    final int length = termAtt.termLength();
        for(int i=0;i<length;i++){
            if(Arrays.binarySearch(punctuationMarks, buffer[i]) >= 0) { //punctuation mark found
                buffer[i] = ' ';
            }
        }
 	    return true;
 	} else {
        return false;
    }

 	}
}