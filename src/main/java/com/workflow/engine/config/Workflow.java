package com.workflow.engine.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.workflow.engine.service.WorkflowService;
import com.workflow.engine.util.WorkflowUtil;



@Component
@Scope(  value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, 
proxyMode = ScopedProxyMode.INTERFACES)
public class Workflow implements FlowExecutioner{
	
	@Autowired
	private WorkflowService workflowService;

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
	public void executeWorkflow(Map<String, Object> initialRequest, String threadInstanceCode) 
	{
		setInitialContext(initialRequest);
		try
		{
			execute(threadInstanceCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private void execute(String threadInstanceCode) throws Exception {

		WorkflowContext context = threadContext.get();
		WorkflowNode node = nodes.get(startNode);
		WorkflowNode endTask = nodes.get(endNode);
		if(node != null)
		{
			int noSteps = 1;
			while(!node.getNodeId().equals(endTask.getNodeId()))
			{
				double completion = (double)noSteps/(double)totalSteps * 100;
				
				Thread.sleep(10*1000);
				workflowService.performTask(node,context,threadInstanceCode,completion);			
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
				noSteps++;
			}
			//Execute last task
			Thread.sleep(10*1000);
			workflowService.performTask(node,context, threadInstanceCode,100.00);
			threadContext.remove();
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
