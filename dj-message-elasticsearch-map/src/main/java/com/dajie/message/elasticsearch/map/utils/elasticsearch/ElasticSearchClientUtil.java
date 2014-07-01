package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.model.map.RegionInfoObject;

public class ElasticSearchClientUtil {
	
	private static Client client = ElasticSearchConnectionUtil.getInstance();
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchClientUtil.class);
	
	/**
	 * 
	 * 
	 * */
	public static List<IndexResponse> addDoc(String indexName,String indexType,long ttl,String... jsons)
	{
		
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return null;
		}
		
		if(StringUtils.isEmpty(indexType))
		{
			logger.error("indexType is null.");
			return null;
		}
		
		if(jsons == null || jsons.length == 0)
		{
			logger.error("json is null.");
			return null;
		}
		
		List<IndexResponse> indexResponseList = new ArrayList<IndexResponse>();
		
		if(jsons.length == 1)
		{
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType)
				    .setSource(jsons[0]).setRefresh(true);
			
			if(ttl>0)
			{
				indexRequestBuilder.setTTL(ttl);
			}
			
			IndexResponse i = indexRequestBuilder.execute().actionGet();
			
			if(StringUtils.isNotEmpty(i.getId()))
			{
				indexResponseList.add(i);
			}else
			{
				logger.error("error when add a doc to es.");
			}
			
		}
		else
		{
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			
			for(String s : jsons)
			{
				IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType)
					    .setSource(s);
				
				if(ttl>0)
				{
					indexRequestBuilder.setTTL(ttl);
				}
				
				bulkRequest.add(indexRequestBuilder);
			}
			
			BulkResponse bulkResponse = bulkRequest.setRefresh(true) // Needed when the query shall be available immediately
		    .execute().actionGet();
			
			if (bulkResponse.hasFailures()) {
				
				logger.error("there are some error in bulkResponse");
				
				BulkItemResponse[] bulkItemResponses = bulkResponse.getItems();
				
				for(BulkItemResponse b : bulkItemResponses)
				{
					if(StringUtils.isNotEmpty(b.getFailureMessage()))
					{
						logger.error("error is " + b.getFailureMessage());
					}
					else
					{
						indexResponseList.add((IndexResponse) b.getResponse());
					}
				}
			}else
			{
				
				BulkItemResponse[] bulkItemResponses = bulkResponse.getItems();
				
				for(BulkItemResponse b : bulkItemResponses)
				{
					indexResponseList.add((IndexResponse) b.getResponse());
				}
				
			}
			
		}
		return indexResponseList;
	}
	
	/**
	 * 
	 * 
	 * */
	public static IndexResponse updateDoc(String indexName,String indexType,String docId,String json)
	{
		
		if(StringUtils.isEmpty(docId))
		{
			logger.info("docId is null, turn to add new doc");
			
			List<IndexResponse> result = addDoc(indexName,indexType,0,json);
			
			if(result !=null && result.size() > 0)
				return result.get(0);
			else
				return null;
		}
		
		
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return null;
		}
		
		if(StringUtils.isEmpty(indexType))
		{
			logger.error("indexType is null.");
			return null;
		}
		
		if(StringUtils.isEmpty(json))
		{
			logger.error("json is null.");
			return null;
		}
		
		IndexResponse indexResponse = client.prepareIndex(indexName, indexType,docId)
			    .setSource(json)
			    .setRefresh(true) // Needed when the query shall be available immediately
			    .execute().actionGet();
		
		return indexResponse;
	}
	
	
	public static boolean deleteDoc(String indexName,String indexType,String docId)
	{
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return false;
		}
		
		if(StringUtils.isEmpty(indexType))
		{
			logger.error("indexType is null.");
			return false;
		}
		
		if(StringUtils.isEmpty(docId))
		{
			logger.error("docId is null.");
			return false;
		}
		
		DeleteRequestBuilder  deleteRequestBuilder = client.prepareDelete(indexName, indexType, docId);
		
		DeleteResponse response = deleteRequestBuilder.execute().actionGet();
		
		if(response == null) return false;
		
		return response.isFound();
	}
	
	
	/**
	 * 
	 * 
	 * */
	public static SearchResponse searchDocs(String indexName,String[] indexTypes,
			 QueryBuilder queryBuilder, FilterBuilder filterBuilder,ScriptModel scriptModel,
			 SortBuilder sortBuilder,int from,int size)
	{
		
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return null;
		}
		
		if(indexTypes == null || indexTypes.length ==0)
		{
			logger.error("indexTypes is null.");
			return null;
		}
		
		if(queryBuilder == null&&filterBuilder == null)
		{
			logger.error("baseQueryBuilder and baseFilterBuilder both are null.");
			
			return null;
		}
		
		
		SearchRequestBuilder requestBuilder = client
				.prepareSearch(indexName).setTypes(indexTypes)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		

		
		//放入排序
		if(sortBuilder!=null)
		{
			requestBuilder.addSort(sortBuilder);
		}
		
		//放入查询
		if(queryBuilder!=null)
		{
			requestBuilder.setQuery(queryBuilder);
		}
		else
		{
			logger.info("query is null, set to match_all query.");
			//为空则match_all
			requestBuilder.setQuery(QueryBuilders.matchAllQuery());
		}
		
		//放入filter
		if(filterBuilder!=null)
		{
			requestBuilder.setPostFilter(filterBuilder);
		}
		
		if(size>0)
		{
			requestBuilder.setFrom(from).setSize(size);
		}else
		{
			requestBuilder.setFrom(0).setSize(1);
		}
		
		if(scriptModel!=null)
		{
			requestBuilder.addScriptField(scriptModel.getName(), scriptModel.getScript(), scriptModel.getParams());
		}
		
		requestBuilder.addField("_source");
		
