package com.synthesis.ruleengine.workflow.engine.config;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.synthesis.ruleengine.workflow.engine.dto.WorkflowDto;
import com.synthesis.ruleengine.workflow.engine.dto.WorkflowsDto;
import com.synthesis.ruleengine.workflow.engine.util.WorkflowUtil;

@Component
public class WorkflowManager {
	
	@Value("${synthesis.ruleengine.workflow.engine.workflowpath}")
	private String workflowPath;

	@Autowired
	private ApplicationContext context;
	
	
	private ConcurrentHashMap<String,Workflow> workflows = new ConcurrentHashMap<>();

	public final void executeWorkflow(String flowId,Map<String,Object> initialRequest) {

		Workflow workflow = workflows.get(flowId);
		workflow.executeWorkflow(initialRequest);
	}
	
	

	@PostConstruct
	public void initialize()
	{
		WorkflowsDto flows = WorkflowUtil.getWorkFlowDetails(Paths.get(workflowPath));
		Optional.ofNullable(flows.getWorkflows()).ifPresent(fls -> fls.stream().forEach(flw -> populateWorkflows(flw)));
	}


	private void populateWorkflows(WorkflowDto flow) {
		
		Workflow work = new Workflow();
		workflows.put(flow.getFlowId(), work);
		
		
		
		work.setFlowDescription(flow.getFlowDescription());
		work.setFlowId(flow.getFlowId());
		
		Map<String, WorkflowNode> nodesMap = work.getNodes();
		
		Optional.ofNullable(flow.getFlowNodes()).ifPresent(f -> f.stream().forEach(node -> {
			
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
				wNode.setDecisionTask((DecisionAction)context.getBean(node.getDecisionTask()));
				wNode.getNextNodes().put(ExitType.IF_CONDITION_TRUE, node.getConditionalTrue());
				wNode.getNextNodes().put(ExitType.ELSE_CONDITION_TRUE, node.getConditionalFalse());
			}
			
			nodesMap.put(node.getJobName(),wNode);
		}));
	}
}
