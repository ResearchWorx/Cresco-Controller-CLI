package core;

/**
 * Created by cody on 4/2/16.
 */
public class ClientTools2 {


private void test() {
    for (int i = 0; i < 2; i++) {
        //String inode_id = "someinodeid" + String.valueOf(i);
        //String configparams =  "resource_id=" + resource_id +",inode_id="+ inode_id +",perflevel=2,pluginname=cresco-agent-dummy-plugin,pluginversion=0.5.0-SNAPSHOT.2325b.2015-10-02T15:58:40Z,watchdogtimer=5000";
        //cf.addPlugin(resource_id, inode_id,configparams);
        //cf.removePlugin(resource_id, inode_id);
    }

    //String resource_id = "someresourceid";
    //String inode_id = "someinodeid";
    //String configparams =  "resource_id=" + resource_id +",inode_id="+ inode_id +",perflevel=1,pluginname=cresco-agent-dummy-plugin,pluginversion=0.5.0-SNAPSHOT.2325b.2015-10-02T15:58:40Z,watchdogtimer=5000";
    //String pluginPath = cf.addPlugin(resource_id, inode_id,configparams);
    //String pluginPath = cf.removePlugin(resource_id, inode_id);

    //String inode_id = "someinodeid" + 34;
    //String configparams =  "pluginname=cresco-agent-sysinfo-plugin,pluginversion=0.5.0-SNAPSHOT.${buildNumber}.2015-11-01T16:01:51Z,watchdogtimer=5000";
    //cf.addPlugin(resource_id, inode_id,configparams);

    /*
    cf.getEnvStatus("location", "home");

    List<String> resourceList = cf.getControllerResourceInventory();


    List<String> pluginList = cf.getControllerPluginInventory();
    if (pluginList != null) {
        for (String pluginInfo : pluginList) {
            //System.out.println(pluginInfo);
            cf.getPluginInfo(pluginInfo);
            //String[] str = pluginInfo.split("=");
            //String configParams = "pluginname=" + str[0] + ",pluginversion=" + str[1];

            //String pluginPath = cf.getPluginStatus("0");
            //String pluginPath = cf.removePlugin("some node id");

            //String pluginPath = cf.addPlugin("0", "some application name",configParams);
            //System.out.println("status_code=" + pluginPath);
        }
    }
*/
//cf.updatePlugins(true);
		/*
		List<String> resource = gdb.getresourceNodeList(null,null);
		if(resource != null)
		{
			System.out.println("Resource List");

			for(String str : resource)
			{

				List<String> inodes = gdb.getresourceNodeList(str,null);
				if(inodes == null)
				{
					System.out.println("NO NODES!");
				}
				else
				{
					for(String str2 : inodes)
					{
						System.out.println("resource_id" + str + " inode=" + str2 + " status=" + cf.getPluginStatus(str,str2));
					}
				}
			}
		}
		*/
		/*
		//updating controller plugins
		cf.updatePlugins();
		//listing controller plugin inventory
		List<String> pluginList = cf.getControllerPluginInventory();
		if(pluginList != null)
		{
			for(String pluginInfo : pluginList)
			{
				System.out.println(pluginInfo);
				//String[] str = pluginInfo.split("=");
				//String configParams = "pluginname=" + str[0] + ",pluginversion=" + str[1];

				//String pluginPath = cf.getPluginStatus("0");
				//String pluginPath = cf.removePlugin("some node id");

				//String pluginPath = cf.addPlugin("0", "some application name",configParams);
				//System.out.println("status_code=" + pluginPath);
			}
		}
		*/

//pluginPath = cf.getPluginStatus("r0","i0");

//cf.updatePlugins();


//isNode
		/*
		for(String region : gdb.getNodeList(null, null, null))
		{
			gdb.removeNode(region, null,null);
		}
		*/
		/*
		for(String region : gdb.getNodeList(null, null, null))
		{
			for(String agent : gdb.getNodeList(region, null, null))
			{
				for(String plugin : gdb.getNodeList(region, agent, null))
				{
					System.out.println("region=" + region + " agent=" + agent + " plugin=" + plugin);
					gdb.removeNode(region, agent, plugin);
				}
			}
		}
		*/
		/*
		for(int i = 0; i< 30; i++)
		{
			new Thread(new CreateNodes()).start();
			new Thread(new DeleteNodes()).start();
		}
		*/
		/*
		System.out.println("Starting Node Creation Service");
		CreateNodes cn = new CreateNodes();
		Thread cnThread = new Thread(cn);
		cnThread.start();



		System.out.println("Starting Node Delete Service");
		DeleteNodes dn = new DeleteNodes();
		Thread dnThread = new Thread(dn);
		dnThread.start();
		*/


//System.out.println(gdb.getNodeId("region0", null,null));
//System.out.println(gdb.getNodeId("region0","agent0",null));
//System.out.println(gdb.getNodeId("region0","agent0","plugin/10"));

//addNode
//gdb.addNode("region1000", null, null);

//System.out.println(gdb.getNodeId("region1000", null,null));

//System.out.println(gdb.addNode("region1000", "agent0", null));

//String booha = gdb.addNode("region1001", "agent0", "plugin0");
//String booha = gdb.addNode("region1005", "agent0", "plugin/0");
//System.out.println("Boojaa: " + booha);
//System.out.println(gdb.getNodeId("region1005","agent0","plugin/0"));
//System.out.println(gdb.getNodeId("region1005","agent0",null));
//System.out.println(gdb.getNodeId("region1005",null,null));

		/*
	    gdb.createVertexClass("rNode", rProps);
	    gdb.createVertexClass("aNode", aProps);
	    gdb.createVertexClass("pNode", pProps);


		for(int i = 0; i<500; i++)
		{
			gdb.addrnode("region" + i);

			for(int ii = 0; ii<500; ii++)
			{
				gdb.addanode("region" + i,"agent" + ii);

				for(int iii = 0; iii<500; iii++)
				{
					gdb.addpnode("region" + i,"agent" + ii,"plugin/" + iii);

				}
			}
		}
		*/

		/*
	    OrientGraph graph = gdb.factory.getTx();
		Index<Vertex> index = graph.getIndex("pNode.nodePath", Vertex.class);
		//System.out.println(index.getIndexName());

		System.out.println("Entity.uuid: "+index);
        System.out.println(graph.getIndices());
        System.out.println(graph.getIndexedKeys(Vertex.class));
        graph.shutdown();
		*/

		/*
		OrientGraph graph = gdb.factory.getTx();

		OrientGraphQuery oQuery = (OrientGraphQuery) graph.query();
		Iterable<Vertex> resultIterator =  oQuery.labels("pNode").has( "region", "region33" ).has( "agent", "agent33" ).vertices();
		Iterator<Vertex> iter = resultIterator.iterator();
		while (iter.hasNext())
		{
			Vertex v = iter.next();

			System.out.println(v.getId().toString());
			//System.out.println(v.getId().toString());
		}
		graph.shutdown();
		*/


//WORKING!!!
		/*
		OrientGraph graph = gdb.factory.getTx();

		OrientGraphQuery oQuery = (OrientGraphQuery) graph.query();
		Iterable<Vertex> resultIterator =  oQuery.labels("pNode").has( "region", "region0" ).has( "agent", "agent0" ).vertices();
		Iterator<Vertex> iter = resultIterator.iterator();
		while (iter.hasNext())
		{
			Vertex v = iter.next();

			System.out.println(v.getId().toString());
			System.out.println(v.getId().toString());
		}
		graph.shutdown();
		*/
		/*
		OrientGraph graph = gdb.factory.getTx();

		for (Vertex v : (Iterable<Vertex>) graph.command(
	            new OCommandSQL("SELECT rid FROM INDEX:pNode.nodePath WHERE key = [\"region1000\",\"agent1\"]")).execute())
		{
			OrientVertex vertex = (OrientVertex) v;
			System.out.println(vertex.getIdentity().toString());
			System.out.println(v.getProperty("rid").toString());

		}
		graph.shutdown();
		*/

		/*
		//super fast
		OrientGraph graph = gdb.factory.getTx();

		for (Vertex v : (Iterable<Vertex>) graph.command(
	            new OCommandSQL("SELECT rid FROM INDEX:pNode.nodePath WHERE key = [\"region0\",\"agent1\"]")).execute())
		{
			OrientVertex vertex = (OrientVertex) v;
			System.out.println(vertex.getIdentity().toString());
			System.out.println(v.getProperty("rid").toString());

		}
		graph.shutdown();
		*/

		/*
		ClientFunctions cf = new ClientFunctions();
		List<String> pluginList = cf.getControllerPluginInventory();
		if(pluginList != null)
		{
			for(String pluginInfo : pluginList)
			{
				System.out.println(pluginInfo);
				String[] str = pluginInfo.split("=");
				String configParams = "pluginname=" + str[0] + ",pluginversion=" + str[1];

				//String pluginPath = cf.getPluginStatus("0");
				String pluginPath = cf.removePlugin("some node id");

				//String pluginPath = cf.addPlugin("0", "some application name",configParams);
				System.out.println("status_code=" + pluginPath);
			}
		}
		*/
//cf.updatePlugins();

		/*
		ProvisioningEngine pe = new ProvisioningEngine();
		String plugin = "cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar";
		String pluginurl = "http://127.0.0.1:32003/";
		//pe.downloadPlugin("region0", "agent0", plugin, pluginurl, false);


		String agent_path = gdb.getLowAgent();
		System.out.println("Agent Path: " + agent_path);
		*/

		/*
		System.out.println(pe.getPluginName("/Users/cody/git/test/plugins0/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar"));
		System.out.println(pe.getPluginVersion("/Users/cody/git/test/plugins0/cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar"));

		System.out.println(pe.getPluginName("/Users/cody/git/test/plugins0/cresco-agent-controller-plugin-0.5.0-SNAPSHOT.jar"));
		System.out.println(pe.getPluginVersion("/Users/cody/git/test/plugins0/cresco-agent-controller-plugin-0.5.0-SNAPSHOT.jar"));
		*/

//me.setParam("configparams", "perflevel="+ perflevel + ",pluginname=DummyPlugin,jarfile=cresco-agent-dummy-plugin-0.5.0-SNAPSHOT-jar-with-dependencies.jar,region=" + region  + ",watchdogtimer=5000");

//pe.addPlugin("region0","agent0","1");
//pe.addPlugin("region0","agent0","2");
//pe.addPlugin("region0","agent0","3");
//pe.addPlugin("region0","agent0","4");

//pe.addPlugin("region0","agent1","1");
//pe.addPlugin("region0","agent1","1");

//pe.removePlugin("region0", "agent0","plugin/10");
//pe.removePlugin("region0", "agent0","plugin/11");
//pe.removePlugin("region0", "agent0","plugin/12");
//pe.removePlugin("region0", "agent0","plugin/13");


//pe.removePlugin("region0", "agent1","plugin/2");
//pe.removePlugin("region0", "agent1","plugin/3");
}}
