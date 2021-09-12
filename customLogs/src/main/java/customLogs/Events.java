package customLogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.EventDetails;

public class Events {
	
	static Logger logger = LoggerFactory.getLogger("Events");
	private List<EventDetails> eventsList=new ArrayList<EventDetails>();
	
	public void addEvent(EventDetails event) {
		eventsList.add(event);
	}
	
	public void calculateEventsDuration() {
		 logger.info("Calculating durations for each event...");
		
		 for (EventDetails eventDetails : eventsList) {
			Optional<EventDetails> optional=eventsList.parallelStream().filter(e->e.getId().equals(eventDetails.getId()) && e.getState().equalsIgnoreCase("FINISHED")).findFirst();
	
			if(optional.isPresent()) {
				eventDetails.setDuration(optional.get().getTimestamp()- eventDetails.getTimestamp());
			}
		}
		 logger.info("Durations calculated for all events");		 
	}
	
	public void setEventsAlert() {
		logger.info("Set Alert for events taking more than 4ms");
		eventsList.stream().filter(e->e.getDuration()>4).forEach(e->e.setAlert(true));
		
	}
	
	public List<EventDetails> getEventsList() {
		return eventsList;
	}


	public List<EventDetails> getFinishedEvents() {
		return eventsList.stream().filter(e->e.getDuration()>0).collect(Collectors.toList());
	}
	
}
