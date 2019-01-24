package api.webhook.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Servlet implementation class WebhookTestServlet
 */
@WebServlet(
		urlPatterns = { "/WebhookTestServlet", "/TestError", "/TestTimeOut"}, 
		initParams = { 
				@WebInitParam(name = "Custom Message", value = "Hello")
				
		})
public class WebhookTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String message="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebhookTestServlet() {
        super();
        // TODO Auto-generated constructor stub
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String notification_id = request.getParameter("notification_id");
		String log_name = request.getParameter("log_name");
		String listType = request.getParameter("list_type");
		PrintWriter out = response.getWriter();
		String msg=null;
		
		if (log_name==null && listType==null && notification_id==null && Utils.checkParams(request)){
			log_name="webhooks.log";
		}
		
		
		if(listType!=null && listType.equalsIgnoreCase("logs")|| listType!=null && listType.equalsIgnoreCase("notifications")){
			msg = Utils.displayDirContent(listType);
		}
		else{
			msg = Utils.readFile(log_name, notification_id);
		}
		
		
		if(notification_id!=null||listType!=null){
			response.setContentType("application/json");
		}
		
		
		if(msg==null){
			msg = "{\"error\":\"No data found!\"}";
		}
		
		out.println(msg);
		
		System.out.println(" Webhook Test Servlet Called");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str="";
		PrintWriter out = response.getWriter();
		String time_out = request.getParameter("t");
		
		while( (str = br.readLine()) != null ){
			sb.append(str);
		}
		str = sb.toString();
		
		response.setContentType("application/json");
		String respStr;
		if (request.getRequestURI().equals("/WebhookTestListener/TestError")){
			response.setStatus(500);
			respStr = "\"response\": {\"http_response_code\": \"500\"}";
		}
		else if (request.getRequestURI().contains("/TestTimeOut")){
				if(time_out==null){
					time_out ="60";
				} 

			try {
				TimeUnit.SECONDS.sleep(Integer.parseInt(time_out));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setStatus (200);
			respStr = "\"response\": {\"http_response_code\": \"200\",\"status\":\"success\"}";
			out.println("{\"status\":\"success\"}");
		}
		else{
			response.setStatus (200);
			respStr = "\"response\": {\"http_response_code\": \"200\",\"status\":\"success\"}";
			out.println("{\"status\":\"success\"}");
		}

		try {
			JSONObject event = new JSONObject(sb.toString());
			message = event.getString("notification_id");
			
		} 
		
		catch (JSONException e) {
			e.printStackTrace();
		}

		
		String logData = "{\"notification_id\":\""+message+"\","+Utils.getHeaders(request)+",\"payload\":" +str+","+respStr+ "}";
		Utils.writeFile("webhooks.log", logData, message);
		
		
		}

}