//		requestBuilder.setExplain(true);
//		requestBuilder.setTrackScores(true);
		
		logger.debug(requestBuilder.toString());
		SearchResponse response = requestBuilder.execute().actionGet();
		
		return response;
		
	}
	
	
	public static SearchResponse searchDocWithScan(String indexName,String[] indexTypes,
			QueryBuilder queryBuilder, FilterBuilder filterBuilder,ScriptModel scriptModel,int size)
	{
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return null;
		}
		
		if(indexTypes == null || indexTypes.length ==0)
		{
			logger.error("indexTypes is null.");
			return null;
		}
		
		if(queryBuilder == null&&filterBuilder== null)
		{
			logger.error("baseQueryBuilder and baseFilterBuilder both are null.");
			
			return null;
		}
		
		SearchRequestBuilder requestBuilder = client
				.prepareSearch(indexName).setTypes(indexTypes)
				.setSearchType(SearchType.SCAN)
				.setSize(500).setScroll(new TimeValue(6000));
				
				//放入查询
				if(queryBuilder!=null)
				{
					requestBuilder.setQuery(queryBuilder);
				}
				else
				{
					logger.info("query is null, set to match_all query.");
					//为空则match_all
					requestBuilder.setQuery(QueryBuilders.matchAllQuery());
				}
				
				//放入filter
				if(filterBuilder!=null)
				{

					requestBuilder.setPostFilter(filterBuilder);

				}
				
				if(size>0)
				{
					requestBuilder.setSize(size);
				}
				
				if(scriptModel!=null)
				{
					requestBuilder.addScriptField(scriptModel.getName(), scriptModel.getScript(), scriptModel.getParams());
				}
				
				requestBuilder.addField("_source");
				
			logger.debug(requestBuilder.toString());
			SearchResponse response = requestBuilder.execute().actionGet();
				
			return response;
	}
	
	
	public static List<SearchResponse> searchResultWithScrollId(String scrollId)
	{
		
		if(StringUtils.isEmpty(scrollId))
		{
			logger.error("scrollId is null. return empty list");
			return new ArrayList<SearchResponse>();
		}
		
		List<SearchResponse> searchResponseList = new ArrayList<SearchResponse>();
		
		SearchResponse scrollResp = null;
		
		while(true)
		{
			SearchScrollRequestBuilder searchScrollRequestBuilder = 
					client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000));
			
			 scrollResp = searchScrollRequestBuilder.execute().actionGet();
			 
			 if (scrollResp.getHits().getHits().length == 0) {
			        break;
			 }
			 
			 logger.debug(scrollResp.toString());

			 searchResponseList.add(scrollResp);
		}
		
		return searchResponseList;
	}
	
	public static Object deleteScrollWithId(String scrollId)
	{
		if(StringUtils.isNotEmpty(scrollId))
		{
			logger.error("scrollId is null. no scroll going to delete.");
			return null;
		}
		
		DeleteRequestBuilder  deleteRequestBuilder = client.prepareDelete().setIndex("_search").setType("scroll").setId(scrollId);
		
		DeleteResponse response = deleteRequestBuilder.execute().actionGet();
		
		if(response == null) return false;
		
		return response.isFound();
		
	}
	
	public static SearchResponse countDoc(String indexName,String[] indexTypes, QueryBuilder queryBuilder,SortBuilder sortBuilder)
	{
		
			if(queryBuilder == null) return null;
		
		
			SearchRequestBuilder requestBuilder = client
			.prepareSearch(indexName).setTypes(indexTypes).setFrom(0).setSize(1)
			.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			//放入查询
			
			requestBuilder.setQuery(queryBuilder);
			//放入排序
			if(sortBuilder!=null)
			{
				requestBuilder.addSort(sortBuilder);
			}
			
			logger.debug(requestBuilder.toString());
			
			SearchResponse response = requestBuilder.execute().actionGet();
			
//			logger.debug(response.toString());
			return response;
	}
	
	
	public static Map<RegionInfoObject,SearchResponse> countDoc(String indexName,String[] indexTypes,
			Map<RegionInfoObject,QueryBuilder> queryBuilderMap,Map<RegionInfoObject,SortBuilder> sortBuilderMap)
	{
		
		if(queryBuilderMap == null || queryBuilderMap.size() == 0 )return null;
		
		Map<RegionInfoObject,SearchResponse> searchResponseMap = new HashMap<RegionInfoObject,SearchResponse>();
		
		if(queryBuilderMap!=null&&queryBuilderMap.size()== 1)
		{
			SearchRequestBuilder requestBuilder = client
					.prepareSearch(indexName).setTypes(indexTypes)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			//放入查询

			RegionInfoObject r = null;
			for(Entry<RegionInfoObject, QueryBuilder> q : queryBuilderMap.entrySet())
			{
				requestBuilder.setQuery(q.getValue());
				r = q.getKey();
				
				if(sortBuilderMap!=null&&sortBuilderMap.get(r)!=null) requestBuilder.addSort(sortBuilderMap.get(r));
				
			}
			
			
			
			SearchResponse response = requestBuilder.execute().actionGet();
			
			if(response.status() == RestStatus.OK)
			{
				searchResponseMap.put(r, response);
			}else
			{
				logger.error("error when count a doc to es.");
			}
			
		}
		else
		{
			MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
			
			for(Entry<RegionInfoObject, QueryBuilder> q : queryBuilderMap.entrySet())
			{
				
				SearchRequestBuilder  searchRequestBuilder  = client
				.prepareSearch(indexName).setTypes(indexTypes).setFrom(0).setSize(1)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(q.getValue());
				
				logger.debug(searchRequestBuilder.toString());
				
				if(sortBuilderMap!=null&&sortBuilderMap.get(q.getKey())!=null) searchRequestBuilder.addSort(sortBuilderMap.get(q.getKey()));
				multiSearchRequestBuilder.add(searchRequestBuilder);
			}
			
			MultiSearchResponse multiSearchRequest = multiSearchRequestBuilder
		    .execute().actionGet();
			

			Item[] multiSearchRequestItems = multiSearchRequest.getResponses();
			
			if(multiSearchRequestItems!=null && multiSearchRequestItems.length > 0)
			{
				int i = 0;

				for(Entry<RegionInfoObject, QueryBuilder> q : queryBuilderMap.entrySet())
				{
					searchResponseMap.put(q.getKey(), multiSearchRequestItems[i].getResponse());
					i++;
				}
			}
		
		}
		return searchResponseMap;
	}
	
	public static GetResponse getDocById(String indexName,String indexType,String _id)
	{
		if(StringUtils.isEmpty(indexName))
		{
			logger.error("indexName is null.");
			return null;
		}
		
		if(StringUtils.isEmpty(indexType))
		{
			logger.error("indexType is null.");
			return null;
		}
		
		if(_id == null)
		{
			logger.error("_id is null.");
			return null;
		}
		
		return client.prepareGet(indexName, indexType, _id).execute().actionGet();
	}

	
}
