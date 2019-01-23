package com.workflow.engine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.engine.config.WorkflowManager;



@RestController
@RequestMapping(value = "/workflow")
public class TestController {
	
	@Autowired 
	private WorkflowManager mgr;
	
	
	@CrossOrigin
	@RequestMapping(value = "/getFlow/{flowId}",  method = RequestMethod.GET)
	public ResponseEntity<String> fetchProgressChart(HttpServletRequest httpServletRequest,
			@PathVariable(required=true,name="flowId") String flowId)
	{
		String msg = mgr.executeWorkflow(flowId, null);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

}
