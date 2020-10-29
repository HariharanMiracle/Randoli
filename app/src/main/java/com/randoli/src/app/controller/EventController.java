package com.randoli.src.app.controller;

import java.util.ArrayList;
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
		try {
			event.generateEventId();
			producerTemplate.requestBody("direct:insert", event, List.class);
			return true;			
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping(value = "/event", method = RequestMethod.GET)
	public List<Event> getAllEvents() {
		try {
			List<Event> events = producerTemplate.requestBody("direct:select", null, List.class);
			return events;			
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ArrayList<Event>();
		}
	}

	
	@RequestMapping(value = "/event", consumes = "application/json", method = RequestMethod.PUT)
	public boolean updateEvent(@RequestBody Event event) {
		try {
			producerTemplate.requestBody("direct:update", event, List.class);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value = "/event/{eventId}", method = RequestMethod.DELETE)
	public boolean updateEvent(@PathVariable String eventId) {
		try {
			producerTemplate.requestBody("direct:delete", eventId, List.class);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value = "/events", consumes = "application/json", method = RequestMethod.POST)
	public boolean insertAllEvent(@RequestBody List<Event> eventList) {
		try {
			for(Event event : eventList) {
				event.generateEventId();
				producerTemplate.requestBody("direct:insert", event, List.class);
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}