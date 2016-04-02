package core;


import shared.MsgEvent;
import shared.MsgEventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClientFunctions {

	
	public ClientFunctions() 
	{
		
	}
	
	public String getEnvStatus(String environment_id, String environment_value)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"getenvstatus");
		me.setParam("globalcmd", "getenvstatus");
		me.setParam("environment_id", environment_id);
		me.setParam("environment_value", environment_value);
		me = CLI.cc.sendMsgEventReturn(me);
		
		if(me.getParam("count") != null)
		{	
			System.out.println("environment_id=" + me.getParam("count"));
			return me.getParam("count");
		}
		return null;
	}
	
	public String addPlugin(String resource_id, String inode_id, String configParams)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"add application node");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "addplugin");
		me.setParam("inode_id", inode_id);
		me.setParam("resource_id", resource_id);
		me.setParam("configparams",configParams);
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		
		if(me.getParam("status_code") != null)
		{	
			System.out.println("status_desc:" + me.getParam("status_desc"));
			return me.getParam("status_code");
		}
		return null;
	}
	
	public String removePlugin(String resource_id, String inode_id)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"remove application node");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "removeplugin");
		me.setParam("inode_id", inode_id);
		me.setParam("resource_id", resource_id);
		
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		
		if(me.getParam("status_code") != null)
		{
			System.out.println("status_desc:" + me.getParam("status_desc"));
			
			return me.getParam("status_code");
		}
		return null;
	}
	
	public String getPluginStatus(String resource_id, String inode_id)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"add application node");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "getpluginstatus");
		me.setParam("inode_id", inode_id);
		me.setParam("resource_id", resource_id);
		
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
		System.out.println(me.getMsgBody());
		
		if(me.getParam("status_code") != null)
		{
			System.out.println("status_desc:" + me.getParam("status_desc"));
			return me.getParam("status_code");
		}
		return null;
	}
	
	public void updatePlugins(boolean force)
	{
		String pluginName = "cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
		String downloadUrl = "http://128.163.188.129:9998/job/Cresco-Agent-Dummy-Plugin/lastSuccessfulBuild/com.researchworx.cresco$cresco-agent-dummy-plugin/artifact/com.researchworx.cresco/cresco-agent-dummy-plugin/0.5.0-SNAPSHOT/";
		controllerPluginDownload(pluginName, downloadUrl, force);
		
		downloadUrl = "http://128.163.188.129:9998/job/Cresco-Agent-AMPQChannel-Plugin/lastSuccessfulBuild/com.researchworx.cresco$cresco-agent-ampqchannel-plugin/artifact/com.researchworx.cresco/cresco-agent-ampqchannel-plugin/0.5.0-SNAPSHOT/";
		pluginName = "cresco-agent-ampqchannel-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
		
		controllerPluginDownload(pluginName, downloadUrl, force);
				
		
		List<String> inventory = getControllerPluginInventory();
		System.out.println("inventory list = " + inventory.size());
		for(String str : inventory)
		{
			System.out.println("inventory item = " + str);
		}
		
		
	}
	
	public boolean controllerPluginDownload(String plugin, String pluginurl, boolean forceDownload)
	{
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"download plugin");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "plugindownload");
		me.setParam("plugin", plugin);
		me.setParam("pluginurl", pluginurl);
		if(forceDownload)
		{
			me.setParam("forceplugindownload", "true");
		}
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		if(me.getParam("hasplugin") != null)
		{
			if(me.getParam("hasplugin").equals(plugin))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<String> getControllerPluginInventory()
	{
		List<String> inventory = new ArrayList<String>();
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"get plugin inventory");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "plugininventory");
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		if(me.getParam("pluginlist") != null)
		{
			String[] pluginList = me.getParam("pluginlist").split(",");
			for(String str : pluginList)
			{
				inventory.add(str);
			}
		}
		return inventory;
	}
	
	public List<String> getControllerResourceInventory()
	{
		List<String> inventory = new ArrayList<String>();
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"get resourceinventory inventory");
		//me.setParam("src_region", "external");
		//me.setParam("src_agent", "external");
		//me.setParam("dst_region", "external");
		//me.setParam("dst_agent", "external");
		me.setParam("globalcmd", "resourceinventory");
		//return CLI.cc.sendMsgEvent(me);
		me = CLI.cc.sendMsgEventReturn(me);
		/*
		if(me.getParam("pluginlist") != null)
		{
			String[] pluginList = me.getParam("pluginlist").split(",");
			for(String str : pluginList)
			{
				inventory.add(str);
			}
		}
		*/
		return inventory;
	}
	
	
	public void getPluginInfo(String plugin_id)
	{
		List<String> inventory = new ArrayList<String>();
		MsgEvent me = new MsgEvent(MsgEventType.CONFIG,null,null,null,"get plugin info");
		me.setParam("globalcmd", "plugininfo");
		me.setParam("plugin_id", plugin_id);
		
		me = CLI.cc.sendMsgEventReturn(me);
		
		if(me.getParam("node_name") != null)
		{
			System.out.println(me.getParam("node_name"));
			System.out.println("queue");
			System.out.println(me.getParam("node_id"));
			System.out.println(me.getParam("params"));
		}
		
	}
	
	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Entry<K,V>> entries = new LinkedList<Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Entry<K,V>>() {
 
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
                
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
	
		
}
