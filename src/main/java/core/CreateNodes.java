package core;

import java.util.Random;

public class CreateNodes implements Runnable {

	public static Random rand = null;
	public CreateNodes()
	{
		rand = new Random();
	}
	public void run() 
	{
		try
		{
			while(true)
			{
				String region = "region" + Integer.toString(randInt(0, 100));
				String agent = "agent" + Integer.toString(randInt(0, 100));
				String plugin = "plugin/" + Integer.toString(randInt(0, 100));
				//System.out.println(gdb.addNode(region,agent,plugin));
				CLI.gdb.addNode(region,agent,plugin);
				
			}
		}
		catch(Exception ex)
		{
			System.out.println("CreateNode Error: " + ex.toString());
		}
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: This will (intentionally) not run as written so that folks
	    // copy-pasting have to think about how to initialize their
	    // Random instance.  Initialization of the Random instance is outside
	    // the main scope of the question, but some decent options are to have
	    // a field that is initialized once and then re-used as needed or to
	    // use ThreadLocalRandom (if using at least Java 1.7).
	    

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

		
}
