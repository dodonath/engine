package com.workflow.engine.decision;

import org.springframework.stereotype.Component;

import com.workflow.engine.config.DecisionAction;
import com.workflow.engine.config.WorkflowContext;

@Component("DecisionA")
public class DecisionA implements DecisionAction{

	@Override
	public boolean getDecision(WorkflowContext context) {
		
		Integer value = (Integer) context.getData("value2");
		return value.intValue()==2? true : false;
	}

}
