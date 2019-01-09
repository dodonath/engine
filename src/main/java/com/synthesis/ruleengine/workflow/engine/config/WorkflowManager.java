package com.synthesis.ruleengine.workflow.engine.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class WorkflowManager {

	
	private ConcurrentHashMap<String,Workflow> workflows = new ConcurrentHashMap<>();

	public final void executeWorkflow(String flowId,Map<String,Object> initialRequest) {

		Workflow workflow = workflows.get(flowId);
		workflow.executeWorkflow(initialRequest);
	}

	@PostConstruct
	public void initialize()
	{
		//TODO parse the json
		//parse the json and create the workflows 
	}

}
