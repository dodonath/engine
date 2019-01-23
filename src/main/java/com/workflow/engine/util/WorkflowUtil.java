package com.workflow.engine.util;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.config.ExitType;
import com.workflow.engine.config.NodePosition;
import com.workflow.engine.config.NodeType;
import com.workflow.engine.dto.WorkflowsDto;

public class WorkflowUtil {
	
	private static final Map<String,ExitType> exitMap;
	private static final Map<String,NodeType> nodeTypeMap;
	private static final Map<String,NodePosition> nodePositionMap;
	
	static
	{
		exitMap = Arrays.stream(ExitType.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
		nodeTypeMap = Arrays.stream(NodeType.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
		nodePositionMap = Arrays.stream(NodePosition.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
	}
	

	public static NodePosition getPosition(String positionKey) {
		return Optional.ofNullable(positionKey).map(k -> nodePositionMap.get(k)).orElse(null);
	}


	public static NodeType getNodeType(String typeKey) {
		return Optional.ofNullable(typeKey).map(k -> nodeTypeMap.get(k)).orElse(null);
	}

	public static ExitType getExitType(String exitKey)
	{
		return Optional.ofNullable(exitKey).map(k -> exitMap.get(k)).orElse(null);
	}
	
	public static WorkflowsDto getWorkFlowDetails(Path path) {
		try 
		{
			byte[] content = Files.readAllBytes(path);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,Boolean.FALSE);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,Boolean.TRUE);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,Boolean.TRUE);
			return mapper.readValue(content, WorkflowsDto.class);
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	public static ExitType getDecisionType(boolean val) {
		if(val)
		{
			return ExitType.IF_CONDITION_TRUE;
		}
		return ExitType.ELSE_CONDITION_TRUE;
	}
	
	public static <M,T> Map<M,T> populateListToMapWithFieldNameKey(List<T> list,String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Map<M,T> map = new HashMap<>();
		String getter = "get"+StringUtils.capitalize(fieldName);

		if(list==null || list.isEmpty()) return map;
		{
			for(T value : list)
			{
				M key = (M) value.getClass().getMethod(getter, null).invoke(value, null);
				if(key!=null)
				{
					map.put(key, value);
				}
			}
		}
		return map;
	}

	public static <M,T> Map<M,List<T>> populateListToMapListWithFieldNameKey(List<T> list,String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Map<M,List<T>> map = new HashMap<>();
		String getter = "get"+StringUtils.capitalize(fieldName);

		if(list==null || list.isEmpty()) return map;
		{
			List<T> newList = null;
			for(T value : list)
			{
				M key = (M) value.getClass().getMethod(getter, null).invoke(value, null);
				newList = map.get(key);
				if(null == newList)
				{
					newList = new ArrayList<>();
				}
				newList.add(value);
				map.put(key, newList);
			}
		}
		return map;
	}
	
	
	
	
	
	
	
}
