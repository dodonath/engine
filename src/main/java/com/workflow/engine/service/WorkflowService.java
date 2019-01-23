package com.workflow.engine.service;

import java.util.List;
import java.util.Map;

import com.workflow.engine.config.Workflow;
import com.workflow.engine.config.WorkflowContext;
import com.workflow.engine.config.WorkflowNode;
import com.workflow.engine.entity.WorkflowMaster;

public interface WorkflowService {

	List<WorkflowMaster> getAllflows();

	void saveAllWorkflow(Map<String, Workflow> workflows);

	void saveWorkflowInstanceStatus(Thread flowThread, Workflow workflow);

	void performTask(WorkflowNode node, WorkflowContext context, String threadInstanceCode, double completion) throws Exception;

	Long performBeforeTask(WorkflowNode node, String threadInstanceCode);

	Long performErrorTask(WorkflowNode node, String threadInstanceCode, String errorStack, Long drilldownId,
			double completion);

	Long performAfterTask(WorkflowNode node, String threadInstanceCode, Long drilldownId, double completion);

}
