package com.randoli.src.app.controller;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.randoli.src.app.model.Event;

@RestController
public class EventController {

	@Autowired
	ProducerTemplate producerTemplate;

	@RequestMapping(value = "/event", consumes = "application/json", method = RequestMethod.POST)
	public boolean insertEvent(@RequestBody Event event) {
		event.generateEventId();
		producerTemplate.requestBody("direct:insert", event, List.class);
		return true;
	}

	@RequestMapping(value = "/event", method = RequestMethod.GET)
	public List<Event> getAllEvents() {
		List<Event> events = producerTemplate.requestBody("direct:select", null, List.class);
		return events;
	}

	
	@RequestMapping(value = "/event", consumes = "application/json", method = RequestMethod.PUT)
	public boolean updateEvent(@RequestBody Event event) {
		producerTemplate.requestBody("direct:update", event, List.class);
		return true;
	}
	
	@RequestMapping(value = "/event/{eventId}", method = RequestMethod.DELETE)
	public boolean updateEvent(@PathVariable String eventId) {
		producerTemplate.requestBody("direct:delete", eventId, List.class);
		return true;
	}
}