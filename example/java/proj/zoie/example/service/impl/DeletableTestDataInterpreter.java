package proj.zoie.example.service.impl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.util.Version;

import proj.zoie.api.indexing.ZoieIndexable;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;

import java.util.Map;

public class DeletableTestDataInterpreter implements ZoieIndexableInterpreter<Map> {

    long _delay;
    final Analyzer _analyzer;

    public DeletableTestDataInterpreter()
    {
      this(0,new StandardAnalyzer(Version.LUCENE_CURRENT));
    }

    public DeletableTestDataInterpreter(long delay)
    {
      this(delay,new StandardAnalyzer(Version.LUCENE_CURRENT));
    }

    public DeletableTestDataInterpreter(long delay,Analyzer analyzer)
    {
      _delay = delay;
      _analyzer = analyzer;
    }

	public ZoieIndexable interpret(final Map src) {
        final String srcString = (String) src.get("src");
        final Boolean delete = (Boolean) src.get("delete");
		String[] parts=srcString.split(" ");
		final long id=Long.parseLong(parts[0])+((long)(Integer.MAX_VALUE)*2L);
		return new ZoieIndexable(){
			public Document buildDocument(){
				Document doc=new Document();

				doc.add(new Field("content",srcString,Store.YES,Index.ANALYZED));
				doc.add(new Field("id",String.valueOf(id),Store.YES,Index.NO));
				try
                {
                  Thread.sleep(_delay); // slow down indexing process
                }
                catch (InterruptedException e)
                {
                }
                return doc;
			}

			public IndexingReq[] buildIndexingReqs(){
				return new IndexingReq[]{new IndexingReq(buildDocument(),getAnalyzer())};
			}

			public Analyzer getAnalyzer(){
				return _analyzer;
			}

			public long getUID() {
				return id;
			}

			public boolean isDeleted() {
				return delete;
			}

			public boolean isSkip() {
				return false;
			}
		};
	}

	public ZoieIndexable convertAndInterpret(Map src) {
		return interpret(src);
	}

}
