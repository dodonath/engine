package com.synthesis.ruleengine.workflow.engine.config;

public interface DecisionAction {

	public boolean getDecision(WorkflowContext context);
}
