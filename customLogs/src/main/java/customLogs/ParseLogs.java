package customLogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import pojo.EventDetails;

public class ParseLogs {

	static Logger logger = LoggerFactory.getLogger("ParseLogs");

	private String line;
	private File fileName;
	private Events events = new Events();

	public ParseLogs(File fileName) throws FileNotFoundException {
		this.fileName = fileName;

	}

	public Events parse() throws IOException {
		logger.info("Parsing Log File: {} ", fileName);
		LineIterator iterator = FileUtils.lineIterator(fileName, "UTF-8");
		try {

			Gson gsonObj = new Gson();
			EventDetails event;

			while (iterator.hasNext()) {
				line = iterator.nextLine();
				logger.debug("Parsing line: {}", line);
				event = gsonObj.fromJson(line, EventDetails.class);
				checkForMandatoryFields(event);
				events.addEvent(event);
			}
		} catch (Exception e) {
			logger.error("Problem while parsing line :{}...\n error message: {}", line, e.getMessage());
		} finally {
			if (iterator != null) {
				iterator.close();
			}
		}
		logger.info("File Parsed sucessfully");

		return events;
	}

	private void checkForMandatoryFields(EventDetails event) {
		Optional.ofNullable(event.getId()).orElseThrow(() -> new IllegalStateException("id field is not present"));
		Optional.ofNullable(event.getState())
				.orElseThrow(() -> new IllegalStateException("State field is not present"));
		Optional.ofNullable(event.getTimestamp())
				.orElseThrow(() -> new IllegalStateException("TimeStamp field is not present"));

	}

}
