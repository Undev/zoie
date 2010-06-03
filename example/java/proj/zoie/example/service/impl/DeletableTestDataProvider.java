package proj.zoie.example.service.impl;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.File;
import java.util.*;

import proj.zoie.api.DataConsumer.DataEvent;
import proj.zoie.impl.indexing.StreamDataProvider;

public class DeletableTestDataProvider extends StreamDataProvider<Map>
{
    private long _currentVersion;
    private Iterator<Map> _currentIterator;
    private static final String[] SAMPLE_DATA = new String[]{
            "01 chunky",
            "02 backon",
            "03 fiz",
            "04 baz",
            "05 foo",
            "06 bar",
            "07 ping",
            "08 pong",
            "09 zip",
            "10 zap"
    };
    private List<Map> _events;
    private Integer _numOfIterations;
	public DeletableTestDataProvider(int numOfIterations)
	{
		super();
        this._numOfIterations = numOfIterations;
		reset();
	}

	@Override
	public void reset()
	{
        _events = buildEventsFromSampleData(SAMPLE_DATA, _numOfIterations);
        _currentIterator = _events.iterator();
	}

    private List<Map> buildEventsFromSampleData(String[] sampleData, int numOfIterations)
    {
        List<Map> r = new ArrayList<Map>();
        // add records first
        for( String s: sampleData) {
            r.add(mapFromVal(s,false));
        }
        // delete, then reinsert
        for(int i=0; i< numOfIterations; i++)
        {
            for( String s: sampleData) {
                r.add(mapFromVal(s,true));
                r.add(mapFromVal(s,false));

            }

        }
        return r;
    }

    private Map mapFromVal(String val, boolean del){
        Map r = new HashMap();
        r.put("src", val);
        r.put("delete",Boolean.valueOf(del));
        return r;
    }

	@Override
	public DataEvent<Map> next() {
        DataEvent<Map> r = null;
        try{
        if(_currentIterator.hasNext()){
            Map v = _currentIterator.next();
            r = new DataEvent<Map>(_currentVersion++,v);
        }
        } catch (NoSuchElementException nsee){
            r = null;
        }
    	return r;
	}
}