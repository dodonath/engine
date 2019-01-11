package com.synthesis.ruleengine.workflow.engine.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthesis.ruleengine.workflow.engine.config.ExitType;
import com.synthesis.ruleengine.workflow.engine.config.NodePosition;
import com.synthesis.ruleengine.workflow.engine.config.NodeType;
import com.synthesis.ruleengine.workflow.engine.dto.WorkflowsDto;

public class WorkflowUtil {
	
	private static final Map<String,ExitType> exitMap;
	private static final Map<String,NodeType> nodeTypeMap;
	private static final Map<String,NodePosition> nodePosition;
	
	static
	{
		exitMap = Arrays.stream(ExitType.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
		nodeTypeMap = Arrays.stream(NodeType.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
		nodePosition = Arrays.stream(NodePosition.values()).collect(Collectors.toMap(e -> e.toString(), e -> e));
	}
	

	public static NodePosition getPosition(String positionKey) {
		return Optional.ofNullable(positionKey).map(k -> nodePosition.get(k)).orElse(null);
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
	
	
	
	
	
	
	
}
