package com.syra.workflow.engine.jobs;

import org.springframework.stereotype.Component;

import com.syra.workflow.engine.config.WorkflowAction;
import com.syra.workflow.engine.config.WorkflowContext;

@Component("TestJob3")
public class TestJob3 implements WorkflowAction{

	@Override
	public void actionToBeTaken(WorkflowContext context) {
		System.out.println("Processing "+this.getClass().getSimpleName());
	}

}
