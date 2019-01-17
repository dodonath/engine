
package com.syra.workflow.engine.dto;

import java.util.List;

public class WorkflowDto {

    
    private String flowId;
    
    private String flowDescription;
    
    private List<FlowNodeDto> flowNodes = null;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public List<FlowNodeDto> getFlowNodes() {
        return flowNodes;
    }

    public void setFlowNodes(List<FlowNodeDto> flowNodes) {
        this.flowNodes = flowNodes;
    }

	public String getFlowDescription() {
		return flowDescription;
	}

	public void setFlowDescription(String flowDescription) {
		this.flowDescription = flowDescription;
	}

}
