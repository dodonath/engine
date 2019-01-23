package com.workflow.engine.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.workflow.engine.entity.WorkflowInstanceStatus;

public interface WorkflowInstanceStatusRepository extends CrudRepository<WorkflowInstanceStatus, Long>,JpaRepository<WorkflowInstanceStatus, Long>,PagingAndSortingRepository<WorkflowInstanceStatus, Long>{

	WorkflowInstanceStatus findByInstanceCode(String threadInstanceCode);


}
