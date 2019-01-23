package com.workflow.engine.jobs;

import org.springframework.stereotype.Component;

import com.workflow.engine.config.WorkflowAction;
import com.workflow.engine.config.WorkflowContext;

@Component("TestJob3")
public class TestJob3 implements WorkflowAction{

	@Override
	public void actionToBeTaken(WorkflowContext context) {
		System.out.println("Processing "+this.getClass().getSimpleName());
		throw new RuntimeException("Error stack demo");
	}

}
