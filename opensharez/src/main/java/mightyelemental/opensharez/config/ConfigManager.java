package mightyelemental.opensharez.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mightyelemental.opensharez.OpenShareZ;

public class ConfigManager {

	/** A list of all the config settings. Using generic type to allow settings of varying types to be stored */
	@SuppressWarnings("rawtypes")
	private static List<ConfigSetting> settings = new ArrayList<ConfigSetting>();

	public static final String CONFIG_FILE_PATH = OpenShareZ.HOME_DIR + "/.OpenShareZconfig";

	public static boolean doesConfigExists() {
		return new File(CONFIG_FILE_PATH).exists();
	}

	/**
	 * Creates a new config file if one does not exist
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	public static void createConfigFileIfNotPresent() throws IOException {
		if (doesConfigExists()) return;
		setSetting("copy_to_clipboard", true);// Copy image to clipboard after capture
		setSetting("save_image_to_file", true);// Upload image after capture
		setSetting("upload_after_capture", false);// Upload image after capture
		saveConfig();
	}

	static {
		// System.out.println( doesConfigExists() );
	}

	/**
	 * Load the config file
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	@SuppressWarnings("rawtypes")
	public static void loadSettings() throws IOException {
		if (!doesConfigExists()) return;
		BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH));
		String line = null;
		while ((line = br.readLine()) != null) {
			ConfigSetting cs = ConfigSetting.settingFromString(line);
			if (cs != null) settings.add(cs);
		}
		br.close();
	}

	/**
	 * Used to set the value of a setting. If the setting already exists, it will change the value of the setting.
	 * 
	 * @param name the name of the variable
	 * @param value the value to set the variable to
	 * @param <T> the type the variable should be
	 */
	@SuppressWarnings("unchecked")
	public static <T> void setSetting( String name, T value ) {
		ConfigSetting<T> curr = null;
		for (ConfigSetting<?> conf : settings) {
			if (conf.getSettingName().equals(name)) {
				curr = (ConfigSetting<T>) conf;
			}
		}

		if (curr == null) {
			ConfigSetting<T> setting = new ConfigSetting<T>(name, value);
			settings.add(setting);
		} else {
			curr.setValue(value);
		}

	}

	/**
	 * Save the config file to disk.
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	@SuppressWarnings("rawtypes")
	public static void saveConfig() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE_PATH));

		for (ConfigSetting setting : settings) {
			writer.write(setting.getSaveString() + "\n");
		}

		writer.close();
	}

}
