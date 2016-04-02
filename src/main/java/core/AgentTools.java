package core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.MsgEvent;
import shared.MsgEventType;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;


import java.util.Random;


public class AgentTools {

    private static final Logger logger = LoggerFactory.getLogger(AgentTools.class);

    public AgentTools() {

    }

    public void runCmd(String[] args) {
        try {
            logger.info("Running AgentTools Command: " + args[0]);
            //    public boolean addPlugin(String region, String agent, String configparams) {
            if(args[0].toLowerCase().equals("addplugin")) {
                String region = args[1];
                String agent = args[2];
                String configparams = args[3];
                logger.info("Adding Plugin: Region=" + region + " Agent=" + agent + " ConfigParams=" + configparams);
                if(addPlugin(region,agent,configparams)) {
                    logger.info("Success");
                }
                else {
                    logger.error("Failed!");
                }

                //addPlugin()
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }

    public void createApp() {
        System.out.println("Creating demo_app");

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        for (int x = 0; x < 2; x = x + 1) {

            for (int xx = 0; xx < 2; xx = xx + 1) {

                for (int xxx = 0; xxx < 4; xxx = xxx + 1) {
                    int randomNum = rand.nextInt((10 - 1) + 1) + 1;
                    System.out.println("Creating Plugin for region" + x + " agent" + xx + " config=" + randomNum);
                    addPlugin("region" + x, "agent" + xx, String.valueOf(randomNum));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
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

    public boolean addInPlugin(String region, String agent, String server, String delay, String rate) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "add plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "pluginadd");
        String configParams = "pluginname=MD5Plugin,jarfile=/opt/Cresco/plugins/cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,ampq_control_host=" + server + ",ampq_control_username=cresco,ampq_control_password=u$cresco01,watchdogtimer=5000,perfapp=MD5in,dataqueue=md5data,dataqueuedelay=" + delay + ",md5producerrate=" + rate + ",enablemd5consumer=0,enablemd5producer=1";
        me.setParam("configparams", configParams);
        //me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=/opt/Cresco/plugins/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
        return CLI.cc.sendMsgEvent(me);
    }

    public boolean addOutPlugin(String region, String agent, String server, String delay, String rate) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "add plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "pluginadd");
        String configParams = "pluginname=MD5Plugin,jarfile=/opt/Cresco/plugins/cresco-agent-MD5processor-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,ampq_control_host=" + server + ",ampq_control_username=cresco,ampq_control_password=u$cresco01,watchdogtimer=5000,dataqueuedelay=" + delay + ",perfapp=MD5out,dataqueue=md5data,dataqueuedelay=1000,md5producerrate=" + rate + ",enablemd5consumer=1,enablemd5producer=0";
        me.setParam("configparams", configParams);
        //me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=/opt/Cresco/plugins/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
        return CLI.cc.sendMsgEvent(me);
    }

    //perfapp=MD5out,

    public boolean addPlugin(String region, String agent, String configparams) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "add plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "pluginadd");
        //me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");
        me.setParam("configparams", configparams);
        return CLI.cc.sendMsgEvent(me);
    }

    public boolean addPlugin2(String region, String agent, String perflevel) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "add plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "pluginadd");
        me.setParam("configparams", "perflevel=" + perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region + ",watchdogtimer=5000");
        return CLI.cc.sendMsgEvent(me);
    }

    public boolean downloadPlugin(String region, String agent, String plugin, String pluginurl, boolean forceDownload) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "download plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "plugindownload");
        me.setParam("plugin", plugin);
        me.setParam("pluginurl", pluginurl);
        //me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=..//Cresco-Agent-Dummy-Plugin/target/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=test2,watchdogtimer=5000");
        if (forceDownload) {
            me.setParam("forceplugindownload", "true");
        }
        return CLI.cc.sendMsgEvent(me);
    }

    public boolean getPluginInventory(String region, String agent) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "get plugin inventory");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "plugininventory");
        return CLI.cc.sendMsgEvent(me);
    }

    public boolean removePlugin(String region, String agent, String plugin) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, region, null, null, "remove plugin");
        me.setParam("src_region", region);
        me.setParam("src_agent", "external");
        me.setParam("dst_region", region);
        me.setParam("dst_agent", agent);
        me.setParam("controllercmd", "regioncmd");
        me.setParam("configtype", "pluginremove");
        me.setParam("plugin", plugin);
        return CLI.cc.sendMsgEvent(me);
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
