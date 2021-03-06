package core;


import app.gEdge;
import app.gNode;
import app.gPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.MsgEvent;
import shared.MsgEventType;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GlobalTools {

    private static final Logger logger = LoggerFactory.getLogger(GlobalTools.class);

    private String[] args;

    public GlobalTools() {

    }

    public void runCmd(String[] args) {
        try {
            this.args = args;
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
                    getListRegions();
                    break;
                case 2:
                    getListAgents();
                    break;
                case 3:
                    getListPlugins();
                    break;
                case 4:
                    getGlobalResourceInfo();
                    break;
                case 5:
                    pControllerResourceInventory();
                    break;
                case 6:
                    gPipelineSubmit();
                    break;
                case 7:
                    getGpipeline();
                    break;
                case 8:
                    getGpipelineExport();
                    break;
                case 9:
                    printGPipelineStatus();
                    break;
                case 10:
                    removeGpipeline();
                    break;
                case 11:
                    removeAllPipelines();
                    break;
                case 12:
                    addQueues();
                    break;
                case 13:
                    addCOP();
                    break;
                case 14:
                    addPP();
                    break;
                case 15:
                    addContainer();
                    break;
                case 16:
                    gPipelineSubmitIRNC();
                case 17:
                    getGlobalNetResourceInfo();
                default:
                    printCmd();
            }

        } catch (Exception ex) {
            printCmd();
            logger.error(ex.getMessage());
        }

    }

    public void addQueues() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("tenant_id","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)

        List<gNode> gNodes = new ArrayList<>();


        //String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};
        String[] locationIds = {"queue"};

        int count = 0;

        for(String location: locationIds) {
            Map<String, String> n0Params = new HashMap<>();

            n0Params.put("pluginname", "cresco-container-plugin");
            n0Params.put("jarfile", "cresco-container-plugin-0.1.0.jar");
            n0Params.put("container_image", "rabbitmq:3-management");
            n0Params.put("e_params", "RABBITMQ_DEFAULT_USER:RABBITMQ_DEFAULT_PASS");
            n0Params.put("RABBITMQ_DEFAULT_USER", "admin");
            n0Params.put("RABBITMQ_DEFAULT_PASS", "cody01");
            n0Params.put("p_params", "5672:15672");
            n0Params.put("location", location);

            gNodes.add(new gNode("dummy", "node" + String.valueOf(count), String.valueOf(count), n0Params));
            count++;
        }

        //n0Params.put("pluginname","cresco-container-plugin");
        //n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        //n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/cresco-container");
        //CRESCO_GC_HOST
        //CRESCO_LOCATION
        //n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        //n0Params.put("location_region","home");
        //n0Params.put("location_agent","home");
        //n0Params.put("location","master");

        /*
        if(args.length == 3) {
            n0Params.put("location",args[2]);
            n0Params.put("e_params","CRESCO_LOCATION=" + args[2] + ",CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        else {
            n0Params.put("location","home");
            n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        */

        /*
        if(args[1] != null) {
            int count = Integer.parseInt(args[1]);
            for(int i = 0; i < count; i++) {
                gNodes.add(new gNode("dummy", "node" + String.valueOf(i), String.valueOf(i), n0Params));
            }
        }
        else {
            gNodes.add(new gNode("dummy", "node0", "0", n0Params));
        }
        */

        //gNode n0 = new gNode("dummy", "node0", "0", n0Params);

        //List<gNode> gNodes = new ArrayList<>();
        //gNodes.add(n0);

        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        me.setParam("gpipeline",compressedGpay);
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        System.out.println(me.getParams().toString());

        //me = CLI.cc.sendMsgEventReturn(me);

        me = CLI.cc.sendJSONReturn("/addgpipeline",gson.toJson(me));

        //ce.setParam("gpipeline_id",gpay.pipeline_id);
        //System.out.println(returnString);
        //System.out.println("SUBMITTED");
        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }



    public void addCOP() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("tenant_id","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)

        List<gNode> gNodes = new ArrayList<>();


        String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};
        //String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};

        int count = 0;

        for(String location: locationIds) {
            Map<String, String> n0Params = new HashMap<>();

            //path_stage
            //ppFactory.setHost(plugin.getConfig().getStringParam("pp_amqp_host","127.0.0.1"));
            //ppFactory.setUsername(plugin.getConfig().getStringParam("pp_amqp_username","admin"));
            //ppFactory.setPassword(plugin.getConfig().getStringParam("pp_amqp_password","cody01"));

            //cpFactory.setHost(plugin.getConfig().getStringParam("cp_amqp_host","127.0.0.1"));
            //cpFactory.setUsername(plugin.getConfig().getStringParam("cp_amqp_username","admin"));
            //cpFactory.setPassword(plugin.getConfig().getStringParam("cp_amqp_password","cody01"));


            n0Params.put("pluginname", "cresco-pp");
            n0Params.put("jarfile", "cresco-pp-1.0-SNAPSHOT.jar");
            n0Params.put("pp_amqp_host","128.163.202." + location);
            n0Params.put("cop_id","cop-" + location);
            n0Params.put("pp_amqp_username","admin");
            n0Params.put("pp_amqp_password","cody01");
            n0Params.put("cp_amqp_host","cresco.uky.edu");
            n0Params.put("cp_amqp_username","admin");
            n0Params.put("cp_amqp_password","cody01");
            n0Params.put("path_stage","2");
            n0Params.put("location", location);

            gNodes.add(new gNode("dummy", "node" + String.valueOf(count), String.valueOf(count), n0Params));
            count++;
        }

        //n0Params.put("pluginname","cresco-container-plugin");
        //n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        //n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/cresco-container");
        //CRESCO_GC_HOST
        //CRESCO_LOCATION
        //n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        //n0Params.put("location_region","home");
        //n0Params.put("location_agent","home");
        //n0Params.put("location","master");

        /*
        if(args.length == 3) {
            n0Params.put("location",args[2]);
            n0Params.put("e_params","CRESCO_LOCATION=" + args[2] + ",CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        else {
            n0Params.put("location","home");
            n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        */

        /*
        if(args[1] != null) {
            int count = Integer.parseInt(args[1]);
            for(int i = 0; i < count; i++) {
                gNodes.add(new gNode("dummy", "node" + String.valueOf(i), String.valueOf(i), n0Params));
            }
        }
        else {
            gNodes.add(new gNode("dummy", "node0", "0", n0Params));
        }
        */

        //gNode n0 = new gNode("dummy", "node0", "0", n0Params);

        //List<gNode> gNodes = new ArrayList<>();
        //gNodes.add(n0);

        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        me.setParam("gpipeline",compressedGpay);
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        System.out.println(me.getParams().toString());

        //me = CLI.cc.sendMsgEventReturn(me);

        me = CLI.cc.sendJSONReturn("/addgpipeline",gson.toJson(me));

        //ce.setParam("gpipeline_id",gpay.pipeline_id);
        //System.out.println(returnString);
        //System.out.println("SUBMITTED");
        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }

    public void addContainer() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("action_tenantid","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)

        List<gNode> gNodes = new ArrayList<>();

        String[] locationIds = {"14", "15", "16"};
        //String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};
        //String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};

        int count = 0;

        for(String location: locationIds) {
            Map<String, String> n0Params = new HashMap<>();

            //path_stage
            //ppFactory.setHost(plugin.getConfig().getStringParam("pp_amqp_host","127.0.0.1"));
            //ppFactory.setUsername(plugin.getConfig().getStringParam("pp_amqp_username","admin"));
            //ppFactory.setPassword(plugin.getConfig().getStringParam("pp_amqp_password","cody01"));

            //cpFactory.setHost(plugin.getConfig().getStringParam("cp_amqp_host","127.0.0.1"));
            //cpFactory.setUsername(plugin.getConfig().getStringParam("cp_amqp_username","admin"));
            //cpFactory.setPassword(plugin.getConfig().getStringParam("cp_amqp_password","cody01"));

            /*
            n0Params.put("pluginname", "cresco-pp");
            n0Params.put("jarfile", "cresco-pp-1.0-SNAPSHOT.jar");
            n0Params.put("pp_amqp_host","128.163.202." + location);
            n0Params.put("pp_amqp_username","admin");
            n0Params.put("pp_amqp_password","cody01");
            n0Params.put("cop_id","cop-" + location);
            n0Params.put("path_stage","1");
            n0Params.put("location", location);
            */

            n0Params.put("pluginname", "cresco-container-plugin");
            n0Params.put("jarfile", "cresco-container-plugin-0.1.0.jar");
            n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/pp");
            n0Params.put("e_params", "CRESCO_path_stage:CRESCO_cop_id:CRESCO_pp_amqp_host:CRESCO_pp_amqp_username:CRESCO_pp_amqp_password:CRESCO_discovery_secret_agent:CRESCO_discovery_ipv4_agent_timeout");
            n0Params.put("CRESCO_path_stage","1");
            n0Params.put("CRESCO_cop_id","cop-"+location);
            //n0Params.put("CRESCO_pp_amqp_host","128.163.202." + location);
            n0Params.put("CRESCO_pp_amqp_host","10.33.70.18");
            n0Params.put("CRESCO_pp_amqp_username","admin");
            n0Params.put("CRESCO_pp_amqp_password","cody01");
            n0Params.put("location", location);
            n0Params.put("CRESCO_discovery_secret_agent","cresco_discovery_secret" + location);
            n0Params.put("CRESCO_discovery_ipv4_agent_timeout","20000");


            gNodes.add(new gNode("dummy", "node" + String.valueOf(count), String.valueOf(count), n0Params));
            count++;
        }

        //n0Params.put("pluginname","cresco-container-plugin");
        //n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        //n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/cresco-container");
        //CRESCO_GC_HOST
        //CRESCO_LOCATION
        //n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        //n0Params.put("location_region","home");
        //n0Params.put("location_agent","home");
        //n0Params.put("location","master");

        /*
        if(args.length == 3) {
            n0Params.put("location",args[2]);
            n0Params.put("e_params","CRESCO_LOCATION=" + args[2] + ",CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        else {
            n0Params.put("location","home");
            n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        */

        /*
        if(args[1] != null) {
            int count = Integer.parseInt(args[1]);
            for(int i = 0; i < count; i++) {
                gNodes.add(new gNode("dummy", "node" + String.valueOf(i), String.valueOf(i), n0Params));
            }
        }
        else {
            gNodes.add(new gNode("dummy", "node0", "0", n0Params));
        }
        */

        //gNode n0 = new gNode("dummy", "node0", "0", n0Params);

        //List<gNode> gNodes = new ArrayList<>();
        //gNodes.add(n0);

        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        System.out.println(gson.toJson(gpay));

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        me.setParam("action_gpipeline",compressedGpay);
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        System.out.println(me.getParams().toString());

        //me = CLI.cc.sendMsgEventReturn(me);

        me = CLI.cc.sendJSONReturn("/addgpipeline",gson.toJson(me));

        //ce.setParam("gpipeline_id",gpay.pipeline_id);
        //System.out.println(returnString);
        //System.out.println("SUBMITTED");
        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }


    public void addPP() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("tenant_id","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)

        List<gNode> gNodes = new ArrayList<>();


        String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};
        //String[] locationIds = {"14", "15", "17", "18", "20", "21", "22", "23", "25", "26", "27", "28", "30", "31", "32", "33", "34", "36", "38"};

        int count = 0;

        for(String location: locationIds) {
            Map<String, String> n0Params = new HashMap<>();

            //path_stage
            //ppFactory.setHost(plugin.getConfig().getStringParam("pp_amqp_host","127.0.0.1"));
            //ppFactory.setUsername(plugin.getConfig().getStringParam("pp_amqp_username","admin"));
            //ppFactory.setPassword(plugin.getConfig().getStringParam("pp_amqp_password","cody01"));

            //cpFactory.setHost(plugin.getConfig().getStringParam("cp_amqp_host","127.0.0.1"));
            //cpFactory.setUsername(plugin.getConfig().getStringParam("cp_amqp_username","admin"));
            //cpFactory.setPassword(plugin.getConfig().getStringParam("cp_amqp_password","cody01"));

            /*
            n0Params.put("pluginname", "cresco-pp");
            n0Params.put("jarfile", "cresco-pp-1.0-SNAPSHOT.jar");
            n0Params.put("pp_amqp_host","128.163.202." + location);
            n0Params.put("pp_amqp_username","admin");
            n0Params.put("pp_amqp_password","cody01");
            n0Params.put("cop_id","cop-" + location);
            n0Params.put("path_stage","1");
            n0Params.put("location", location);
            */

            n0Params.put("pluginname", "cresco-container-plugin");
            n0Params.put("jarfile", "cresco-container-plugin-0.1.0.jar");
            n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/pp");
            n0Params.put("e_params", "CRESCO_path_stage:CRESCO_cop_id:CRESCO_pp_amqp_host:CRESCO_pp_amqp_username:CRESCO_pp_amqp_password:CRESCO_discovery_secret_agent:CRESCO_discovery_ipv4_agent_timeout");
            n0Params.put("CRESCO_path_stage","1");
            n0Params.put("CRESCO_cop_id","cop-"+location);
            n0Params.put("CRESCO_pp_amqp_host","128.163.202." + location);
            n0Params.put("CRESCO_pp_amqp_username","admin");
            n0Params.put("CRESCO_pp_amqp_password","cody01");
            n0Params.put("location", location);
            n0Params.put("CRESCO_discovery_secret_agent","cresco_discovery_secret" + location);
            n0Params.put("CRESCO_discovery_ipv4_agent_timeout","20000");


            gNodes.add(new gNode("dummy", "node" + String.valueOf(count), String.valueOf(count), n0Params));
            count++;
        }

        //n0Params.put("pluginname","cresco-container-plugin");
        //n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        //n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/cresco-container");
        //CRESCO_GC_HOST
        //CRESCO_LOCATION
        //n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        //n0Params.put("location_region","home");
        //n0Params.put("location_agent","home");
        //n0Params.put("location","master");

        /*
        if(args.length == 3) {
            n0Params.put("location",args[2]);
            n0Params.put("e_params","CRESCO_LOCATION=" + args[2] + ",CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        else {
            n0Params.put("location","home");
            n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        */

        /*
        if(args[1] != null) {
            int count = Integer.parseInt(args[1]);
            for(int i = 0; i < count; i++) {
                gNodes.add(new gNode("dummy", "node" + String.valueOf(i), String.valueOf(i), n0Params));
            }
        }
        else {
            gNodes.add(new gNode("dummy", "node0", "0", n0Params));
        }
        */

        //gNode n0 = new gNode("dummy", "node0", "0", n0Params);

        //List<gNode> gNodes = new ArrayList<>();
        //gNodes.add(n0);

        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        me.setParam("gpipeline",compressedGpay);
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        System.out.println(me.getParams().toString());

        //me = CLI.cc.sendMsgEventReturn(me);

        me = CLI.cc.sendJSONReturn("/addgpipeline",gson.toJson(me));

        //ce.setParam("gpipeline_id",gpay.pipeline_id);
        //System.out.println(returnString);
        //System.out.println("SUBMITTED");
        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }



    public void printCmd() {
        System.out.println("0=[pControllerPluginInventory()]");
        System.out.println("1=[pControllerResourceInventory()]");
        System.out.println("2=[gPipelineSubmit()]");
    }

    public String getEnvStatus(String environment_id, String environment_value) {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "getenvstatus");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "getenvstatus");
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
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "addplugin");
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
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "removeplugin");
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
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "getpluginstatus");
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
        me.setParam("globalcmd", Boolean.TRUE.toString());

        me.setParam("action", "plugindownload");
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
        me.setParam("globalcmd", Boolean.TRUE.toString());

        me.setParam("action", "plugininventory");
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

    public void pControllerResourceInventory() {
        List<String> inventory = getControllerResourceInventory();
        System.out.println("inventory list = " + inventory.size());
        for (String str : inventory) {
            System.out.println("inventory item = " + str);
        }

    }

    public void removeAllPipelines() {

        for(String pipelineId : getGpipelineList() ) {
            System.out.println("Removing Pipeline_id: " + pipelineId);
            removeGpipeline(pipelineId);
        }
    }

    public List<String> getGpipelineList() {

        List<String> pipelineList = null;
        try {
            pipelineList = new ArrayList<>();
            MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "getgpipelinelist");
            me = CLI.cc.sendMsgEventReturn(me);

            if (me == null) {
                System.out.println("Can't get gpipeline");
            } else {
                if (me.getParam("gpipeline_ids") != null) {
                    String pipelineIdString = me.getParam("gpipeline_ids");
                    //System.out.println(pipelineIdString);
                    if(pipelineIdString.contains(",")) {
                        String[] pipelineIds = pipelineIdString.split(",");
                        for(String pipelineId : pipelineIds) {
                            pipelineList.add(pipelineId);
                        }
                    }
                    else {
                        pipelineList.add(pipelineIdString);
                    }
                }
                //System.out.println(me.getParams().toString());
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return pipelineList;
    }

    public String stringUncompress(String str) {
        String uncompressedString = null;
        try {

            byte[] exportDataRawCompressed = DatatypeConverter.parseBase64Binary(str);
            InputStream iss = new ByteArrayInputStream(exportDataRawCompressed);
            //uncompress
            InputStream is = new GZIPInputStream(iss);
            uncompressedString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
        }
        catch(Exception ex) {
            logger.error("uncompressParam " + ex.getMessage());
        }
        return uncompressedString;
    }

    public void printGPipelineStatus() {

            MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "getgpipelinestatus");
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                /*
                if(me.getParam("gpipeline_ids") != null) {
                    String pipelineIdString = me.getParam("gpipeline_ids");
                    System.out.println(pipelineIdString);
                }
                */
                //System.out.println(me.getParams().toString());
                String compressed = me.getParam("pipelineinfo");
                String uncompressed = stringUncompress(compressed);
                System.out.println(uncompressed);

            }
    }

    public void removeGpipeline(String pipelineId) {
        //String pipelineId = args[1];

            MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelineremove");
            me.setParam("pipeline_id", pipelineId);
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                System.out.println(me.getParams().toString());
            }

    }

    public void removeGpipeline() {
        System.out.println("args = " + args.length + " " + args[1]);
        String pipelineId = args[1];
        if(pipelineId == null) {
            System.out.println("Please enter pipeline_id");
        }
        else {
            MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "gpipelineremove");
            me.setParam("action_pipelineid", args[1]);
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                System.out.println(me.getParams().toString());
            }
        }

    }

    public void getGpipelineExport() {
        System.out.println("args = " + args.length + " " + args[1]);
        String pipelineId = args[1];
        if(pipelineId == null) {
            System.out.println("Please enter pipeline_id");
        }
        else {
            MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "getgpipelineexport");
            me.setParam("action_pipelineid", args[1]);
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                //System.out.println(me.getParams().toString());
                //gpipeline
                String compressed = me.getParam("gpipeline");
                String uncompressed = stringUncompress(compressed);
                System.out.println(uncompressed);
                //System.out.println(me.getParam("gpipeline"));
            }
        }

    }

    public void getGpipeline() {
        System.out.println("args = " + args.length + " " + args[1]);
        String pipelineId = args[1];
        if(pipelineId == null) {
            System.out.println("Please enter pipeline_id");
        }
        else {
            MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "getgpipeline");
            me.setParam("action_pipelineid", args[1]);
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                //System.out.println(me.getParams().toString());
                //gpipeline
                String compressed = me.getParam("gpipeline");
                String uncompressed = stringUncompress(compressed);
                System.out.println(uncompressed);
                //System.out.println(me.getParam("gpipeline"));
            }
        }

    }

    public String getGpipelineStatus(String pipelineId) {
        String pipelineStatus = null;
        if(pipelineId == null) {
            System.out.println("Please enter pipeline_id");
        }
        else {
            MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "getgpipelinestatus");
            me.setParam("pipeline_id", pipelineStatus);
            me = CLI.cc.sendMsgEventReturn(me);


            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                //System.out.println(me.getParams().toString());
                if(me.getParam("status_code") != null) {
                pipelineStatus =   me.getParam("status_code");
                }
            }
        }
        return pipelineStatus;
    }

    /*
    public void getGpipelineStatus() {
        System.out.println("args = " + args.length + " " + args[1]);
        String pipelineId = args[1];
        if(pipelineId == null) {
            System.out.println("Please enter pipeline_id");
        }
        else {
            MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
            me.setParam("globalcmd", Boolean.TRUE.toString());
            me.setParam("action", "getgpipelinestatus");
            me.setParam("pipeline_id", args[1]);
            me = CLI.cc.sendMsgEventReturn(me);

            if(me == null) {
                System.out.println("Can't get gpipeline");
            }
            else {
                System.out.println(me.getParams().toString());
            }
        }

    }
    */

    public void gPipelineSubmit3() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("tenant_id","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)
        Map<String,String> n0Params = new HashMap<>();
        n0Params.put("pluginname","p0");
        n0Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");
        n0Params.put("location","0");

        Map<String,String> n1Params = new HashMap<>();
        n1Params.put("pluginname","p1");
        n1Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");

        Map<String,String> n2Params = new HashMap<>();
        n2Params.put("pluginname","p2");
        n2Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");

        Map<String,String> n3Params = new HashMap<>();
        n3Params.put("pluginname","p3");
        n3Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");


        gNode n0 = new gNode("dummy", "node0", "0", n0Params);
        gNode n1 = new gNode("dummy", "node1", "1", n1Params);
        gNode n2 = new gNode("dummy", "node2", "2", n2Params);
        gNode n3 = new gNode("dummy", "node3", "3", n3Params);

        List<gNode> gNodes = new ArrayList<>();
        gNodes.add(n0);
        gNodes.add(n1);
        gNodes.add(n2);


        gEdge e0 = new gEdge("0","0","1");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());
        System.out.println("SUBMITTED");
    }

    public void gPipelineSubmit2() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("tenant_id","0");

        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)
        Map<String,String> n0Params = new HashMap<>();
        n0Params.put("pluginname","cresco-sysinfo-plugin");
        n0Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");

        Map<String,String> n1Params = new HashMap<>();
        n1Params.put("pluginname","cresco-sysinfo-plugin");
        n1Params.put("jarfile","cresco-sysinfo-plugin-0.5.0.jar");

        gNode n0 = new gNode("dummy", "node0", "0", n0Params);
        gNode n1 = new gNode("dummy", "node0", "1", n1Params);

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

    public void gPipelineSubmitIRNC() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("action_tenantid","0");


        Gson gson = new GsonBuilder().create();

