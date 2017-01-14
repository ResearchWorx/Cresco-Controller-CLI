package core;


import app.gEdge;
import app.gNode;
import app.gPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.MsgEvent;
import shared.MsgEventType;

import java.util.*;
import java.util.Map.Entry;

public class GlobalTools {

    private static final Logger logger = LoggerFactory.getLogger(GlobalTools.class);

    public GlobalTools() {

    }

    public void runCmd(String[] args) {
        try {
            int cmd = Integer.parseInt(args[0]);
            System.out.println("CMD=" + cmd);
            if(args.length > 1) {
                args = Arrays.copyOfRange(args, 1, args.length);
                for(int i = 0; i < args.length; i++) {
                    System.out.println(i + "=" + args[i]);
                }
            }

            switch (cmd) {
                case 0:
                    pControllerPluginInventory();
                    break;
                case 1:
                    pControllerResourceInventory();
                    break;
                case 2:
                    gPipelineSubmit();
                default:
                    printCmd();
            }

        } catch (Exception ex) {
            printCmd();
            logger.error(ex.getMessage());
        }

    }

    public void printCmd() {
        System.out.println("0=[pControllerPluginInventory()]");
        System.out.println("1=[pControllerResourceInventory()]");
        System.out.println("2=[gPipelineSubmit()]");
    }

    public String getEnvStatus(String environment_id, String environment_value) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "getenvstatus");
        me.setParam("globalcmd", "getenvstatus");
        me.setParam("environment_id", environment_id);
        me.setParam("environment_value", environment_value);
        me = CLI.cc.sendMsgEventReturn(me);

        if (me.getParam("count") != null) {
            System.out.println("environment_id=" + me.getParam("count"));
            return me.getParam("count");
        }
        return null;
    }

    public String addPlugin(String resource_id, String inode_id, String configParams) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "add application node");
        //me.setParam("src_region", "external");
        //me.setParam("src_agent", "external");
        //me.setParam("dst_region", "external");
        //me.setParam("dst_agent", "external");
        me.setParam("globalcmd", "addplugin");
        me.setParam("inode_id", inode_id);
        me.setParam("resource_id", resource_id);
        me.setParam("configparams", configParams);
        //return CLI.cc.sendMsgEvent(me);
        me = CLI.cc.sendMsgEventReturn(me);
        //me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");

        if (me.getParam("status_code") != null) {
            System.out.println("status_desc:" + me.getParam("status_desc"));
            return me.getParam("status_code");
        }
        return null;
    }

    public String removePlugin(String resource_id, String inode_id) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "remove application node");
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

        if (me.getParam("status_code") != null) {
            System.out.println("status_desc:" + me.getParam("status_desc"));

            return me.getParam("status_code");
        }
        return null;
    }

    public String getPluginStatus(String resource_id, String inode_id) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "add application node");
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

        if (me.getParam("status_code") != null) {
            System.out.println("status_desc:" + me.getParam("status_desc"));
            return me.getParam("status_code");
        }
        return null;
    }

    public void updatePlugins(boolean force) {
        String pluginName = "cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
        String downloadUrl = "http://128.163.188.129:9998/job/Cresco-Agent-Dummy-Plugin/lastSuccessfulBuild/com.researchworx.cresco$cresco-agent-dummy-plugin/artifact/com.researchworx.cresco/cresco-agent-dummy-plugin/0.5.0-SNAPSHOT/";
        controllerPluginDownload(pluginName, downloadUrl, force);

        downloadUrl = "http://128.163.188.129:9998/job/Cresco-Agent-AMPQChannel-Plugin/lastSuccessfulBuild/com.researchworx.cresco$cresco-agent-ampqchannel-plugin/artifact/com.researchworx.cresco/cresco-agent-ampqchannel-plugin/0.5.0-SNAPSHOT/";
        pluginName = "cresco-agent-ampqchannel-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";

        controllerPluginDownload(pluginName, downloadUrl, force);


        List<String> inventory = getControllerPluginInventory();
        System.out.println("inventory list = " + inventory.size());
        for (String str : inventory) {
            System.out.println("inventory item = " + str);
        }


    }

    public boolean controllerPluginDownload(String plugin, String pluginurl, boolean forceDownload) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "download plugin");
        //me.setParam("src_region", "external");
        //me.setParam("src_agent", "external");
        //me.setParam("dst_region", "external");
        //me.setParam("dst_agent", "external");
        me.setParam("globalcmd", "plugindownload");
        me.setParam("plugin", plugin);
        me.setParam("pluginurl", pluginurl);
        if (forceDownload) {
            me.setParam("forceplugindownload", "true");
        }
        //return CLI.cc.sendMsgEvent(me);
        me = CLI.cc.sendMsgEventReturn(me);
        if (me.getParam("hasplugin") != null) {
            if (me.getParam("hasplugin").equals(plugin)) {
                return true;
            }
        }
        return false;
    }

    public void pControllerPluginInventory() {
        List<String> inventory = getControllerPluginInventory();
        System.out.println("inventory list = " + inventory.size());
        for (String str : inventory) {
            System.out.println("inventory item = " + str);
        }

    }

    public List<String> getControllerPluginInventory() {
        List<String> inventory = new ArrayList<String>();
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get plugin inventory");
        //me.setParam("src_region", "external");
        //me.setParam("src_agent", "external");
        //me.setParam("dst_region", "external");
        //me.setParam("dst_agent", "external");
        me.setParam("globalcmd", "plugininventory");
        //return CLI.cc.sendMsgEvent(me);
        me = CLI.cc.sendMsgEventReturn(me);
        System.out.println(me.getParams().toString());
        if (me.getParam("pluginlist") != null) {
            String[] pluginList = me.getParam("pluginlist").split(",");
            for (String str : pluginList) {
                inventory.add(str);
            }
        }
        return inventory;
    }

