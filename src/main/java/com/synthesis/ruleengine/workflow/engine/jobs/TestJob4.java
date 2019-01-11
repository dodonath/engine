package com.synthesis.ruleengine.workflow.engine.jobs;

import org.springframework.stereotype.Component;

import com.synthesis.ruleengine.workflow.engine.config.WorkflowAction;
import com.synthesis.ruleengine.workflow.engine.config.WorkflowContext;


@Component("TestJob4")
public class TestJob4 implements WorkflowAction{
	@Override
	public void actionToBeTaken(WorkflowContext context) {
		System.out.println("Processing "+this.getClass().getSimpleName());
	}
}