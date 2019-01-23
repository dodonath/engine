package com.workflow.engine.config;

import java.util.HashMap;
import java.util.Map;

public class WorkflowContext {
	
	private Map<String,Object> data = new HashMap<>();
	
	public Object getData(String key)
	{
		return data.get(key);
	}

	
	public void putData(String key,Object value)
	{
		data.put(key,value);
	}
	
	public void putAllData(Map<String,Object> values)
	{
		data.putAll(values);
	}
	
	
	

}
