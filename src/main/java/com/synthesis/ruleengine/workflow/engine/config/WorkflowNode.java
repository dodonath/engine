package com.synthesis.ruleengine.workflow.engine.config;

import java.util.HashMap;
import java.util.Map;

public class WorkflowNode {
	
	private WorkflowAction action;
	
	private NodePosition nodePosition;
	
	private NodeType type;
	
	private String nodeId;
	
	private Map<ExitType,WorkflowNode> nextNodes = new HashMap<>(ExitType.values().length);

	public WorkflowAction getAction() {
		return action;
	}

	public void setAction(WorkflowAction action) {
		this.action = action;
	}

	public NodePosition getNodePosition() {
		return nodePosition;
	}

	public void setNodePosition(NodePosition nodePosition) {
		this.nodePosition = nodePosition;
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

	public Map<ExitType, WorkflowNode> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Map<ExitType, WorkflowNode> nextNodes) {
		this.nextNodes = nextNodes;
	}
	
	


}
