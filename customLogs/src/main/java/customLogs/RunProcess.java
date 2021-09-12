package customLogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunProcess {
	static Logger logger = LoggerFactory.getLogger("RunProcess");
	static File fileName;

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		logger.info("!!!Start!!!");
		verifyInputParameter(args);
		ParseLogs parseLogs = new ParseLogs(fileName);
		Events events = parseLogs.parse();
		events.calculateEventsDuration();
		events.setEventsAlert();
		
		new DatabaseOperations().insertEvents(events.getFinishedEvents());;

		logger.info("!!!Done!!!");

	}

	private static void verifyInputParameter(String[] inputArgs) throws FileNotFoundException {
		logger.info("Checking Input Parameters...");
		if (inputArgs.length == 0)
			throw new IllegalArgumentException("Input File Path missing");

		if (Files.exists(Paths.get(inputArgs[0]))) {

			fileName = Paths.get(inputArgs[0]).toFile();
		} else {
			logger.error("Input File not found..File: {}",Paths.get(inputArgs[0]));
			throw new FileNotFoundException();
		}

		logger.info("Input Parameters Verified");
	}

}
