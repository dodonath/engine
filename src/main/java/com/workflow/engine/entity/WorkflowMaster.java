package com.workflow.engine.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="WORKFLOW_MASTER")
public class WorkflowMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1797752537696486293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="workflowId")
	private Long workflowId;
	
	@Column(name="workflowCode")
	private String workflowCode;
	
	@Column(name="workflowDescription")
	private String workflowDescription;
	
	@Column(name="totalSteps")
	private Long totalSteps; 
	
	
	@Column(name="createdBy")
	private String createdBy;
	
	@Column(name="createdAt")
	private Long createdAt;
	
	@Column(name="updatedBy")
	private String updatedBy;
	
	@Column(name="updatedAt")
	private Long updatedAt;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "active")
	private Boolean active;

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

	public String getWorkflowDescription() {
		return workflowDescription;
	}

	public void setWorkflowDescription(String workflowDescription) {
		this.workflowDescription = workflowDescription;
	}

	public Long getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(Long totalSteps) {
		this.totalSteps = totalSteps;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
}
