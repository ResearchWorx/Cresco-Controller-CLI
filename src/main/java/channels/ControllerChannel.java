package channels;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.CLI;
import shared.MsgEvent;

public class ControllerChannel {

	private final String USER_AGENT = "Cresco-Agent-Controller-Plugin/0.5.0";
	    
	
	private String controllerUrl;
	private String performanceUrl;

	public ControllerChannel()
	{
		if(CLI.config.getStringParam("globalcontroller","globalcontroller_host") != null)
		{
			if(CLI.config.getStringParam("globalcontroller","globalcontroller_port") != null)
			{
				controllerUrl = "http://" + CLI.config.getStringParam("globalcontroller","globalcontroller_host") + ":" + CLI.config.getStringParam("globalcontroller","globalcontroller_port") + "/API";
			}
			else
			{
				controllerUrl = "http://" + CLI.config.getStringParam("globalcontroller","globalcontroller_host") + ":32000/API";
			}
			performanceUrl = "http://" + CLI.config.getStringParam("globalcontroller","globalcontroller_host") + ":32002/API";

		}
		//controllerUrl = "http://" + CLI.config.getStringParam("globalcontroller","globalcontroller_host") + ":" + CLI.config.getStringParam("globalcontroller","globalcontroller_port") + "/API";
		System.out.println("Global Controller URL: " + controllerUrl);
	}
	
	public MsgEvent sendMsgEventReturn(MsgEvent le)
    {
		MsgEvent me = null;
		
		try
		{
			Map<String,String> tmpMap = le.getParams();
			Map<String,String> leMap = null;
			String type = null;
			synchronized (tmpMap)
			{
				leMap = new ConcurrentHashMap<String,String>(tmpMap);
				type = le.getMsgType().toString();
			}
			String url = controllerUrl + urlFromMsg(type,leMap);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
			con.setConnectTimeout(5000);
			
			// optional default is GET
			con.setRequestMethod("GET");
 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
 
			int responseCode = con.getResponseCode();
			
			if(responseCode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));				        
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) 
				{
						response.append(inputLine);
				}
				in.close();
			
				
				try
				{
					//System.out.println(response);
					me = meFromJson(response.toString());
				}
				catch(Exception ex)
				{
					System.out.println("Controller : ControllerChannel : Error meFromJson");
				}					
				return me;
			}
			else
			{
			return me;
			}
		}
		catch(Exception ex)
		{
			System.out.println("Controller : ControllerChannel : sendControllerLog : " + ex.toString());
			return me;
		}
	}
 
	public boolean sendMsgEvent(MsgEvent le)
    {
		try
		{
			Map<String,String> tmpMap = le.getParams();
			Map<String,String> leMap = null;
			String type = null;
			synchronized (tmpMap)
			{
				leMap = new ConcurrentHashMap<String,String>(tmpMap);
				type = le.getMsgType().toString();
			}
			String url = controllerUrl + urlFromMsg(type,leMap);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
			con.setConnectTimeout(5000);
			
			// optional default is GET
			con.setRequestMethod("GET");
 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
 
			int responseCode = con.getResponseCode();
			
			if(responseCode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));				        
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) 
				{
						response.append(inputLine);
				}
				in.close();
			
				try
				{
					System.out.println(response);
					//ce = meFromJson(response.toString());
				}
				catch(Exception ex)
				{
					System.out.println("Controller : ControllerChannel : Error meFromJson");
				}					
				return true;
			}
			else
			{
			return false;
			}
		}
		catch(Exception ex)
		{
			System.out.println("Controller : ControllerChannel : sendControllerLog : " + ex.toString());
			return false;
		}
	}
 
    public String urlFromMsg(String type, Map<String,String> leMap)
    {
    	
    	try
    	{
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append("?type=" + type);
    		
    		Iterator it = leMap.entrySet().iterator();
    		while (it.hasNext()) 
    		{
    			Map.Entry pairs = (Map.Entry)it.next();
    			sb.append("&paramkey=" + URLEncoder.encode(pairs.getKey().toString(), "UTF-8") + "&paramvalue=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8"));
    			it.remove(); // avoids a ConcurrentModificationException
    		}
    		//System.out.println(sb.toString());
        return sb.toString();
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Controller : ControllerChannel : urlFromMsg :" + ex.toString());
    		return null;
    	}
    }
   
    private MsgEvent meFromJson(String jsonMe)
	{
		Gson gson = new GsonBuilder().create();
        MsgEvent me = gson.fromJson(jsonMe, MsgEvent.class);
        //System.out.println(p);
        return me;
	} 
    
    
	// HTTP GET request
	
	
}
