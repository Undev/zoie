package proj.zoie.impl.indexing;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;

import proj.zoie.api.ZoieIndexReader;
import proj.zoie.api.indexing.IndexReaderDecorator;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.Browsable;
import com.browseengine.bobo.api.BoboBrowser;
import com.browseengine.bobo.api.MultiBoboBrowser;
import com.browseengine.bobo.facets.FacetHandler;

public class BoboIndexReaderDecorator implements IndexReaderDecorator<BoboIndexReader> {
	private final List<FacetHandler<?>> facetHandlers;
	private static final Logger log = Logger.getLogger(BoboIndexReaderDecorator.class);

	private final ClassLoader _classLoader;
	public BoboIndexReaderDecorator(List<FacetHandler<?>> facetHandlers){
	    this.facetHandlers = facetHandlers;
		_classLoader = Thread.currentThread().getContextClassLoader();
	}

	public BoboIndexReaderDecorator(){
		this(null);
	}

	public BoboIndexReader decorate(ZoieIndexReader<BoboIndexReader> zoieReader) throws IOException {
	    if (zoieReader != null)
	    {
    		Thread.currentThread().setContextClassLoader(_classLoader);
            if (facetHandlers !=null){
                return BoboIndexReader.getInstanceAsSubReader(zoieReader,facetHandlers, null);
    		}
            else{
    		  return BoboIndexReader.getInstanceAsSubReader(zoieReader);
    		}
	    }
	    else
	    {
	      return null;
	    }
	}

	public BoboIndexReader redecorate(BoboIndexReader reader, ZoieIndexReader<BoboIndexReader> newReader, boolean withDeletes)
			throws IOException {
		reader.rewrap(newReader);
		return reader;
	}

    public void setDeleteSet(BoboIndexReader reader, DocIdSet docIds) {
        // do nothing
    }
}
