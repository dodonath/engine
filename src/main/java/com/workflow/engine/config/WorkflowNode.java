package com.workflow.engine.config;

import java.util.EnumMap;
import java.util.Map;

public class WorkflowNode {
	
	private WorkflowAction action;
	
	private NodeType type;
	
	private String nodeId;
	
	private String nodeDescription;
	
	private NodePosition position;
	
	private Map<ExitType,String> nextNodes = new EnumMap<>(ExitType.class);
	
	private DecisionAction decisionTask;

	public WorkflowAction getAction() {
		return action;
	}

	public void setAction(WorkflowAction action) {
		this.action = action;
	}


	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeDescription() {
		return nodeDescription;
	}

	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	public NodePosition getPosition() {
		return position;
	}

	public void setPosition(NodePosition position) {
		this.position = position;
	}

	public Map<ExitType, String> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Map<ExitType, String> nextNodes) {
		this.nextNodes = nextNodes;
	}

	public DecisionAction getDecisionTask() {
		return decisionTask;
	}

	public void setDecisionTask(DecisionAction decisionTask) {
		this.decisionTask = decisionTask;
	}
	
	
	
	
	


}
