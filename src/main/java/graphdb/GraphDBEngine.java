package graphdb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.CLI;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GraphDBEngine {

	private static GraphDatabaseService graphDb;
	private static RestCypherQueryEngine engine;
	
	public GraphDBEngine()
	{
         String host = null;
         String dbname = null;

			try
			{
				//graphDb = new RestGraphDatabase("http://localhost:7474/db/data");
				//graphDb = new RestGraphDatabase("http://"+ graphdbip + ":7474/db/data");

				host = CLI.config.getStringParam("graphdb","gdb_host");
				String username = CLI.config.getStringParam("graphdb","gdb_username");
				String password = CLI.config.getStringParam("graphdb","gdb_password");
				dbname = CLI.config.getStringParam("graphdb","gdb_dbname");

				//String connection_string = "remote:" + host + "/" + dbname;

				graphDb = new RestGraphDatabase("http://"+ host + ":7474/db/" + dbname);


				//factory = new OrientGraphFactory(connection_string,username,password).setupPool(10, 100);


			}
			catch(Exception ex)
			{
				System.out.println("Could not init REST DB");
			}
		
			//engine = new RestCypherQueryEngine(new RestAPIFacade("http://localhost:7474/db/data"));
			engine = new RestCypherQueryEngine(new RestAPIFacade("http://"+ host + ":7474/db/" + dbname) );
			
	}
	
	public Map<Long,Long> getPerfMetrics(long appNodeId)
	{
		
		Map<Long,Long> perfMetrics = new HashMap<Long,Long>();
		
		ArrayList<Long> pluginIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node(*), n = node(" + appNodeId + ")";
					execStr += " match x-[r]->n";
					execStr += " where type(r) = \"" + RelType.isConnected.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return x";
			result = engine.query( execStr,null );
			
			
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("x");
					   //for (String prop : x.getPropertyKeys()) {
					   //   System.out.println(prop +": "+x.getProperty(prop));
					   //}
					   pluginIdList.add(x.getId());
						
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem pluginlist getperfmetrics " + ex.toString());
		}
		
		//we now want to get performance info on the plugins
		for(long plugid : pluginIdList)
		{
			//Node nodeId = graphDb.getNodeById(plugid);
			long relId = getEdgeId(plugid,appNodeId,RelType.isConnected);
			if(relId != -1)
			{
				Relationship relationship = graphDb.getRelationshipById(relId);
				long perfmetric = Long.parseLong(relationship.getProperty("perfmetric").toString());
				perfMetrics.put(plugid, perfmetric);
			}
			
		}	
		return perfMetrics;
	}
	
	public ArrayList getRegions()
	{
		ArrayList<Long> regionIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			String execStr = "MATCH (n:`Region`) RETURN n";		
			result = engine.query( execStr,null );
					
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("n");
					   regionIdList.add(x.getId());
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem pluginlist getAgentFromPlugin " + ex.toString());
		}
		
		return regionIdList;
		
	}
	
	
	public ArrayList getAgentsInRegion(long regionId)
	{
		ArrayList<Long> agentIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node(" + regionId + "), n = node(*)";
					execStr += " match n-[r]->x";
					execStr += " where type(r) = \"" + RelType.isAgent.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return n";
					
			result = engine.query( execStr,null );
			
			
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("n");
					   //for (String prop : x.getPropertyKeys()) {
					   //   System.out.println(prop +": "+x.getProperty(prop));
					   //}
					   agentIdList.add(x.getId());
					   
						
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem pluginlist getAgentFromPlugin " + ex.toString());
		}
		/*
		if(agentIdList.size() == 1)
		{
			return agentIdList.get(0);
		}
		
		if(agentIdList.size() > 1)
		{
			System.out.println("problem pluginlist getAgentFromPlugin : should only be one agent");
		}
		*/
		return agentIdList;
		//return agentId;
	}
	
	public ArrayList getPluginInAgent(long agentId)
	{
		ArrayList<Long> pluginIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node(" + agentId + "), n = node(*)";
					execStr += " match n-[r]->x";
					execStr += " where type(r) = \"" + RelType.isPlugin.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return n";
					
			result = engine.query( execStr,null );
			
			
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("n");
					   //for (String prop : x.getPropertyKeys()) {
					   //   System.out.println(prop +": "+x.getProperty(prop));
					   //}
					   pluginIdList.add(x.getId());
					   
						
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem pluginlist getAgentFromPlugin " + ex.toString());
		}
		/*
		if(agentIdList.size() == 1)
		{
			return agentIdList.get(0);
		}
		
		if(agentIdList.size() > 1)
		{
			System.out.println("problem pluginlist getAgentFromPlugin : should only be one agent");
		}
		*/
		return pluginIdList;
		//return agentId;
	}
	
	public long getRegionFromAgent(long agentId)
	{
		
		long regionId = -1;
		ArrayList<Long> regionIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node("+ agentId +"), n = node(*)";
					execStr += " match x-[r]->n";
					execStr += " where type(r) = \"" + RelType.isAgent.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return n";
					
			result = engine.query( execStr,null );
			
			
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("n");
					   //for (String prop : x.getPropertyKeys()) {
					   //   System.out.println(prop +": "+x.getProperty(prop));
					   //}
					   regionIdList.add(x.getId());
						
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem regionlist getRegionFromAgent " + ex.toString());
		}
		
		if(regionIdList.size() == 1)
		{
			return regionIdList.get(0);
		}
		
		if(regionIdList.size() > 1)
		{
			System.out.println("problem regionlist getRegionFromAgent : should only be one agent");
		}
		
		
		return regionId;
	}
	
	public long getAgentFromPlugin(long pluginId)
	{
		long agentId = -1;
		ArrayList<Long> agentIdList = new ArrayList<Long>();
		QueryResult<Map<String, Object>> result;
		//get list of plugins for application
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node(" + pluginId + "), n = node(*)";
					execStr += " match x-[r]->n";
					execStr += " where type(r) = \"" + RelType.isPlugin.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return n";
			result = engine.query( execStr,null );
			
			
			 Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) 
			 { 
				 for (Map<String,Object> row : result) 
				 {
					   Node x = (Node)row.get("n");
					   //for (String prop : x.getPropertyKeys()) {
					   //   System.out.println(prop +": "+x.getProperty(prop));
					   //}
					   agentIdList.add(x.getId());
						
				}
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("problem pluginlist getAgentFromPlugin " + ex.toString());
		}
		
		if(agentIdList.size() == 1)
		{
			return agentIdList.get(0);
		}
		
		if(agentIdList.size() > 1)
		{
			System.out.println("problem pluginlist getAgentFromPlugin : should only be one agent");
		}
		
		
		return agentId;
	}
	
	public long getAppNodeId(String application)
	{
		QueryResult<Map<String, Object>> result;
		long nodeId = -1;
		int nodeCount = 0;
		
		try ( Transaction tx = graphDb.beginTx() )
		{
					String execStr = "MATCH (a:Application { applicationname:\"" + application + "\" })";
					execStr += "RETURN a";
					result = engine.query(execStr, null);
					
					//result = engine.execute( "match (n {regionname: '" + region + "'}) return n" );	
					Iterator<Map<String, Object>> iterator=result.iterator(); 
					
					if(iterator.hasNext()) { 
					
					   Map<String,Object> row= iterator.next(); 
					   Node node  = (Node) row.get("a");
						
					   nodeCount++;
					   nodeId = node.getId();
					 }
				
			tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Error : Checking Application=" + application + " " + ex.toString());
		}
		if(nodeCount > 1)
		{
			System.out.println("Error : duplicate nodes!");
			System.out.println("Could not add Application=" + application);
		}
		//System.out.println("getNodeId=" + nodeId);
		//System.out.println("NodeId : Region=" + region + " agent=" + agent + " plugin=" + plugin + " nodeId=" + nodeId);
		return nodeId;
	}
	
	public Boolean isNode(String region, String agent, String plugin)
	{
		long nodeId = -1;
		try ( Transaction tx = graphDb.beginTx() )
		{
			nodeId = getNodeId(region, agent, plugin);
			tx.success();
		}
		if(nodeId != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public long getNodeId(String region, String agent, String plugin)
	{
		QueryResult<Map<String, Object>> result;
		long nodeId = -1;
		int nodeCount = 0;
		
		try ( Transaction tx = graphDb.beginTx() )
		{
			if((region != null) && (agent == null) && (plugin == null)) //region node
			{
				
					String execStr = "MATCH (r:Region { regionname:\"" + region + "\" })";
					execStr += "RETURN r";
					result = engine.query(execStr, null);
					
					//result = engine.execute( "match (n {regionname: '" + region + "'}) return n" );	
					Iterator<Map<String, Object>> iterator=result.iterator(); 
					
					if(iterator.hasNext()) { 
					
					   Map<String,Object> row= iterator.next(); 
					   Node node  = (Node) row.get("r");
						
					   nodeCount++;
					   nodeId = node.getId();
					 }
				
			}
			else if((region != null) && (agent != null) && (plugin == null)) //agent node
			{
					String execStr = "MATCH (r:Region { regionname:\"" + region + "\" })<-[:isAgent]-(Agent)";
					execStr += "WHERE Agent.agentname = \"" + agent + "\"";
					execStr += "RETURN Agent";
					result = engine.query( execStr,null );
					Iterator<Map<String, Object>> iterator=result.iterator(); 
					 if(iterator.hasNext()) { 
					   Map<String,Object> row= iterator.next(); 
					   //out.print("Total nodes: " + row.get("total"));
					   Node node  = (Node) row.get("Agent");
					   nodeCount++;
					   nodeId = node.getId();
					 }
				
			}
			else if((region != null) && (agent != null) && (plugin != null)) //plugin node
			{
				long agentNodeId = getNodeId(region, agent, null); //getting the agentNodeId
				if(agentNodeId != -1)
				{
						String execStr = "match (Agent)<-[:isPlugin]-(Plugin { pluginname:\"" + plugin + "\" })"; 
						execStr += "where id(Agent) = " + agentNodeId + " "; 
						execStr += "RETURN Plugin";
						result = engine.query( execStr,null);
						   
						Iterator<Map<String, Object>> iterator=result.iterator(); 
						 if(iterator.hasNext()) { 
							   
						   Map<String,Object> row= iterator.next(); 
						   //out.print("Total nodes: " + row.get("total"));
						   Node node  = (Node) row.get("Plugin");
						   nodeCount++;
						   nodeId = node.getId();
						 }
					
				}
			}
			tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Error : Checking Region=" + region + " Agent=" + agent + " Plugin=" + plugin + " " + ex.toString());
		}
		if(nodeCount > 1)
		{
			System.out.println("Error : duplicate nodes!");
			System.out.println("Could not add Region=" + region + " Agent=" + agent + " Plugin=" + plugin);
		}
		//System.out.println("getNodeId=" + nodeId);
		//System.out.println("NodeId : Region=" + region + " agent=" + agent + " plugin=" + plugin + " nodeId=" + nodeId);
		return nodeId;
	}

	public String getPropertyByNodeId(String property, long nodeId)
	{
		String prop = null;
		try ( Transaction tx = graphDb.beginTx() )
		{
			prop = graphDb.getNodeById(nodeId).getProperty(property).toString();
			tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Error : Getting Name from NodeId " + ex.toString());
		}
		return prop;
	}

	public String getPluginParamsByNodeId(String param, long nodeId)
	{
		String prop = null;
		try ( Transaction tx = graphDb.beginTx() )
		{
			prop = graphDb.getNodeById(nodeId).getProperty("configparams").toString();
			tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Error : Getting Name from NodeId " + ex.toString());
		}
		
		String[] params = prop.split(",");
		for(String keyval : params)
		{
			String[] keyvals = keyval.split("=");
			if(keyvals[0].equals(param))
			{
				return keyvals[1];
			}
		}
		
		return prop;
	}

	public String getPropertyByRelationId(String property, long nodeSource, long nodeDest, RelType type)
	{
		long relId = getEdgeId(nodeSource, nodeDest, type);
		if(relId == -1)
		{
			return null;
		}
				
		String prop = null;
		try ( Transaction tx = graphDb.beginTx() )
		{
			prop = graphDb.getRelationshipById(relId).getProperty(property).toString();
			tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Error : Getting Prop from RelId " + ex.toString());
		}
		return prop;
	}

	public Map<String,String> getNodeParams(long nodeId)
	{
		Map<String,String> paramMap = new HashMap<String,String>();
		if(nodeId != -1)
		{
			try ( Transaction tx = graphDb.beginTx() )
			{
				Node pluginNode = graphDb.getNodeById(nodeId);
				for(String propKey : pluginNode.getPropertyKeys())
				{
					paramMap.put(propKey, pluginNode.getProperty(propKey).toString());
					//System.out.println(propKey + " " + pluginNode.getProperty(propKey).toString());
				}
				tx.success();
			}
		}
		return paramMap;
	}
	
	public Map<String,String> getNodeParams(String region, String agent, String plugin)
	{
		Map<String,String> paramMap = new HashMap<String,String>();
		long nodeId = getNodeId(region,agent,plugin);
		if(nodeId != -1)
		{
			try ( Transaction tx = graphDb.beginTx() )
			{
				Node pluginNode = graphDb.getNodeById(nodeId);
				for(String propKey : pluginNode.getPropertyKeys())
				{
					paramMap.put(propKey, pluginNode.getProperty(propKey).toString());
					//System.out.println(propKey + " " + pluginNode.getProperty(propKey).toString());
				}
				tx.success();
			}
		}
		return paramMap;
	}
	
	public String getNodeParam(long nodeId, String param)
	{
		String nodeParam = null;
		if(nodeId != -1)
		{
			try ( Transaction tx = graphDb.beginTx() )
			{
				Node pluginNode = graphDb.getNodeById(nodeId);
				nodeParam = pluginNode.getProperty(param).toString();
				tx.success();
			}
		}
		return nodeParam;
	}
	
	public String getNodeParam(String region, String agent, String plugin, String param)
	{
		String nodeParam = null;
		long nodeId = getNodeId(region,agent,plugin);
		if(nodeId != -1)
		{
			try ( Transaction tx = graphDb.beginTx() )
			{
				Node pluginNode = graphDb.getNodeById(nodeId);
				nodeParam = pluginNode.getProperty(param).toString();
				tx.success();
			}
		}
		return nodeParam;
	}
	
	public long getEdgeId(long nodeSource, long nodeDest, RelType type)
	{
		long relId = -1;
		QueryResult<Map<String, Object>> result;
		
		try ( Transaction tx = graphDb.beginTx() )
		{
			//first remove agents
			String execStr = "start x  = node("+ nodeSource + "), n = node(" + nodeDest + ")";
					execStr += " match x-[r]->n";
					execStr += " where type(r) = \"" + type.toString() + "\"";
					//execStr += "return ID(r), TYPE(r)";
					execStr += " return r";
			result = engine.query( execStr,null );
			Iterator<Map<String, Object>> iterator=result.iterator(); 
			 if(iterator.hasNext()) { 
			   Map<String,Object> row= iterator.next(); 
			   //out.print("Total nodes: " + row.get("total"));
			   Relationship relationship  = (Relationship) row.get("r");
			   try{
				   return relationship.getId();
			   }
			   catch(Exception ex)
			   {
				   System.out.println("WTF! " + ex.toString());
			   }
			 }
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("removeNode : removing Region " + ex.toString());
		}
		
		return relId;
	}
	
	public static enum RelType implements RelationshipType
	{
	    isConnected,isPlugin,isAgent
	}
	
	public void removeNode(String region, String agent, String plugin)
	{
		if((region != null) && (agent == null) && (plugin == null)) //region node
		{
			QueryResult<Map<String, Object>> result;
			ArrayList<Node> nodes = new ArrayList<Node>();
			ArrayList<String> nodeNames = new ArrayList<String>();
			
			try ( Transaction tx = graphDb.beginTx() )
			{
				//first remove agents
				String execStr = "MATCH (r:Region {regionname: \"" + region + "\"})-[l]-(b:Agent) ";
				execStr += "RETURN b";
				result = engine.query( execStr,null );
				Iterator<Map<String, Object>> iterator=result.iterator(); 
				 if(iterator.hasNext()) { 
				   Map<String,Object> row= iterator.next(); 
				   //out.print("Total nodes: " + row.get("total"));
				   Node node  = (Node) row.get("b");
				   try{
				   nodeNames.add(node.getProperty("agentname").toString());
				   }
				   catch(Exception ex)
				   {
					   System.out.println("WTF! " + ex.toString());
				   }
				 }
			tx.success();
			}
			catch(Exception ex)
			{
				System.out.println("removeNode : removing Region " + ex.toString());
			}
				
				for(String nodeName : nodeNames)
				{
					removeNode(region,nodeName,null);
				}
		
				long regionNodeId = getNodeId(region,null,null);
				//nodes.add(graphDb.getNodeById(regionNodeId));
				deleteNodesAndRelationships(regionNodeId);
			
		}
		else if((region != null) && (agent != null) && (plugin == null)) //agent node
		{
			QueryResult<Map<String, Object>> result;
			//ArrayList<Node> nodes = new ArrayList<Node>();
			ArrayList<String> pluginNames = new ArrayList<String>();
			 			
			try ( Transaction tx = graphDb.beginTx() )
			{
				//first remove plugins
				String execStr = "MATCH (a:Agent {agentname: \"" + agent + "\"})-[r]-(b:Plugin) ";
				execStr += "RETURN b";
				result = engine.query( execStr,null );
				
				 Iterator<Map<String, Object>> iterator=result.iterator(); 
				 if(iterator.hasNext()) 
				 { 
					 for (Map<String,Object> row : result) 
					 {
						   Node x = (Node)row.get("b");
						   //for (String prop : x.getPropertyKeys()) {
						   //   System.out.println(prop +": "+x.getProperty(prop));
						   //}
						   pluginNames.add(x.getProperty("pluginname").toString());
						   
						}
					/* 
				   Map<String,Object> row= iterator.next(); 
				   Node node  = (Node) row.get("b");
				   pluginNames.add(node.getProperty("pluginname").toString());
				   System.out.println("remove plugin2 " + region + " " + agent + " " + " " + node.getProperty("pluginname").toString());
				   */				
				 }
				 tx.success();
			}	
			catch(Exception ex)
			{
				System.out.println("Woops: " + ex.toString());
			}
			for(String pluginName : pluginNames)
			{
				removeNode(region,agent,pluginName);
			}
			
			long agentNodeId = getNodeId(region,agent,null);
			deleteNodesAndRelationships(agentNodeId);
			
			//if no more nodes exist in region remove region
			ArrayList<String> nodeNames = new ArrayList<String>();
			
			try ( Transaction tx = graphDb.beginTx() )
			{
				//first remove agents
				String execStr = "MATCH (r:Region {regionname: \"" + region + "\"})-[l]-(b:Agent) ";
				execStr += "RETURN b";
				result = engine.query( execStr,null );
				Iterator<Map<String, Object>> iterator=result.iterator(); 
				 if(iterator.hasNext()) { 
				   Map<String,Object> row= iterator.next(); 
				   //out.print("Total nodes: " + row.get("total"));
				   Node node  = (Node) row.get("b");
				   try{
				   nodeNames.add(node.getProperty("agentname").toString());
				   }
				   catch(Exception ex)
				   {
					   System.out.println("WTF! " + ex.toString());
				   }
				 }
			tx.success();
			}
			catch(Exception ex)
			{
				System.out.println("removeNode : removing Region " + ex.toString());
			}
			if(nodeNames.isEmpty())
			{
				removeNode(region,null,null);
			}
			else
			{
				System.out.println(nodeNames.size());
			}
			
			
		}
		else if((region != null) && (agent != null) && (plugin != null)) //plugin node
		{
				//simply delete the plugin
				long pluginNodeId = getNodeId(region,agent,plugin);
				if(pluginNodeId != -1)
				{
					try ( Transaction tx = graphDb.beginTx() )
					{
						deleteNodesAndRelationships(pluginNodeId);
					}
					catch(Exception ex)
					{
						System.out.println("Problem Removing Plugin " + ex.toString());
					}
				}
				else
				{
					System.out.println("Can't remove Region=" + region + " Agent=" + agent + " Plugin=" + plugin + " " + " it does not exist");
				}
		}
		
	}
	
	
public void deleteNodesAndRelationships(long nodeId) {
		try ( Transaction tx = graphDb.beginTx() )
		{
		String query = "START n=node(" + nodeId + ") OPTIONAL MATCH n-[r]-() DELETE r, n;";
		QueryResult<Map<String, Object>> result = engine.query(query, null);
		tx.success();
		}
		catch(Exception ex)
		{
			System.out.println("Unable to delete nodes and relations ! " + ex.toString());
		}
	}
	


}
