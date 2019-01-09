package com.synthesis.ruleengine.workflow.engine.config;

import java.util.HashMap;
import java.util.Map;



public class Workflow implements FlowExecutioner{

	private ThreadLocal<WorkflowContext> threadContext = new ThreadLocal<>();

	private String startNode;

	private Map<String,WorkflowNode> nodes = new HashMap<>();

	private String flowId;

	private String flowDescription;


	public Map<String, WorkflowNode> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, WorkflowNode> nodes) {
		this.nodes = nodes;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowDescription() {
		return flowDescription;
	}

	public void setFlowDescription(String flowDescription) {
		this.flowDescription = flowDescription;
	}

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	@Override
	public void executeWorkflow(Map<String, Object> initialRequest) {
		setInitialContext(initialRequest);
		execute();
	}

	private void execute() {
		
		WorkflowContext context = threadContext.get();
		
		WorkflowNode node = nodes.get(startNode);
		if(node != null)
		{
			WorkflowAction action = null;
			while(node.getNodePosition()!= NodePosition.END)
			{
				action = node.getAction();
				ExitType exit = action.executeProcess(context);
				if(node.getType() == NodeType.SIMPLE)
				{
					node = node.getNextNodes().get(ExitType.NORMAL);
				}
				else if(node.getType() == NodeType.CONDITIONAL)
				{
					node = node.getNextNodes().get(exit);
				}
				else
				{
					//Iterative to be done.Need clue 
				}
					
			}
		}
		
		
	}

	private void setInitialContext(Map<String, Object> initialRequest) 
	{
		//This is for setting the context
		WorkflowContext context = new WorkflowContext();
		if(initialRequest != null && !initialRequest.isEmpty())
		{
			context.putAllData(initialRequest);
		}
		threadContext.set(context);

	}





}
