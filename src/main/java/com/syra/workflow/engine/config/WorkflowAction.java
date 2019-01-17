package com.syra.workflow.engine.config;

public interface WorkflowAction {
	
	default void preProcess(WorkflowContext context) {}
	
	default void postProcess(WorkflowContext context) {}
	
	default void executeProcess(WorkflowContext context) {
		preProcess(context);
		actionToBeTaken(context);
		postProcess(context);
	}

	public void actionToBeTaken(WorkflowContext context);

	
}
