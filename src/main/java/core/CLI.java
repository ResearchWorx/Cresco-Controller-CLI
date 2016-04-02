package core;


import java.io.File;
import java.util.Arrays;
import java.util.Random;

import channels.ControllerChannel;
import graphdb.GraphDBEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CLI {


	public static Config config;
	public static GraphDBEngine gdb;
	public static ControllerChannel cc;

    private static final Logger logger = LoggerFactory.getLogger(CLI.class);

    public static void main(String[] args) throws Exception
	{
        try {
            config = new Config(checkConfig(args));
            gdb = new GraphDBEngine(); //create graphdb connector
            cc = new ControllerChannel(); //methods to communicate with global controller
            AgentTools at = new AgentTools(); //Agent (internal controller) functions
            GlobalTools gt = new GlobalTools(); //Global (external controller) functions

            if(args[3].toLowerCase().equals("-at")) {
                at.runCmd(Arrays.copyOfRange(args,4,args.length));
            }
            else if(args[3].toLowerCase().equals("-gt")) {
                gt.runCmd(Arrays.copyOfRange(args,4,args.length));
            }
            else {
                logger.info("Enter -at for AgentTools or -gt for GlobalTools");
            }

        }
        catch(Exception ex) {
            logger.error(ex.getMessage());
            System.exit(0);
        }



    }


    public static String checkConfig(String[] args)
    {
        String errorMgs = "Cresco-Controller\n" +
                "Usage: java -jar Cresco-Controller.jar" +
                " -f <configuration_file>\n" +
                " -at <AgentTool Function> or " +
                " -gt <GlobalTool Function>";

System.out.println(args.length);
        if (!(args.length < 2))
        {
            logger.error(errorMgs);
            logger.error("Invalid number of arguements.");
            System.exit(0);
        }
        else if(!args[0].equals("-f"))
        {
            logger.error(errorMgs);
            logger.error("Must specify configuration file.");
            System.exit(0);
        }
        else
        {
            File f = new File(args[1]);
            if(!f.exists())
            {
                logger.error("The specified configuration file: " + args[1] + " is invalid");
                System.exit(0);
            }
        }
        return args[1];
    }


}
