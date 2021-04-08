package com.foodie.webservice;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JSONUtil {

	public static String toJSON(Object object)
	{
		return new Gson().toJson(object);
	}
	public static ResponseTransformer json()
	{
		return JSONUtil::toJSON;
	}
}
