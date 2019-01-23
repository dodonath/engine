package com.workflow.engine.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.workflow.engine.config.Workflow;
import com.workflow.engine.config.WorkflowContext;
import com.workflow.engine.config.WorkflowNode;
import com.workflow.engine.config.WorkflowStatus;
import com.workflow.engine.dao.InstanceDrillDownRepository;
import com.workflow.engine.dao.WorkflowInstanceStatusRepository;
import com.workflow.engine.dao.WorkflowRepository;
import com.workflow.engine.entity.InstanceDrillDown;
import com.workflow.engine.entity.WorkflowInstanceStatus;
import com.workflow.engine.entity.WorkflowMaster;
import com.workflow.engine.util.WorkflowConstant;

@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private WorkflowInstanceStatusRepository workflowInstanceStatusRepository;

	@Autowired
	private InstanceDrillDownRepository instanceDrillDownRepository;


	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",readOnly=true)
	public List<WorkflowMaster> getAllflows()
	{
		return workflowRepository.findAll();
	}

	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",propagation = Propagation.REQUIRES_NEW)
	public void saveAllWorkflow(Map<String,Workflow> workflows) 
	{
		List<WorkflowMaster> toBeSaved = new ArrayList<>();
		Long timestamp = Instant.now().toEpochMilli();
		Workflow temp = null;
		try
		{
			List<WorkflowMaster> flows = getAllflows();
			if(flows!=null && !flows.isEmpty())
			{
				for(WorkflowMaster entry : flows)
				{
					temp = workflows.get(entry.getWorkflowCode());
					if(temp!=null)
					{
						checkAndUpdateValue(temp,entry,timestamp,toBeSaved);
					}
					else
					{
						toBeSaved.add(populateIndividual(temp,timestamp));
					}
				}
			}
			else
			{
				workflows.entrySet().parallelStream().forEach(entry -> toBeSaved.add(populateIndividual(entry.getValue(),timestamp)));
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

	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",propagation = Propagation.REQUIRES_NEW)
	public void saveWorkflowInstanceStatus(Thread flowThread, Workflow workflow) {
		Long timestamp = Instant.now().toEpochMilli();
		WorkflowMaster statusMaster = workflowRepository.findByWorkflowCode(workflow.getFlowId());
		WorkflowInstanceStatus status = new WorkflowInstanceStatus();
		status.setPercentCompletion(0.0);
		status.setStartedAt(timestamp);
		status.setActive(Boolean.TRUE);
		status.setInstanceCode(flowThread.getName());
		status.setOverAllStatus(WorkflowStatus.STARTED.toString());
		status.setWorkflow(statusMaster);
		status.setCurrentStep(workflow.getStartNode());
		workflowInstanceStatusRepository.saveAndFlush(status);
	}

	@Override
	public void performTask(WorkflowNode node, WorkflowContext context, String threadInstanceCode,double completion) throws Exception 
	{

		Long drilldownId = performBeforeTask(node,threadInstanceCode);
		try
		{
			node.getAction().executeProcess(context,node);
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			performErrorTask(node,threadInstanceCode,sw.toString(),drilldownId,completion);
			throw new Exception(e);
		}
		performAfterTask(node,threadInstanceCode,drilldownId,completion);
	}


	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",propagation = Propagation.REQUIRES_NEW)
	public Long performAfterTask(WorkflowNode node, String threadInstanceCode, Long drilldownId, double completion) {
		Long endedAt = Instant.now().toEpochMilli();

		WorkflowInstanceStatus instance = workflowInstanceStatusRepository.findByInstanceCode(threadInstanceCode);
		instance.setCurrentStep(node.getNodeId());
		instance.setEndedAt(endedAt);
		instance.setPercentCompletion(completion);
		if(completion == 100.0)
		{
			instance.setOverAllStatus(WorkflowStatus.COMPLETED_WITH_SUCCESS.toString());
		}
		
		instance = workflowInstanceStatusRepository.saveAndFlush(instance);


		InstanceDrillDown drilldown = instanceDrillDownRepository.findByDrilldownId(drilldownId);
		drilldown.setEndedAt(endedAt);
		drilldown.setRunStatus(WorkflowStatus.COMPLETED_WITH_SUCCESS.toString());
		drilldown.setStatus(instance);
		drilldown = instanceDrillDownRepository.saveAndFlush(drilldown);
		return drilldown.getDrilldownId();
	}

	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",propagation = Propagation.REQUIRES_NEW)
	public Long performErrorTask(WorkflowNode node, String threadInstanceCode, String errorStack, Long drilldownId, double completion) {
		Long endedAt = Instant.now().toEpochMilli();

		WorkflowInstanceStatus instance = workflowInstanceStatusRepository.findByInstanceCode(threadInstanceCode);
		instance.setCurrentStep(node.getNodeId());
		instance.setOverAllStatus(WorkflowStatus.COMPLETED_WITH_ERROR.toString());
		instance.setEndedAt(endedAt);
		instance.setPercentCompletion(completion);
		instance.setErrorStack(errorStack);
		instance = workflowInstanceStatusRepository.saveAndFlush(instance);


		InstanceDrillDown drilldown = instanceDrillDownRepository.findByDrilldownId(drilldownId);
		drilldown.setEndedAt(endedAt);
		drilldown.setRunStatus(WorkflowStatus.COMPLETED_WITH_ERROR.toString());
		drilldown.setStatus(instance);
		drilldown.setErrorStack(errorStack);
		drilldown = instanceDrillDownRepository.saveAndFlush(drilldown);
		return drilldown.getDrilldownId();

	}

	@Override
	@Transactional(transactionManager = "workflowSqlTransactionManager",propagation = Propagation.REQUIRES_NEW)
	public Long performBeforeTask(WorkflowNode node, String threadInstanceCode) {
		WorkflowInstanceStatus instance = workflowInstanceStatusRepository.findByInstanceCode(threadInstanceCode);
		instance.setCurrentStep(node.getNodeId());
		instance.setOverAllStatus(WorkflowStatus.RUNNING.toString());
		instance = workflowInstanceStatusRepository.saveAndFlush(instance);
		//TODO calculate the percentage


		InstanceDrillDown drilldown = new InstanceDrillDown();
		drilldown.setActive(Boolean.TRUE);
		drilldown.setStatus(instance);
		drilldown.setStartedAt(Instant.now().toEpochMilli());
		drilldown.setRunStatus(WorkflowStatus.STARTED.toString());
		drilldown.setStepName(node.getNodeId());
		drilldown.setStatus(instance);
		drilldown  = instanceDrillDownRepository.saveAndFlush(drilldown);
		return drilldown.getDrilldownId();

	}

}
