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
@Table(name="INSTANCES_DRILL_DOWN")
public class InstanceDrillDown implements Serializable	{

	/**
	 * 
	 */
	private static final long serialVersionUID = -737598148346265114L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="drilldownId")
	private Long drilldownId ;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="instanceId")
	private WorkflowInstanceStatus status;
	
	@Column(name="stepName")
	private String stepName;
	
	@Column(name="runStatus")
	private String runStatus;
	
	@Column(name="errorStack")
	private String errorStack;
	
	@Column(name="startedAt")
	private Long startedAt;
	
	@Column(name="endedAt")
	private Long endedAt;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "active")
	private Boolean active;


	public Long getDrilldownId() {
		return drilldownId;
	}


	public void setDrilldownId(Long drilldownId) {
		this.drilldownId = drilldownId;
	}

	public WorkflowInstanceStatus getStatus() {
		return status;
	}

	public void setStatus(WorkflowInstanceStatus status) {
		this.status = status;
	}


	public String getStepName() {
		return stepName;
	}


	public void setStepName(String stepName) {
		this.stepName = stepName;
	}


	public String getRunStatus() {
		return runStatus;
	}


	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}


	public String getErrorStack() {
		return errorStack;
	}


	public void setErrorStack(String errorStack) {
		this.errorStack = errorStack;
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
