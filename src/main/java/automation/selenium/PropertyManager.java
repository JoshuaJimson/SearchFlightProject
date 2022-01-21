package automation.selenium;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	private static PropertyManager propertyManager;
	private static final Object lock = new Object();
    private static String propertyFilePath=System.getProperty("user.dir") + "\\resources\\config.properties"; 
	private static String app_url;
	private static String chrome_driver_path;
	private static String edge_driver_path;

	// Create a Singleton instance. We need only one instance of Property Manager.
	public static PropertyManager getInstance() {
		if (propertyManager == null) {
			synchronized (lock) {
				propertyManager = new PropertyManager();
				propertyManager.loadData();
			}
		}
		return propertyManager;
	}

	// Get all configuration data and assign to related fields.
	private void loadData() {
		// Declare a properties object
		Properties prop = new Properties();

		// Read configuration.properties file
		try {
            prop.load(new FileInputStream(propertyFilePath));
		} catch (IOException e) {
			System.out.println("Configuration properties file cannot be found");
		}

		app_url = prop.getProperty("app_url");
		chrome_driver_path = prop.getProperty("chrome_driver_path");
		edge_driver_path = prop.getProperty("edge_driver_path");
	}

	public String getAppURL() {
		return app_url;
	}

	public String getChromeDriverPath() {
		return chrome_driver_path;
	}

	public String getEdgeDriverPath() {
		return edge_driver_path;
	}
	
}
