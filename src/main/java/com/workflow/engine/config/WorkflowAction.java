package com.workflow.engine.config;

public interface WorkflowAction {
	
	static void preProcess(WorkflowContext context, WorkflowNode node) {
		//TODO add start steps
	}
	
	static void postProcess(WorkflowContext context, WorkflowNode node) {
		//TODO add end steps
	}
	
	default void executeProcess(WorkflowContext context, WorkflowNode node) {
		preProcess(context,node);
		actionToBeTaken(context);
		postProcess(context,node);
	}

	public void actionToBeTaken(WorkflowContext context);

	
}
