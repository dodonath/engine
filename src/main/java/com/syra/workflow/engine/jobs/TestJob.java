package com.syra.workflow.engine.jobs;

import org.springframework.stereotype.Component;

import com.syra.workflow.engine.config.WorkflowAction;
import com.syra.workflow.engine.config.WorkflowContext;

@Component("TestJob")
public class TestJob implements WorkflowAction{

	@Override
	public void actionToBeTaken(WorkflowContext context) {
		
		Object object = context.getData("value1");
		
		//This is for testing purpose only
		System.out.println("Processing "+this.getClass().getSimpleName());
		//End
		
		context.putData("value2", new Integer(2));
	}

}
