package com.syra.workflow.engine.config;

import java.util.Map;

public interface FlowExecutioner {

	void executeWorkflow(Map<String, Object> initialRequest);
}
