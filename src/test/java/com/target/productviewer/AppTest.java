package com.target.productviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.target.productviewer.repository.ElasticSearch;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private static final Logger log = LoggerFactory.getLogger(AppTest.class);
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    
//    public void ValidateElasticSearchConnectivity()
    public void test()
    {	
    	assertTrue( !ElasticSearch.esClient.connectedNodes().isEmpty() );
    }
}
