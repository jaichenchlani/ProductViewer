package com.target.productviewer.repository;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.target.productviewer.ProductController;


public class ElasticSearch {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	private static TransportClient esClient = null;
    public static String ES_CLUSTERNAME = "elasticsearch";
    public static String ES_HOSTS = "127.0.0.1:9300";
    public static int ES_TRANSPORT_PING_TIMEOUT_IN_SECS = 10;
    public static String ES_INDEX = "myelasticdb";
    public static String ES_TYPE = "myretail";

    static {
        try {
            log.info("Connecting to ElasticSearch Data Store");
        	ElasticSearch.connect();
        }
        catch(Exception e) {
            System.out.println("Error instantiating ElasticSearchManager " +e.getMessage());
            System.exit(1);
        }
    }

    public static boolean connect() throws Exception {

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("client.transport.ping_timeout", ES_TRANSPORT_PING_TIMEOUT_IN_SECS+"s")
                .put("cluster.name", ES_CLUSTERNAME).build();
        esClient = new TransportClient(settings);

        String[] hs = ES_HOSTS.split(",");
        for(String h : hs) {
            String[] hp = h.split(":");
            esClient = esClient.addTransportAddress(new InetSocketTransportAddress(hp[0], Integer.parseInt(hp[1])));
        }
        IndicesExistsResponse elemIndexExists =  esClient.admin().indices().prepareExists(ES_INDEX).execute().actionGet();
        if(!elemIndexExists.isExists()){
            throw new Exception("Exit no ES index " + ES_INDEX);
        }

        return true;
    }

    public static SearchResponse search(String idx, String type, QueryBuilder qb, int from, int size) {
        return esClient.prepareSearch(idx)
                .setTypes(type)
                .setQuery(qb)
                .setFrom(from)
                .setSize(size)
                .execute().actionGet();
    }
    
    public static GetResponse getDocumentById(String index, String type, String docId) {
    	return esClient.prepareGet(ES_INDEX, ES_TYPE, docId)
    			.execute()
    			.actionGet();
    }

    public static IndexResponse setDocumentById(String index, String type, String docId, String json) {
        IndexRequestBuilder request = esClient.prepareIndex(index, type)
                .setId(docId)
                .setSource(json);
        return request.get();
    }
    
    
    public static DeleteResponse deleteDocumentById(String index, String type, String docId) {
        DeleteRequestBuilder builder = esClient.prepareDelete(index, type, docId);
        return builder.get();
    }
}
