package com.synthesis.ruleengine.workflow.engine.config;

public interface WorkflowAction {
	
	default void preProcess(WorkflowContext context) {}
	
	default void postProcess(WorkflowContext context) {}
	
	default ExitType executeProcess(WorkflowContext context) {
		preProcess(context);
		ExitType exitStrategy = actionToBeTaken(context);
		postProcess(context);
		return exitStrategy;
	}

	public ExitType actionToBeTaken(WorkflowContext context);

	
}
