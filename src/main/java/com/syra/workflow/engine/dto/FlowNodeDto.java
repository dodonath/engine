
package com.syra.workflow.engine.dto;

public class FlowNodeDto {

   
    private String type;
   
    private String jobName;
   
    private String nodeDescription;
    
    private String position;
   
    private String normalExit;
    
    private String conditionalTrue;
   
    private String conditionalFalse;
    
    private String decisionTask;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public String getConditionalTrue() {
        return conditionalTrue;
    }

    public void setConditionalTrue(String conditionalTrue) {
        this.conditionalTrue = conditionalTrue;
    }

    public String getConditionalFalse() {
        return conditionalFalse;
    }

    public void setConditionalFalse(String conditionalFalse) {
        this.conditionalFalse = conditionalFalse;
    }

	public String getDecisionTask() {
		return decisionTask;
	}

	public void setDecisionTask(String decisionTask) {
		this.decisionTask = decisionTask;
	}

	public String getNormalExit() {
		return normalExit;
	}

	public void setNormalExit(String normalExit) {
		this.normalExit = normalExit;
	}
    
    

}