/*
        Map<String,String> n0Params = new HashMap<>();
        n0Params.put("pluginname","cresco-container-plugin");
        n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n0Params.put("container_image","docker.elastic.co/elasticsearch/elasticsearch:5.2.2");
        n0Params.put("e_parms","http.host:transport.host");
        n0Params.put("http.host","0.0.0.0");
        n0Params.put("transport.host","127.0.0.1");
        n0Params.put("p_parms","9200:9200");
        n0Params.put("location","demo-global-controller");
*/
        //docker run -p 9200:9200 -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" docker.elastic.co/elasticsearch/elasticsearch:5.2.2


        Map<String,String> n0Params = new HashMap<>();
        n0Params.put("pluginname","cresco-container-plugin");
        n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n0Params.put("container_image","rabbitmq:3-management");
        n0Params.put("e_parms","RABBITMQ_DEFAULT_USER:RABBITMQ_DEFAULT_PASS");
        n0Params.put("RABBITMQ_DEFAULT_USER","user");
        n0Params.put("RABBITMQ_DEFAULT_PASS","password");
        n0Params.put("p_parms","5672:15672");
        //n0Params.put("location","demo-global-controller");
        n0Params.put("location","location0");

        Map<String,String> n1Params = new HashMap<>();
        n1Params.put("pluginname","cresco-container-plugin");
        n1Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n1Params.put("container_image","rabbitmq:3-management");
        n1Params.put("e_parms","RABBITMQ_DEFAULT_USER:RABBITMQ_DEFAULT_PASS");
        n1Params.put("RABBITMQ_DEFAULT_USER","user");
        n1Params.put("RABBITMQ_DEFAULT_PASS","password");
        n1Params.put("p_parms","5672:15672");
        n1Params.put("location","location1");

        Map<String,String> n2Params = new HashMap<>();
        n2Params.put("pluginname","cresco-container-plugin");
        n2Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n2Params.put("container_image","rabbitmq:3-management");
        n2Params.put("e_parms","RABBITMQ_DEFAULT_USER:RABBITMQ_DEFAULT_PASS");
        n2Params.put("RABBITMQ_DEFAULT_USER","user");
        n2Params.put("RABBITMQ_DEFAULT_PASS","password");
        n2Params.put("p_parms","5672:15672");
        n2Params.put("location","location2");

        List<gNode> gNodes = new ArrayList<>();

        gNodes.add(new gNode("QueueProvider", "location0", "0", n0Params));
        gNodes.add(new gNode("QueueProvider", "location1", "1", n1Params));
        gNodes.add(new gNode("QueueProvider", "location2", "2", n2Params));


        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        System.out.println("\n" + gson.toJson(gpay) + "\n");

        me.setParam("action_gpipeline",compressedGpay);

        System.out.println(me.getParams().toString());

        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }


    public void gPipelineSubmit() {
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "gpipelinesubmit");
        me.setParam("action_tenantid","0");


        Gson gson = new GsonBuilder().create();

        //public gNode(String type, String node_name, String node_id,Map<String, String> params)
        Map<String,String> n0Params = new HashMap<>();

        /*
        n0Params.put("pluginname","cresco-container-plugin");
        n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n0Params.put("container_image","rabbitmq:3-management");
        n0Params.put("e_parms","RABBITMQ_DEFAULT_USER,RABBITMQ_DEFAULT_PASS");
        n0Params.put("RABBITMQ_DEFAULT_USER","user");
        n0Params.put("RABBITMQ_DEFAULT_PASS","password");
        n0Params.put("p_parms","5672,15672");
        n0Params.put("location","0");
        */

        n0Params.put("pluginname","cresco-container-plugin");
        n0Params.put("jarfile","cresco-container-plugin-0.1.0.jar");
        n0Params.put("container_image", "gitlab.rc.uky.edu:4567/cresco/cresco-container");
        //CRESCO_GC_HOST
        //CRESCO_LOCATION
        //n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");
        //n0Params.put("location_region","home");
        //n0Params.put("location_agent","home");
        //n0Params.put("location","master");
        if(args.length == 3) {
            n0Params.put("location",args[2]);
            n0Params.put("e_params","CRESCO_LOCATION=" + args[2] + ",CRESCO_AGENT_DISC_TIMEOUT=2000");
        }
        else {
            n0Params.put("location","home");
            n0Params.put("e_params","CRESCO_LOCATION=home,CRESCO_AGENT_DISC_TIMEOUT=2000");

        }

        List<gNode> gNodes = new ArrayList<>();

        if(args[1] != null) {
            int count = Integer.parseInt(args[1]);
            for(int i = 0; i < count; i++) {
                gNodes.add(new gNode("dummy", "node" + String.valueOf(i), String.valueOf(i), n0Params));
            }
        }
        else {
            gNodes.add(new gNode("dummy", "node0", "0", n0Params));
        }


        //gNode n0 = new gNode("dummy", "node0", "0", n0Params);

        //List<gNode> gNodes = new ArrayList<>();
        //gNodes.add(n0);

        gEdge e0 = new gEdge("0","1000000","1000000");

        List<gEdge> gEdges = new ArrayList<>();
        gEdges.add(e0);

        gPayload gpay = new gPayload(gNodes,gEdges);
        gpay.pipeline_id = "0";
        gpay.pipeline_name = "demo_pipeline";

        String compressedGpay = DatatypeConverter.printBase64Binary(stringCompress(gson.toJson(gpay)));

        me.setParam("gpipeline_compressed",String.valueOf(Boolean.TRUE));

        System.out.println("\n" + gson.toJson(gpay) + "\n");

        me.setParam("action_gpipeline",compressedGpay);
        //gPayload me = gson.fromJson(json, gPayload.class);
        //System.out.println(p);
        //return gson.toJson(gpay);

        System.out.println(me.getParams().toString());

        me = CLI.cc.sendMsgEventReturn(me);

        //me = CLI.cc.sendJSONReturn("/addgpipeline",gson.toJson(me));


        //ce.setParam("gpipeline_id",gpay.pipeline_id);
        //System.out.println(returnString);
        //System.out.println("SUBMITTED");
        System.out.println("PipelineId =" +  me.getParam("gpipeline_id"));
    }

    public byte[] stringCompress(String str) {
        byte[] dataToCompress = str.getBytes(StandardCharsets.UTF_8);
        byte[] compressedData = null;
        try
        {
            ByteArrayOutputStream byteStream =
                    new ByteArrayOutputStream(dataToCompress.length);
            try
            {
                GZIPOutputStream zipStream =
                        new GZIPOutputStream(byteStream);
                try
                {
                    zipStream.write(dataToCompress);
                }
                finally
                {
                    zipStream.close();
                }
            }
            finally
            {
                byteStream.close();
            }

            compressedData = byteStream.toByteArray();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return compressedData;
    }

    public List<String> getControllerResourceInventory() {
        List<String> inventory = new ArrayList<String>();
        MsgEvent me = new MsgEvent(MsgEventType.CONFIG, null, null, null, "get resourceinventory inventory");
        //me.setParam("src_region", "external");
        //me.setParam("src_agent", "external");
        //me.setParam("dst_region", "external");
        //me.setParam("dst_agent", "external");
        me.setParam("globalcmd", Boolean.TRUE.toString());

        me.setParam("action", "resourceinventory");
        //return CLI.cc.sendMsgEvent(me);

        System.out.println("SENDING..");
        me = CLI.cc.sendMsgEventReturn(me);
        System.out.println("REC...");
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

    public void getListRegions() {
        MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "listregions");

        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

    }

    public void getListAgents() {
        MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "listagents");
        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

    }

    public void getListPlugins() {
        MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "listplugins");
        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

    }

    public void getGlobalResourceInfo() {
        MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "resourceinfo");
        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

    }

    public void getGlobalNetResourceInfo() {
        MsgEvent me = new MsgEvent(MsgEventType.EXEC, null, null, null, "get resourceinventory inventory");
        me.setParam("globalcmd", Boolean.TRUE.toString());
        me.setParam("action", "netresourceinfo");
        me = CLI.cc.sendMsgEventReturn(me);

        System.out.println(me.getParams().toString());

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
