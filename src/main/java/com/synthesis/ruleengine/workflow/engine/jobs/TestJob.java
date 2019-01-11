package com.synthesis.ruleengine.workflow.engine.jobs;

import org.springframework.stereotype.Component;

import com.synthesis.ruleengine.workflow.engine.config.ExitType;
import com.synthesis.ruleengine.workflow.engine.config.WorkflowAction;
import com.synthesis.ruleengine.workflow.engine.config.WorkflowContext;

@Component("TestJob")
public class TestJob implements WorkflowAction{

	@Override
	public void actionToBeTaken(WorkflowContext context) {
		
		//Object object = context.getData("value1");
		
		//This is for testing purpose only
		System.out.println("Processing "+this.getClass().getSimpleName());
		//End
		
		context.putData("value2", new Integer(2));
	}

}
