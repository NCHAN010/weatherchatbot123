package com.foodie.model;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather {

	//api key
	static String apiKey ="56d98de2e4e349560be330e6b81924d4";
	public static void main(String[] args) throws ClientProtocolException ,IOException
	{
		(new Weather()).getWeather("2643743");
		(new Weather()).getWeatherReport("4887398", 0);

	}
	public Weather() {
		
	}
	public static JsonObject getWeather (String cityCode)throws  ClientProtocolException ,IOException {
		
		//step1: Prepare the URL
	
		String url="http://api.openweathermap.org/data/2.5/forecast?id="+cityCode+"&APPID="+apiKey;
		
		//step2: create http client
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		//step3: create a http get object and execute the url
		HttpGet httpGet= new HttpGet(url);
		HttpResponse response= httpclient.execute(httpGet);
		
		//step4: Process the result
		JsonObject json=null;
		int statusCode =response.getStatusLine().getStatusCode();
		if(statusCode ==200)
		{
			String response_string= EntityUtils.toString(response.getEntity());
			json= (new JsonParser()).parse(response_string).getAsJsonObject();
			Gson gson=new GsonBuilder().setPrettyPrinting().create();
			String prettyJson =gson.toJson(json);
			//System.out.println(prettyJson);
			
		}
		return json;
	}
	
	public static String getWeatherReport(String cityCode , Integer i )throws ClientProtocolException, IOException
	{
		JsonObject currentWeather=null;
		if(cityCode !=null)
		{
			currentWeather = getWeatherAtTime(cityCode,i);
		}
		
		String weatherReport =null;
		if(currentWeather!=null)
		{
			JsonObject weather = currentWeather.get("weather").getAsJsonArray().get(0).getAsJsonObject();
			
			Double avgTemp = Double.valueOf(currentWeather.get("main").getAsJsonObject().get("temp").getAsString())-273.15;
		
			String avgTemptSt = String.valueOf(avgTemp).split("\\.")[0];
			weatherReport = "The temperature is "+avgTemptSt+" degrees Celsius. "+weather.get("description").getAsString()+".";
			
			
		}
		System.out.println(weatherReport);
		return weatherReport;
		
	}
	
	public static JsonObject getWeatherAtTime(String cityCode, Integer i )throws ClientProtocolException, IOException
	{
		JsonObject json = getWeather(cityCode);
		JsonArray list =json.get("list").getAsJsonArray();
		JsonObject weatherAtTime= list.get(i).getAsJsonObject();
		return weatherAtTime;
	}
	public JsonObject getCurrentWeather(String cityName) {return null;}


}
