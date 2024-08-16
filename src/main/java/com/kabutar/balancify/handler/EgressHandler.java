package com.kabutar.balancify.handler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.kabutar.balancify.constants.HttpMethod;

public class EgressHandler {
	
	protected static String handle(
			String reqUrl,
			String method,
			Map<String,List<String>> requestProps,
			String reqParam
			) {
		
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//set request method
			conn.setRequestMethod(method);
			
			// writing request body for post patch put
			if(method.equals(HttpMethod.PATCH) || method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
				conn.setDoOutput(true);
				OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
				os.write(reqParam);
				os.flush();
				os.close();
			}
			
			//set request headers
			for(Map.Entry<String, List<String>> entry:requestProps.entrySet()) {
				for(String headerValue : entry.getValue()) {
					conn.setRequestProperty(entry.getKey(),headerValue);
				}
			}
			
			// get response code
			int responseCode = conn.getResponseCode();
			String responseMessage = conn.getResponseMessage();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reqParam;
	}
}


