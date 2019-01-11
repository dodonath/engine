package com.synthesis.ruleengine.workflow.engine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synthesis.ruleengine.workflow.engine.config.WorkflowManager;

@RestController
@RequestMapping(value = "/workflowEngine")
public class WorkflowController {

	@Autowired
	private WorkflowManager workflowMgr;


	@CrossOrigin
	@RequestMapping(value = "/runWorkflow/{workflowId}",  method = RequestMethod.GET)
	public ResponseEntity<String> fetchProgressChart(
			HttpServletRequest httpServletRequest,
			@PathVariable(required= true,value="workflowId") String workflowId ) 
	{
		workflowMgr.executeWorkflow(workflowId, null);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

}
