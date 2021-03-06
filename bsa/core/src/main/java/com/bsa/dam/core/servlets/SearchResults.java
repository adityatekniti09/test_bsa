/**
 * 
 */
package com.bsa.dam.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.*;
import org.apache.sling.commons.json.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;


@SlingServlet(name = "search", paths = "/bin/bsa/searchresults", methods =  "GET")

public class SearchResults extends org.apache.sling.api.servlets.SlingAllMethodsServlet   {


	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(SearchResults.class);

	@Reference
	private QueryBuilder queryBuilder;

	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException, IOException {

		ResourceResolver resourceResolver = request.getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);
		//response.setContentType("text/html");
		ValueMap metadata = null;
		String assetPath = null;
		Resource metadataResource = null;
		Node metadataNode = null;
		List<Node> metadataNodeList = new ArrayList<Node>();
		Iterator<Node> nodeItr = null;
		String searchTerm = request.getParameter("searchText");
		String poffset = request.getParameter("offSet");
		String limit = request.getParameter("resultsPerPage");
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("path", "/content/dam/trail-blazer");
		queryMap.put("type", "dam:Asset");
		queryMap.put("p.offset", poffset);
		queryMap.put("p.limit",limit);
		queryMap.put("fulltext",searchTerm);
		queryMap.put("orderby", "@jcr:content/metadata/dc:title");
		queryMap.put("orderby.sort", "asc");
		try {
			Query nQuery = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
			LOG.info("Executing the QueryBuilder with getResults()");
			SearchResult result = nQuery.getResult();
			// paging metadata
			int hitsPerPage = result.getHits().size(); // 20 (set above) or lower
			long totalMatches = result.getTotalMatches();
			long offset = result.getStartIndex();
			long numberOfPages = totalMatches / Long.parseLong(limit);
			JSONObject parentObject = new JSONObject();
			parentObject.put("hitsPerPage",hitsPerPage);
			parentObject.put("totalMatches",totalMatches);
			parentObject.put("offset",offset);
			JSONArray assetsArray = new JSONArray();
			nodeItr = result.getNodes();
			while (nodeItr.hasNext()) {
				assetPath = nodeItr.next().getPath();
				metadataResource = resourceResolver.getResource(assetPath + "/jcr:content/metadata");
				metadataNode = metadataResource.adaptTo(Node.class);
				metadata = metadataResource.adaptTo(ValueMap.class);
				JSONObject obj = new JSONObject();
				obj.put("title",metadata.get("dc:title",""));
				obj.put("downloadPath",assetPath);
				assetsArray.put(obj);
			}
			parentObject.put("assetsObject",assetsArray);
			response.setContentType("application/json");
			try{
				response.getWriter().print(parentObject.toString());
			}catch (IOException e){

			}

		} catch (RepositoryException e) {
			LOG.error("Unalbe to retrieve assets ", e);
			return;
		}catch (JSONException e) {
			LOG.error("Unalbe to retrieve assets ", e);
			return;
		}
	}
}
