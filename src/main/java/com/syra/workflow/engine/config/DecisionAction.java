package com.syra.workflow.engine.config;

public interface DecisionAction {

	public boolean getDecision(WorkflowContext context);
}
