package api.webhook.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Utils {
	
	private static final Logger logger = LogManager.getLogger(Utils.class);
	
	/**
	 * 
	 * @param filename
	 * @param key - lookup field to retrieve data
	 * @return
	 */
	public static String readFile(String log_name, String search_param){
		String path = System.getProperty("catalina.base");
		File logFile;
		String dataStr = null;
		
		if(search_param==null){
			logFile = new File(path+"/logs/webhook/"+log_name);
		}
		else{
			logFile = new File(path+"/logs/webhook/json/"+search_param+".json");
			
		}
		
		
		try {
			dataStr = FileUtils.readFileToString(logFile);
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}

			return dataStr;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param fileContent
	 */
	@SuppressWarnings("deprecation")
	public static void writeFile(String fileName, String fileContent, String notification_id){
		String path = System.getProperty("catalina.base");
		File jsonFile = new File(path+"/logs/webhook/json/"+notification_id+".json");
		
		try {
			
			logger.info(prettyPrint(fileContent));
			FileUtils.writeStringToFile(jsonFile, prettyPrint(fileContent));			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param jsonStr
	 * @return pretty JSON formatted string
	 */
	public static String prettyPrint(String jsonStr){
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		
		try{
			JsonElement je = jp.parse(jsonStr);
			return gson.toJson(je);
		}
		catch(Exception e){
			return jsonStr;
		}
		
	}
	
	/**
	 * 
	 * @param folderName
	 * @return
	 */
	public static String displayDirContent(String param){
		String path = System.getProperty("catalina.base");
		File dir = new File(path+"/logs/webhook");
		if(param.equalsIgnoreCase("notifications")){
			dir = new File(path+"/logs/webhook/json");
		}
		StringBuilder sb = new StringBuilder("");
		int i=0;
		File[] files = dir.listFiles(onlyFiles);
		if (files != null) {
            for (File file : files) {
                try {
                	
                    if(i<files.length-1)
                    	sb.append("\"" +i++ + "\":\"" + file.getName() + "\",");
                    else
                    	sb.append("\"" +i++ + "\":\"" + file.getName() + "\"");
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }   
            }
        }
		
		return prettyPrint("{\"names\":[{"+sb.toString().replace(".json", "")+"}]}");
	}
	
	private static FileFilter onlyFiles = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isFile();
        }
    };
    
    /**
     * 
     * @param req
     * @return
     */
    public static Boolean checkParams(HttpServletRequest req){
    	Map map=req.getParameterMap();
        Set set = map.entrySet();
        Iterator itr = set.iterator();
        String str = "";
        
        while(itr.hasNext()){
            Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)itr.next();
            str = str+ entry.getKey();
        }
        
        if("log_name,list_type, notification_id".contains(str))
        	return true;
        else
        	return false;
    }
    
    /**
     * 
     * @param req
     * @return
     */
    public static String getHeaders(HttpServletRequest request){
    	Enumeration<String> headerNames = request.getHeaderNames();
    	String headerName, headers = "";
		while (headerNames.hasMoreElements()) {
			headerName = headerNames.nextElement();
			headers = headers+"\""+headerName+"\":"+"\""+request.getHeader(headerName)+"\"";
			if(headerNames.hasMoreElements()){
				headers = headers+",";
			}
					}
		return "\"headers\":{"+headers+"}";
    	
    }

}
