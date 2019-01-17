package com.syra.workflow.engine.config;

import java.util.HashMap;
import java.util.Map;

import com.syra.workflow.engine.util.WorkflowUtil;



public class Workflow implements FlowExecutioner{

	private ThreadLocal<WorkflowContext> threadContext = new ThreadLocal<>();

	private String startNode;
	
	private String endNode;

	private Map<String,WorkflowNode> nodes = new HashMap<>();
	
	private Long totalSteps;

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
		WorkflowNode endTask = nodes.get(endNode);
		if(node != null)
		{
			WorkflowAction action = null;
			while(!node.getNodeId().equals(endTask.getNodeId()))
			{
				action = node.getAction();
				action.executeProcess(context);
				if(node.getType() == NodeType.PROCESS)
				{
					node = nodes.get(node.getNextNodes().get(ExitType.NORMAL));
				}
				else if(node.getType() == NodeType.DECISION)
				{
					boolean condition = node.getDecisionTask().getDecision(context);
					node = nodes.get(node.getNextNodes().get(WorkflowUtil.getDecisionType(condition)));
				}
				else
				{
					//Iterative to be done.Need clue 
				}
			}
			
			
			//Node task has ended 
			//Execute last task
			performLastTask(node,context);
			threadContext.remove();
			
		}
		
		
	}

	private void performLastTask(WorkflowNode node, WorkflowContext context) {
		
		node.getAction().executeProcess(context);
		//TODO perform additional task for ending the project.
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

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public Long getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(Long totalSteps) {
		this.totalSteps = totalSteps;
	}

	



}
