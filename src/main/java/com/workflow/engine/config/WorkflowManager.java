package com.workflow.engine.config;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.workflow.engine.dto.FlowNodeDto;
import com.workflow.engine.dto.WorkflowDto;
import com.workflow.engine.dto.WorkflowsDto;
import com.workflow.engine.service.WorkflowService;
import com.workflow.engine.util.WorkflowUtil;

@Component
public class WorkflowManager {
	
	@Autowired
	public WorkflowService workflowService;

	@Value("${synthesis.ruleengine.workflow.engine.workflowpath}")
	private String workflowPath;

	@Autowired
	private ApplicationContext context;


	private ConcurrentHashMap<String,Workflow> workflows = new ConcurrentHashMap<>();

	public final String executeWorkflow(String flowId,Map<String,Object> initialRequest) {

		Workflow workflow = workflows.get(flowId);
		String threadInstanceCode = "Thread_"+Instant.now().toEpochMilli();
		Thread flowThread = new Thread(() -> workflow.executeWorkflow(initialRequest,threadInstanceCode));
		flowThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() 
		{

			@Override
			public void uncaughtException(Thread th, Throwable e) 
			{
				// TODO set the uncaught exception details
			}

		});
		flowThread.setName(threadInstanceCode);
		workflowService.saveWorkflowInstanceStatus(flowThread,workflow);
		flowThread.start();
		return String.valueOf(flowThread.getName());
	}


	@EventListener
	public void onAppReady(ApplicationReadyEvent event) {
		workflowService.saveAllWorkflow(workflows);
	}


	@PostConstruct
	public void initialize()
	{
		WorkflowsDto flows = WorkflowUtil.getWorkFlowDetails(Paths.get(workflowPath));
		Optional.ofNullable(flows.getWorkflows()).ifPresent(fls -> fls.stream().forEach(flw -> populateWorkflows(flw)));
	}


	private void populateWorkflows(WorkflowDto flow) {

		Workflow work = context.getBean(Workflow.class);
		work.setFlowDescription(flow.getFlowDescription());
		work.setFlowId(flow.getFlowId());

		Map<String, WorkflowNode> nodesMap = work.getNodes();
		long decisionNodeCount = 0l;
		if(Optional.ofNullable(flow.getFlowNodes()).isPresent())
		{
			for(FlowNodeDto node : flow.getFlowNodes())
			{
				WorkflowNode wNode = new WorkflowNode();
				wNode.setNodeDescription(node.getNodeDescription());
				wNode.setNodeId(node.getJobName());
				wNode.setType(WorkflowUtil.getNodeType(node.getType()));
				wNode.setAction((WorkflowAction)context.getBean(node.getJobName()));
				wNode.setPosition(WorkflowUtil.getPosition(node.getPosition()));

				//check start node
				if(node.getPosition()!=null && NodePosition.START.toString().equalsIgnoreCase(node.getPosition().toString()))
				{
					work.setStartNode(node.getJobName());
				}
				//check end node
				else if(node.getPosition()!=null && NodePosition.END.toString().equalsIgnoreCase(node.getPosition().toString()))
				{
					work.setEndNode(node.getJobName());
				}
				//For normal exit put the nodes name
				if(NodeType.PROCESS.toString().equalsIgnoreCase(node.getType().toString()))
				{
					wNode.getNextNodes().put(ExitType.NORMAL, node.getNormalExit());
				}
				else if(NodeType.DECISION.toString().equalsIgnoreCase(node.getType().toString()))
				{
					decisionNodeCount = decisionNodeCount+1l;
					wNode.setDecisionTask((DecisionAction)context.getBean(node.getDecisionTask()));
					wNode.getNextNodes().put(ExitType.IF_CONDITION_TRUE, node.getConditionalTrue());
					wNode.getNextNodes().put(ExitType.ELSE_CONDITION_TRUE, node.getConditionalFalse());
				}
				nodesMap.put(node.getJobName(),wNode);
			}
		}
		work.setTotalSteps(Long.valueOf(String.valueOf(nodesMap.size())) - Math.round((Math.pow(2, decisionNodeCount) - 1)));
		workflows.put(flow.getFlowId(), work);
	}
}
