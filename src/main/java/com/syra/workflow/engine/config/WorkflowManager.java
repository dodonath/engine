package com.syra.workflow.engine.config;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.syra.workflow.engine.dao.WorkflowRepository;
import com.syra.workflow.engine.dto.FlowNodeDto;
import com.syra.workflow.engine.dto.WorkflowDto;
import com.syra.workflow.engine.dto.WorkflowsDto;
import com.syra.workflow.engine.entity.WorkflowMaster;
import com.syra.workflow.engine.util.WorkflowConstant;
import com.syra.workflow.engine.util.WorkflowUtil;

@Component
public class WorkflowManager {

	@Autowired
	private WorkflowRepository workflowRepository;


	@Value("${synthesis.ruleengine.workflow.engine.workflowpath}")
	private String workflowPath;

	@Autowired
	private ApplicationContext context;


	private ConcurrentHashMap<String,Workflow> workflows = new ConcurrentHashMap<>();

	public final String executeWorkflow(String flowId,Map<String,Object> initialRequest) {

		Workflow workflow = workflows.get(flowId);
		workflow.executeWorkflow(initialRequest);
		return Thread.currentThread().getName();
	}


	@EventListener
	public void onAppReady(ApplicationReadyEvent event) {

		List<WorkflowMaster> toBeSaved = new ArrayList<>();
		Long timestamp = Instant.now().toEpochMilli();
		Workflow temp = null;
		try
		{
			List<WorkflowMaster> flows = workflowRepository.findAll();
			if(flows!=null && !flows.isEmpty())
			{
				Map<String,WorkflowMaster> map = WorkflowUtil.populateListToMapWithFieldNameKey(flows, "workflowCode");
				for(Map.Entry<String,WorkflowMaster> entry : map.entrySet())
				{
					temp = workflows.get(entry.getKey());
					if(temp!=null)
					{
						checkAndUpdateValue(temp,entry.getValue(),timestamp,toBeSaved);
					}
					else
					{
						toBeSaved.add(populateIndividual(temp,timestamp));
					}
				}
			}
			else
			{
				populateAll(toBeSaved,timestamp);
			}

			Optional.ofNullable(toBeSaved).ifPresent(values -> values.stream().forEach(value -> {
				workflowRepository.saveAndFlush(value);
			}));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



	private void checkAndUpdateValue(Workflow source, WorkflowMaster target, Long timestamp, List<WorkflowMaster> toBeSaved) 
	{
		if(target.getTotalSteps().longValue() != source.getTotalSteps().longValue())
		{
			target.setTotalSteps(source.getTotalSteps());
			target.setUpdatedAt(timestamp);
			target.setUpdatedBy(WorkflowConstant.ADMIN);
			toBeSaved.add(target);
		}
	}


	private void populateAll(List<WorkflowMaster> toBeSaved, Long timestamp) {
		workflows.entrySet().parallelStream().forEach(entry -> toBeSaved.add(populateIndividual(entry.getValue(),timestamp)));
	}


	private WorkflowMaster populateIndividual(Workflow flow, Long timestamp) {
		WorkflowMaster master = new WorkflowMaster();
		master.setWorkflowCode(flow.getFlowId());
		master.setWorkflowDescription(flow.getFlowDescription());
		master.setTotalSteps(flow.getTotalSteps());
		master.setActive(Boolean.TRUE);
		master.setCreatedAt(timestamp);
		master.setCreatedBy(WorkflowConstant.ADMIN);
		master.setUpdatedAt(timestamp);
		master.setUpdatedBy(WorkflowConstant.ADMIN);
		return master;
	}


	@PostConstruct
	public void initialize()
	{
		WorkflowsDto flows = WorkflowUtil.getWorkFlowDetails(Paths.get(workflowPath));
		Optional.ofNullable(flows.getWorkflows()).ifPresent(fls -> fls.stream().forEach(flw -> populateWorkflows(flw)));
	}


	private void populateWorkflows(WorkflowDto flow) {

		Workflow work = new Workflow();
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
