package com.syra.workflow.engine.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="WORKFLOW_INSTANCES_STATUS")
public class WorkflowInstanceStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7704755066653199885L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="instanceId")
	private Long instanceId;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="workflowId")
	private WorkflowMaster workflow;
	
	@Column(name="instanceCode")
	private String instanceCode;
	
	@Column(name="currentStep")
	private String currentStep;
	  
	@Column(name="percentCompletion")
	private Double percentCompletion ;
	
	@Column(name="overAllStatus")
	private String overAllStatus;
	
	@Column(name="startedAt")
	private Long startedAt;
	
	@Column(name="endedAt")
	private Long endedAt;
	   
	   
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "active")
	private Boolean active;


	public Long getInstanceId() {
		return instanceId;
	}


	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public WorkflowMaster getWorkflow() {
		return workflow;
	}


	public void setWorkflow(WorkflowMaster workflow) {
		this.workflow = workflow;
	}


	public String getInstanceCode() {
		return instanceCode;
	}


	public void setInstanceCode(String instanceCode) {
		this.instanceCode = instanceCode;
	}


	public String getCurrentStep() {
		return currentStep;
	}


	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}


	public Double getPercentCompletion() {
		return percentCompletion;
	}


	public void setPercentCompletion(Double percentCompletion) {
		this.percentCompletion = percentCompletion;
	}


	public String getOverAllStatus() {
		return overAllStatus;
	}


	public void setOverAllStatus(String overAllStatus) {
		this.overAllStatus = overAllStatus;
	}


	public Long getStartedAt() {
		return startedAt;
	}


	public void setStartedAt(Long startedAt) {
		this.startedAt = startedAt;
	}


	public Long getEndedAt() {
		return endedAt;
	}


	public void setEndedAt(Long endedAt) {
		this.endedAt = endedAt;
	}


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
	

}
