package channels;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import shared.MsgEvent;
import core.CLI;

public class ControllerChannel {

	private final String USER_AGENT = "Cresco-Agent-Controller-Plugin/0.5.0";
	    
	
	private String controllerUrl;
	
	public ControllerChannel()
	{
		controllerUrl = "http://" + CLI.config.getParam("globalcontroller_host") + ":" + CLI.controllerport + "/API";
		
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
					//System.out.println(response);
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
   
    
    
    
	// HTTP GET request
	
	
}
