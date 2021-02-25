package mightyelemental.opensharez.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigSetting<T> {

	private String	settingName;
	private T		value;

	public ConfigSetting( String settingName, T value ) {
		this.settingName = settingName;
		this.value = value;
	}

	/**
	 * Convert a String to a ConfigSetting. Accepted data types are defined here as well.
	 * 
	 * @param s the string to convert
	 * @return The ConfigSetting generated from the String
	 */
	@SuppressWarnings("rawtypes")
	public static ConfigSetting settingFromString( String s ) {
		Matcher m = Pattern.compile("(.*?)\\<(.*?)\\>=(.*)").matcher(s);
		if (m.find()) {
			String name = m.group(1);
			String classType = m.group(2);
			String value = m.group(3);
			System.out.printf("%s, %s, %s\n", name, classType, value);
			switch (classType) {
				case "java.lang.Boolean":
					return new ConfigSetting<Boolean>(name, Boolean.parseBoolean(value));
				case "java.lang.Integer":
					return new ConfigSetting<Integer>(name, Integer.parseInt(value));
			}
		}
		return null;
	}

	/**
	 * Get the name of the setting
	 * 
	 * @return The name of the setting
	 */
	public String getSettingName() {
		return settingName;
	}

	/**
	 * Get the value of the setting
	 * 
	 * @return The value of the setting
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Convert the setting to a String format to be able to save it
	 * 
	 * @return The setting in String form
	 */
	public String getSaveString() {
		return String.format("%s<%s>=%s", settingName, value.getClass().getName(), value.toString());
	}

	/**
	 * Set the value of the setting
	 * 
	 * @param value the value to set the setting to
	 */
	public void setValue( T value ) {
		this.value = value;
	}

}