//
    public void pControllerResourceInventory() {
        List<String> inventory = getControllerResourceInventory();
        System.out.println("inventory list = " + inventory.size());
        for (String str : inventory) {
            System.out.println("inventory item = " + str);
        }

    }

    public void gPipelineSubmit() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", "gpipelinesubmit");

        Gson gson = new GsonBuilder().create();


        //public gNode(String type, String node_name, String node_id,Map<String, String> params)
        gNode n0 = new gNode("dummy", "node0", "0", new HashMap<String,String>());
        gNode n1 = new gNode("dummy", "node0", "1", new HashMap<String,String>());

        List<gNode> gNodes = new ArrayList<>();
        gNodes.add(n0);
        gNodes.add(n1);

        gEdge e0 = new gEdge("0","0","1");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";
        me.setParam("gpipeline",gson.toJson(gpay));
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());
        System.out.println("SUBMITTED");
    }


    public List<String> getControllerResourceInventory() {
        List<String> inventory = new ArrayList<String>();
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        //me.setParam("src_region", "external");
        //me.setParam("src_agent", "external");
        //me.setParam("dst_region", "external");
        //me.setParam("dst_agent", "external");
        me.setParam("globalcmd", "resourceinventory");
        //return CLI.cc.sendMsgEvent(me);

        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

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


    public void getPluginInfo(String plugin_id) {
        List<String> inventory = new ArrayList<String>();
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get plugin info");
        me.setParam("globalcmd", "plugininfo");
        me.setParam("plugin_id", plugin_id);

        me = CLI.cc.sendMsgEventReturn(me);

        if (me.getParam("node_name") != null) {
            System.out.println(me.getParam("node_name"));
            System.out.println("queue");
            System.out.println(me.getParam("node_id"));
            System.out.println(me.getParam("params"));
        }

    }


    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
        List<Entry<K, V>> entries = new LinkedList<Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Entry<K, V>>() {

            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());

            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


}
