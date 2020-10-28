package com.randoli.src.app.controller;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	@Autowired
	ProducerTemplate producerTemplate;

	@RequestMapping(value = "/root", method = RequestMethod.GET)
	public String root() {
		return "root";
	}
}