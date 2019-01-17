package com.syra.workflow.engine.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.syra.workflow.engine.entity.WorkflowMaster;


@Transactional(transactionManager = "workflowSqlEntityManager",propagation = Propagation.REQUIRES_NEW)
public interface WorkflowRepository extends CrudRepository<WorkflowMaster, Long>,JpaRepository<WorkflowMaster, Long>,PagingAndSortingRepository<WorkflowMaster, Long>{

}
