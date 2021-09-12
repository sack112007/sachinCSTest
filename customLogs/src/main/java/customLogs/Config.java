package customLogs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	
	static Logger logger = LoggerFactory.getLogger("Config");
	
	private static Config config;
	private static Properties properties;
	
	private Config() throws FileNotFoundException, IOException {
		logger.debug("Load properties file");
		properties=new Properties();
		properties.load(new FileInputStream(getClass().getClassLoader().getResource("Config.properties").getFile()));
		
	}

	private static void getInstance() throws FileNotFoundException, IOException {
		if(config==null)
			config=new Config();
	}
	
	public static String getProperty(String key) throws FileNotFoundException, IOException {
		logger.debug("Get Property Value:{}",key);
		getInstance();
		return properties.getProperty(key);
	}
	
}
