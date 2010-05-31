package proj.zoie.impl.indexing;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.apache.log4j.Logger;

import proj.zoie.api.ZoieIndexReader;
import proj.zoie.api.indexing.IndexReaderDecorator;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.Browsable;
import com.browseengine.bobo.api.BoboBrowser;
import com.browseengine.bobo.api.MultiBoboBrowser;
import com.browseengine.bobo.facets.FacetHandler;
import com.browseengine.bobo.facets.FacetHandlerFactory;

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
                return BoboIndexReader.getInstanceAsSubReader(zoieReader,facetHandlers);
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


    public static Browsable buildBrowsable(ZoieSystem<BoboIndexReader, Object> zoieSystem) throws IOException {

      List<ZoieIndexReader<BoboIndexReader>> readerList = zoieSystem.getIndexReaders();
      // this call is very fast and the readers would all be decorated
      List<BoboIndexReader> boboReaders = ZoieIndexReader.extractDecoratedReaders(readerList);
      MultiBoboBrowser browser = new MultiBoboBrowser(BoboBrowser.createBrowsables(boboReaders));
      return browser;
    }

}
