package com.amazons3utils.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazons3utils.entities.ResquestBody;
import com.amazons3utils.interfaces.services.IBusinessService;

@RestController
public class s3UtilsController {

	@Autowired
	private IBusinessService businessService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String Get() {
		return "Is Working!!  API AWS S3 utils";
	}

	@RequestMapping(value = "/utils/url", method = RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<String> Get(@RequestBody ResquestBody request) {
		
		try {
			String result = businessService.getGlosaFile(request.file);
			return new ResponseEntity<>(result, HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
