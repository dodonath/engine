package com.workflow.engine.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.workflow.engine.entity.InstanceDrillDown;

public interface InstanceDrillDownRepository extends CrudRepository<InstanceDrillDown, Long>,JpaRepository<InstanceDrillDown, Long>,PagingAndSortingRepository<InstanceDrillDown, Long>{

	InstanceDrillDown findByDrilldownId(Long drilldownId);


}
