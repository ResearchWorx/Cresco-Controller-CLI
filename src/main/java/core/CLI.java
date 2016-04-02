package core;


import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import graphdb.GraphDBEngine;
import channels.ControllerChannel;
import shared.MsgEvent;
import shared.MsgEventType;


public class CLI {


	public static Config config;
	//public static String controllerip = "10.33.4.63";
	//public static String graphDBip = "10.33.4.63";
	public static int controllerport = 32000;
	public static GraphDBEngine gdb;
	public static ControllerChannel cc;

	public static void main(String[] args) throws Exception 
	{

        String configFile = checkConfig(args);

        //Make sure config file
        config = new Config(configFile);


        gdb = new GraphDBEngine(config.getParam("gdb_host")); //create graphdb connector
		
		cc = new ControllerChannel();
		//String pluginName = "cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
		//downloadPlugin("region0","agent-hzkl",plugin,"http://10.33.4.63");
		//addPlugin("region0","agent-kmqo","1");
		//removePlugin("region0","agent-clzk","plugin/4");
		//gdb.deleteNodesAndRelationships(1162);
		
		
		//addPlugin("region0","agent-clzk","1");
		
	//String region = "region0";
		ArrayList<Long> regionList = gdb.getRegions();
		System.out.println("Region Count: " + regionList.size());
		for(long regionId : regionList)
		{
			String region = gdb.getNodeParam(regionId, "regionname");
			System.out.println("Region: " + region);
			
			ArrayList<Long> agentList = gdb.getAgentsInRegion(regionId);
			System.out.println("Region " + region + " Agent Count: " + agentList.size()); 
			for(long agentId : agentList)
			{
				String agent = gdb.getNodeParam(agentId, "agentname");
				//System.out.println("\tAgent: " + agent);
				if(region.equals("region0"))
				{   
					//10.33.4.56
					//10.33.4.58
					//10.33.4.60
					//10.33.4.62
					
					/*
				    addInPlugin(region,agent,"10.33.8.4", "1", "100");
					addInPlugin(region,agent,"10.33.4.58", "1", "100");
					addInPlugin(region,agent,"10.33.4.60", "1", "100");
					addInPlugin(region,agent,"10.33.4.62", "1", "100");
					addOutPlugin(region,agent,"10.33.8.4", "1", "100");
					addOutPlugin(region,agent,"10.33.4.58", "1", "100");
					addOutPlugin(region,agent,"10.33.4.60", "1", "100");
					addOutPlugin(region,agent,"10.33.4.62", "1", "100");
					*/
					
					//addOutPlugin(region,agent,"10.33.8.4");
					//System.out.println("add region0 plugin");
					//addPlugin(region,agent,"1");
				}
				
				//String pluginName = "cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
				/*
				String pluginName = "cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
				System.out.println("\tDownloading: " + pluginName);
				downloadPlugin(region,agent,pluginName,"http://10.33.4.63");
				*/
				//addPlugin(region,agent,"1");
				//removePlugin("region0","agent-kmqo","plugin/4");
				
				
				ArrayList<Long> pluginList = gdb.getPluginInAgent(agentId);
				System.out.println("Region " + region + " Agent " + agent + " Plugin Count: " + pluginList.size()); 
				
				for(long pluginId : pluginList)
				{
					try
					{
						String plugin = gdb.getNodeParam(pluginId, "pluginname");
						String configparams = gdb.getNodeParam(pluginId, "configparams");
						
						String propertiesFormat = configparams.replaceAll(",", "\n");
				        Properties properties = new Properties();
				        properties.load(new StringReader(propertiesFormat));
				        
				        //cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar
				        //if(properties.getProperty("jarfile").contains("cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar"))
				        if(properties.getProperty("jarfile").contains("cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar"))
					    {	
				        	System.out.println("removing plugin");
				        	System.out.println(properties);
				        	System.out.println(plugin);
				        	//removePlugin(region,agent,plugin);
						}
					}
					catch(Exception ex)
					{
						System.out.println(ex);
						String plugin = gdb.getNodeParam(pluginId, "pluginname");
						//removePlugin(region,agent,plugin);
					}
					
					//System.out.println("\t\tPlugin: " + plugin);
					
					/*
					String propertiesFormat = configparams.replaceAll(",", "\n");
			        Properties properties = new Properties();
			        properties.load(new StringReader(propertiesFormat));
			        
			        if(properties.getProperty("jarfile").contains("cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar"))
			        {
			        	//System.out.println(properties);
			        	//System.out.println(plugin);
			        	//removePlugin(region,agent,plugin);
					}
			        */
					
					//removePlugin(region,agent,plugin);
					
					
					//System.out.println(pluginId);
					/*
					Map<String,String> pluginParams = gdb.getNodeParams(pluginId);
					Iterator it = pluginParams.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pairs = (Map.Entry)it.next();
				        System.out.println(pairs.getKey() + " = " + pairs.getValue());
				        it.remove(); // avoids a ConcurrentModificationException
				    }
				    */
				}
				
			}
		}
	
		//createApp();
		//improveApp("demo_app");
		//cleanApp("demo_app");
		//forceCleanDB("demo_app");
		
		
		
	}
	public static void createApp()
	{
		System.out.println("Creating demo_app");
	
		Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    for(int x = 0; x < 2; x = x+1) 
	    {
	    	
	    	for(int xx = 0; xx < 2; xx = xx+1) 
		    {
	    		
	    		for(int xxx = 0; xxx < 4; xxx = xxx+1) 
			    {
	    			int randomNum = rand.nextInt((10 - 1) + 1) + 1;
	    		    System.out.println("Creating Plugin for region" + x + " agent" + xx + " config=" + randomNum);
	    			addPlugin("region" + x,"agent" + xx,String.valueOf(randomNum));
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
		    
		    }
		    
	         
	    }
		/*
		addPlugin("region0","agent0","2");
		addPlugin("region0","agent0","1");
		addPlugin("region0","agent0","5");
		
		addPlugin("region0","agent1","1");
		addPlugin("region0","agent1","10");
		addPlugin("region0","agent1","5");
		
		addPlugin("region1","agent0","5");
		addPlugin("region1","agent0","1");
		addPlugin("region1","agent0","5");
		
		addPlugin("region1","agent1","0");
		addPlugin("region1","agent1","1");
		addPlugin("region1","agent1","2");
		*/
	}
	public static void improveApp(String application) throws InterruptedException
	{
		long totalPerformance = 0;
		long appNodeId = gdb.getAppNodeId(application);
		if(appNodeId == -1)
		{
			System.out.println("No application to improve");
			System.exit(0);
		}
		Map<Long,Long> pluginPerf = gdb.getPerfMetrics(appNodeId);
		Map<Long,Long> agentPluginMap = new HashMap<Long,Long>(); 
		Map<Long,Long> regionAgentMap = new HashMap<Long,Long>();
	
		ArrayList<Long> regionIdList = new ArrayList<Long>();  
		ArrayList<Long> agentIdList = new ArrayList<Long>();  
		
		if(pluginPerf == null)
		{
			System.out.println("No plugins to improve");
			System.exit(0);
		}
		System.out.println("Collected Performance Metrics from: " + pluginPerf.size() + " plugins");
		
		
		for (Entry<Long, Long> entry : pluginPerf.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ metric: " + entry.getValue());
			//System.out.println(gdb.getAgentFromPlugin(entry.getKey()));
			long agentId = gdb.getAgentFromPlugin(entry.getKey());
			if(!agentIdList.contains(agentId))
			{
				agentIdList.add(agentId);
			}
			agentPluginMap.put(entry.getKey(), agentId);
		}
		System.out.println("Related " + pluginPerf.size() + " plugins to " + agentIdList.size() + " Agents");
		for (Entry<Long, Long> entry : agentPluginMap.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ agent: " + entry.getValue());
			if(!regionAgentMap.containsKey(entry.getValue()))
			{
				long regionId = gdb.getRegionFromAgent(entry.getValue());
				if(!regionIdList.contains(regionId))
				{
					regionIdList.add(regionId);
				}
				regionAgentMap.put(entry.getValue(), regionId);
			}
		}
		System.out.println("Related " + agentIdList.size()  + " agents to " + regionIdList.size() + " Regions");
		System.out.println(" ");
		for(long regionId : regionIdList)
		{
			String region = gdb.getPropertyByNodeId("regionname",regionId);
			System.out.println("Region: " + region);
			for (Entry<Long, Long> entry : regionAgentMap.entrySet())
			{
				long agentId = entry.getKey();
				String agent = gdb.getPropertyByNodeId("agentname",agentId);
				if(entry.getValue() == regionId)
				{
					System.out.println("\t\tAgent: " + agent);
					//int pluginCount = 0;
					Map<Long,Long> perfSortedPlugin =  new TreeMap<Long,Long>();
					
					for (Entry<Long, Long> pluginentry : agentPluginMap.entrySet())
					{
						long pluginId = pluginentry.getKey();
						if(pluginentry.getValue() == agentId)
						{
							//build plugin list
							perfSortedPlugin.put(pluginId, pluginPerf.get(pluginId));
							//System.out.println("\t\t\t\tPlugin: " + gdb.getPropertyByNodeId("pluginname",pluginId) + " Perf: " + pluginPerf.get(pluginId));
						}
					}
					perfSortedPlugin = sortByValues(perfSortedPlugin); //sort based on performance
					boolean isFirst = true;
					for (Entry<Long, Long> sortedentry : perfSortedPlugin.entrySet())
					{
						long pluginId = sortedentry.getKey();
						String plugin =  gdb.getPropertyByNodeId("pluginname",pluginId);
						totalPerformance += pluginPerf.get(pluginId);
						System.out.println("\t\t\t\tPlugin: " + plugin + " [Config=" + gdb.getPluginParamsByNodeId("perflevel", pluginId) + "] [Perf=" + pluginPerf.get(pluginId) + "]");
						//System.out.println( gdb.getPropertyByRelationId("perflevel", pluginId, appNodeId, RelType.isConnected));
						
					}
					int pluginBudget = 5;
					if(perfSortedPlugin.size() < pluginBudget)
					{
						long topPlugin = (long) perfSortedPlugin.keySet().toArray()[0];
						String perflevel = gdb.getPluginParamsByNodeId("perflevel", topPlugin);
						String plugin = gdb.getPropertyByNodeId("pluginname",topPlugin);
						//System.out.println("Top = " + perfSortedPlugin.keySet().toArray()[0] + " " + gdb.getPropertyByNodeId("pluginname",tmp));	
						System.out.println("\t\t+Adding new Plugin with Config=" + perflevel + "\n");
						addPlugin(region, agent, perflevel);
						Thread.sleep(1000); //sleep on add
						System.out.println(" ");
						
						
					}
					else //budget it max.. must remove
					{
						long bottomPlugin = (long) perfSortedPlugin.keySet().toArray()[perfSortedPlugin.keySet().size()-1];
						String perflevel = gdb.getPluginParamsByNodeId("perflevel", bottomPlugin);
						String plugin = gdb.getPropertyByNodeId("pluginname",bottomPlugin);
						System.out.println("\t\t*No more plugin budget: Maximum=" + pluginBudget);
						System.out.println("\t\t-Removing old Plugin: " + plugin + " with Config=" + perflevel);
						removePlugin(region,agent,plugin);
						Thread.sleep(1000); //sleep on remove
						long topPlugin = (long) perfSortedPlugin.keySet().toArray()[0];
						perflevel = gdb.getPluginParamsByNodeId("perflevel", topPlugin);
						plugin = gdb.getPropertyByNodeId("pluginname",topPlugin);
						//System.out.println("Top = " + perfSortedPlugin.keySet().toArray()[0] + " " + gdb.getPropertyByNodeId("pluginname",tmp));	
						System.out.println("\t\t+Adding new Plugin with Config=" + perflevel + "\n");
						addPlugin(region, agent, perflevel);
						Thread.sleep(1000); //sleep on add
						System.out.println(" ");
						
					}
				}
			}
			
		}
		System.out.println("Total Performance=" + totalPerformance);
	}
	public static void cleanApp(String application)
	{
		long appNodeId = gdb.getAppNodeId(application);
		if(appNodeId == -1)
		{
			System.out.println("No application to clean");
			System.exit(0);
		}
		Map<Long,Long> pluginPerf = gdb.getPerfMetrics(appNodeId);
		Map<Long,Long> agentPluginMap = new HashMap<Long,Long>(); 
		Map<Long,Long> regionAgentMap = new HashMap<Long,Long>();
	
		ArrayList<Long> regionIdList = new ArrayList<Long>();  
		ArrayList<Long> agentIdList = new ArrayList<Long>();  
		
		if(pluginPerf == null)
		{
			System.out.println("No plugins to improve");
			System.exit(0);
		}
		System.out.println("Collected Performance Metrics from: " + pluginPerf.size() + " plugins");
		
		
		for (Entry<Long, Long> entry : pluginPerf.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ metric: " + entry.getValue());
			//System.out.println(gdb.getAgentFromPlugin(entry.getKey()));
			long agentId = gdb.getAgentFromPlugin(entry.getKey());
			if(!agentIdList.contains(agentId))
			{
				agentIdList.add(agentId);
			}
			agentPluginMap.put(entry.getKey(), agentId);
		}
		System.out.println("Related " + pluginPerf.size() + " plugins to " + agentIdList.size() + " Agents");
		for (Entry<Long, Long> entry : agentPluginMap.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ agent: " + entry.getValue());
			if(!regionAgentMap.containsKey(entry.getValue()))
			{
				long regionId = gdb.getRegionFromAgent(entry.getValue());
				if(!regionIdList.contains(regionId))
				{
					regionIdList.add(regionId);
				}
				regionAgentMap.put(entry.getValue(), regionId);
			}
		}
		System.out.println("Related " + agentIdList.size()  + " agents to " + regionIdList.size() + " Regions");
		System.out.println(" ");
		for(long regionId : regionIdList)
		{
			String region = gdb.getPropertyByNodeId("regionname",regionId);
			System.out.println("Region: " + region);
			for (Entry<Long, Long> entry : regionAgentMap.entrySet())
			{
				long agentId = entry.getKey();
				String agent = gdb.getPropertyByNodeId("agentname",agentId);
				if(entry.getValue() == regionId)
				{
					System.out.println("\t\tAgent: " + agent);
					//int pluginCount = 0;
					Map<Long,Long> perfSortedPlugin =  new TreeMap<Long,Long>();
					
					for (Entry<Long, Long> pluginentry : agentPluginMap.entrySet())
					{
						long pluginId = pluginentry.getKey();
						if(pluginentry.getValue() == agentId)
						{
							String plugin = gdb.getPropertyByNodeId("pluginname",pluginId);
							System.out.println("Removing Plugin: " + plugin);
							removePlugin(region, agent, plugin);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
					}
					
				}
		}
			
		}
	}
	public static void forceCleanDB(String application)
	{
		long appNodeId = gdb.getAppNodeId(application);
		if(appNodeId == -1)
		{
			System.out.println("No application to cleanDb");
			System.exit(0);
		}
		Map<Long,Long> pluginPerf = gdb.getPerfMetrics(appNodeId);
		Map<Long,Long> agentPluginMap = new HashMap<Long,Long>(); 
		Map<Long,Long> regionAgentMap = new HashMap<Long,Long>();
	
		ArrayList<Long> regionIdList = new ArrayList<Long>();  
		ArrayList<Long> agentIdList = new ArrayList<Long>();  
		
		if(pluginPerf == null)
		{
			System.out.println("No plugins to improve");
			System.exit(0);
		}
		System.out.println("Collected Performance Metrics from: " + pluginPerf.size() + " plugins");
		
		
		for (Entry<Long, Long> entry : pluginPerf.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ metric: " + entry.getValue());
			//System.out.println(gdb.getAgentFromPlugin(entry.getKey()));
			long agentId = gdb.getAgentFromPlugin(entry.getKey());
			if(!agentIdList.contains(agentId))
			{
				agentIdList.add(agentId);
			}
			agentPluginMap.put(entry.getKey(), agentId);
		}
		System.out.println("Related " + pluginPerf.size() + " plugins to " + agentIdList.size() + " Agents");
		for (Entry<Long, Long> entry : agentPluginMap.entrySet())
		{
			//System.out.println("plugin: " + entry.getKey() + "/ agent: " + entry.getValue());
			if(!regionAgentMap.containsKey(entry.getValue()))
			{
				long regionId = gdb.getRegionFromAgent(entry.getValue());
				if(!regionIdList.contains(regionId))
				{
					regionIdList.add(regionId);
				}
				regionAgentMap.put(entry.getValue(), regionId);
			}
		}
		System.out.println("Related " + agentIdList.size()  + " agents to " + regionIdList.size() + " Regions");
		System.out.println(" ");
		for(long regionId : regionIdList)
		{
			String region = gdb.getPropertyByNodeId("regionname",regionId);
			System.out.println("Region: " + region);
			for (Entry<Long, Long> entry : regionAgentMap.entrySet())
			{
				long agentId = entry.getKey();
				String agent = gdb.getPropertyByNodeId("agentname",agentId);
				if(entry.getValue() == regionId)
				{
					System.out.println("\t\tAgent: " + agent);
					//int pluginCount = 0;
					Map<Long,Long> perfSortedPlugin =  new TreeMap<Long,Long>();
					
					for (Entry<Long, Long> pluginentry : agentPluginMap.entrySet())
					{
						long pluginId = pluginentry.getKey();
						if(pluginentry.getValue() == agentId)
						{
							String plugin = gdb.getPropertyByNodeId("pluginname",pluginId);
							System.out.println("Removing Plugin: " + plugin);
							//removePlugin(region, agent, plugin);
							gdb.removeNode(region, agent, plugin);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
					}
					
				}
		}
			
		}
	}
	

	public static void addInPlugin(String region, String agent, String server, String delay, String rate)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,region,null,null,"add plugin");
		me.setParam("src_region", region);
		me.setParam("src_agent", "external");
		me.setParam("dst_region", region);
		me.setParam("dst_agent", agent);
		me.setParam("controllercmd", "regioncmd");
		me.setParam("configtype", "pluginadd");
		String configParams = "pluginname=MD5Plugin,jarfile=/opt/Cresco/plugins/cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,ampq_control_host=" + server + ",ampq_control_username=cresco,ampq_control_password=u$cresco01,watchdogtimer=5000,perfapp=MD5in,dataqueue=md5data,dataqueuedelay=" + delay +",md5producerrate=" + rate + ",enablemd5consumer=0,enablemd5producer=1";
		me.setParam("configparams",configParams);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=/opt/Cresco/plugins/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		cc.sendMsgEvent(me);
	}
	public static void addOutPlugin(String region, String agent, String server, String delay, String rate)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,region,null,null,"add plugin");
		me.setParam("src_region", region);
		me.setParam("src_agent", "external");
		me.setParam("dst_region", region);
		me.setParam("dst_agent", agent);
		me.setParam("controllercmd", "regioncmd");
		me.setParam("configtype", "pluginadd");
		String configParams = "pluginname=MD5Plugin,jarfile=/opt/Cresco/plugins/cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,ampq_control_host=" + server + ",ampq_control_username=cresco,ampq_control_password=u$cresco01,watchdogtimer=5000,dataqueuedelay=" + delay + ",perfapp=MD5out,dataqueue=md5data,dataqueuedelay=1000,md5producerrate=" + rate + ",enablemd5consumer=1,enablemd5producer=0";
		me.setParam("configparams",configParams);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=/opt/Cresco/plugins/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		cc.sendMsgEvent(me);
	}
	
	
	public static void addPlugin(String region, String agent, String perflevel)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,region,null,null,"add plugin");
		me.setParam("src_region", region);
		me.setParam("src_agent", "external");
		me.setParam("dst_region", region);
		me.setParam("dst_agent", agent);
		me.setParam("controllercmd", "regioncmd");
		me.setParam("configtype", "pluginadd");
		me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=/opt/Cresco/plugins/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		cc.sendMsgEvent(me);
	}
	
	public static void downloadPlugin(String region, String agent, String plugin, String pluginurl)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,region,null,null,"download plugin");
		me.setParam("src_region", region);
		me.setParam("src_agent", "external");
		me.setParam("dst_region", region);
		me.setParam("dst_agent", agent);
		me.setParam("controllercmd", "regioncmd");
		me.setParam("configtype", "plugindownload");
		me.setParam("plugin", plugin);
		me.setParam("pluginurl", pluginurl);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=..//Cresco-Agent-Dummy-Plugin/target/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=test2,watchdogtimer=5000");
		cc.sendMsgEvent(me);
	}
	
	public static void removePlugin(String region, String agent, String plugin)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,region,null,null,"remove plugin");
		me.setParam("src_region", region);
		me.setParam("src_agent", "external");
		me.setParam("dst_region", region);
		me.setParam("dst_agent", agent);
		me.setParam("controllercmd", "regioncmd");
		me.setParam("configtype", "pluginremove");
		me.setParam("plugin", plugin);
		cc.sendMsgEvent(me);
		
	}
	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {
 
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
                
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }

    public static String checkConfig(String[] args)
    {
        String errorMgs = "Cresco-Controller\n" +
                "Usage: java -jar Cresco-Controller.jar" +
                " -f <configuration_file>\n";

        if (args.length != 2)
        {
            System.err.println(errorMgs);
            System.err.println("ERROR: Invalid number of arguements.");
            System.exit(1);
        }
        else if(!args[0].equals("-f"))
        {
            System.err.println(errorMgs);
            System.err.println("ERROR: Must specify configuration file.");
            System.exit(1);
        }
        else
        {
            File f = new File(args[1]);
            if(!f.exists())
            {
                System.err.println("The specified configuration file: " + args[1] + " is invalid");
                System.exit(1);
            }
        }
        return args[1];
    }



}
