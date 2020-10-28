package com.randoli.src.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.randoli.src.app.model.Event;

@Service
public class EventServiceImpl extends RouteBuilder{

	@Autowired
	DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	//define the SQL Component bean which will be used as an endpoint in our route
	@Bean
	public SqlComponent sql(DataSource dataSource) {
		SqlComponent sql = new SqlComponent();
		sql.setDataSource(dataSource);
		return sql;
	}

	@Override
	public void configure() throws Exception {
        
		//Create - C
		//Insert Route
		from("direct:insert").log("Processing message: ").setHeader("message", body()).process(new Processor() {
			public void process(Exchange xchg) throws Exception {
				Event event = xchg.getIn().getBody(Event.class);
				Map<String, Object> eventMap = new HashMap<String, Object>();
				eventMap.put("eventId", event.getEventId());
				eventMap.put("transId", event.getTransId());
				eventMap.put("transTms", event.getTransTms());
				eventMap.put("rcNum", event.getRcNum());
				eventMap.put("clientId", event.getClientId());
				eventMap.put("eventCnt", event.getEventCnt());
				eventMap.put("locationCd", event.getLocationCd());
				eventMap.put("locationId1", event.getLocationId1());
				eventMap.put("locationId2", event.getLocationId2());
				eventMap.put("addrNbr", event.getAddrNbr());
				xchg.getIn().setBody(eventMap);
			}
		}).to("sql:INSERT INTO event(eventId, transId, transTms, rcNum, clientId, eventCnt, locationCd, locationId1, locationId2, addrNbr) VALUES (:#eventId, :#transId, :#transTms, :#rcNum, :#clientId, :#eventCnt, :#locationCd, :#locationId1, :#locationId2, :#addrNbr)");
        
		//Read - R
		// Select Route
        from("direct:select").to("sql:select * from event").process(new Processor() {
			public void process(Exchange xchg) throws Exception {
				ArrayList<Map<String, String>> dataList = (ArrayList<Map<String, String>>) xchg.getIn().getBody();
				List<Event> events = new ArrayList<Event>();
				System.out.println(dataList);
				for (Map<String, String> data : dataList) {
					Event event = new Event();
					event.setEventId(data.get("eventId"));
					event.setTransId(data.get("transId"));
					event.setTransTms(data.get("transTms"));
					event.setRcNum(data.get("rcNum"));
					event.setClientId(data.get("clientId"));
					event.setEventCnt(Integer.parseInt(data.get("eventCnt")));
					event.setLocationCd(data.get("locationCd"));
					event.setLocationId1(data.get("locationId1"));
					event.setLocationId2(data.get("locationId2"));
					event.setAddrNbr(data.get("addrNbr"));
					events.add(event);
				}
				xchg.getIn().setBody(events);
			}
		});
		
        //Update - U
	    //Update Route
	    from("direct:update").log("Processing message: ").setHeader("message", body()).process(new Processor() {
	    	public void process(Exchange xchg) throws Exception {
	      		Event event = xchg.getIn().getBody(Event.class);
	      		Map<String, Object> eventMap = new HashMap<String, Object>();
	       		eventMap.put("eventId", event.getEventId());
	      		eventMap.put("transId", event.getTransId());
	      		eventMap.put("transTms", event.getTransTms());
	      		eventMap.put("rcNum", event.getRcNum());
	      		eventMap.put("clientId", event.getClientId());
	      		eventMap.put("eventCnt", event.getEventCnt());
	      		eventMap.put("locationCd", event.getLocationCd());
	      		eventMap.put("locationId1", event.getLocationId1());
	      		eventMap.put("locationId2", event.getLocationId2());
	      		eventMap.put("addrNbr", event.getAddrNbr());
	      		xchg.getIn().setBody(eventMap);
	      	}
	    }).to("sql:UPDATE event set transId = :#transId, transTms = :#transTms, rcNum = :#rcNum, clientId = :#clientId, eventCnt = :#eventCnt, locationCd = :#locationCd, locationId1 = :#locationId1, locationId2 = :#locationId2, addrNbr = :#addrNbr WHERE eventId = :#eventId");   
        
	    //Delete - D
	    //Delete Route
	    from("direct:delete").log("Processing message: ").setHeader("message", body()).process(new Processor() {
	  		public void process(Exchange xchg) throws Exception {
	  			String eventId = xchg.getIn().getBody(String.class);
	  			Map<String, Object> eventMap = new HashMap<String, Object>();
	  			eventMap.put("eventId", eventId);
	  			xchg.getIn().setBody(eventMap);
	 		}
	  	}).to("sql:DELETE FROM event WHERE eventId = :#eventId");	    
	}
	
}
