package com.farubaba.gson;

import java.util.List;

import com.farubaba.json.BaseJsonService;
import com.farubaba.json.JsonType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GsonService extends BaseJsonService {

	public static final Gson gson = new GsonBuilder().create();
	private static GsonService instance = new GsonService();
	
	public static GsonService getInstance(){
		return instance;
	}
	
	@Override
	public <E> String toJsonString(E e) {
		return gson.toJson(e);
	}

	@Override
	public <T> T fromJson(String jsonString, Class<T> classOfT) {
		return gson.fromJson(jsonString, classOfT);
	}

	@Override
	public <A> List<A> fromJsonToList(String jsonString, Class<A> classOfA) {
		if(getJsonType(jsonString).isArray()) {
			return gson.fromJson(jsonString, new TypeToken<List<A>>(){}.getType());
		}
		return null;
	}

	/**
	 * 因为每一个json库，对json的抽象都不一样。Gson是JsonElement，而fastjson则是JSON。
	 * 为了避免不必要的泛型引入，该方法并没有抽取到JsonService中，而是在JsonService的实现类中各自完成对应功能。
	 * 
	 * @param jsonString
	 * @return
	 */
	public JsonElement parseJsonElement(String jsonString) {
		if(jsonString != null){
			try{
				JsonElement jsonElement = new JsonParser().parse(jsonString);
				return jsonElement;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public JsonType getJsonType(String jsonString) {
		if(jsonString != null) {
			System.out.println(jsonString);
		}
		JsonElement jsonElement = parseJsonElement(jsonString);
		JsonType jsonType = new JsonType();
		if(jsonElement == null) {
			jsonType.setValid(false);
		}
		if(jsonElement != null) {
			if(jsonElement.isJsonArray()) {
				jsonType.setArray(true);
			}else if(jsonElement.isJsonObject()) {
				jsonType.setObject(true);
			}else if(jsonElement.isJsonPrimitive()) {
				jsonType.setPrimitive(true);
			}else if(jsonElement.isJsonNull()) {
				jsonType.setJsonNull(true);
			}
		}
		return jsonType;
	}
}
