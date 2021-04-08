package com.foodie.webservice;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.foodie.chatbot.Chatbot;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class WebServer {

	/*Creating ⬢ weatherman-bot509... done
https://weatherman-bot509.herokuapp.com/ | https://git.heroku.com/weatherman-bot509.git
*/
	
	public static void main(String[] args) {
		Spark.setPort(getHerokuAssignedPort());
		Spark.staticFileLocation("/public");
		
		final Chatbot bot = new Chatbot();
		
		get("/", (req, res) -> "Hello World! I am WeatherMan, the weather bot!!");
		
		//post handle for WeatherMan chatbot
		post("/bot", new Route() {
			public Object handle(Request request, Response response) {
				
				String body = request.body();
				
				System.out.println("body: " + body);
				String splitChar = "&";
				String keyValueSplitter = "=";
				String[] params = body.split(splitChar);
				
				String userUtterance = "noneSaid";
				
				for (int i=0; i < params.length; i++){
					
					String[] sv = params[i].split(keyValueSplitter);
					
					if (sv[0].equals("userUtterance")){
						if (sv.length > 0){
							userUtterance = sv[1];
						} else {
							userUtterance = "";
						}
						userUtterance = userUtterance.replaceAll("%20", " ");
						userUtterance = userUtterance.replaceAll("%3A", ":");
					}
					
				}	
				
				if (!userUtterance.equals("noneSaid")){
				
					System.out.println("Main: User says:" + userUtterance);	
					
					JsonObject userInput = new JsonObject();
					userInput.add("userUtterance", new JsonPrimitive(userUtterance));
					
					JsonObject botResponse = null;
					try {
						botResponse = bot.process(userInput);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Main: Bot says:" + botResponse);	
					if (botResponse != null) {
						return botResponse;
					}
				}
				else {
					return null;
				}
				response.status(400);
				return new ResponseError("Error! POST not handled.");
			}
			
		});
		
		after((req, res) -> {
			res.type("application/json");
		});

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
		//	res.body(toJson(new ResponseError(e)));
		});
	}
	
	static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
            
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
