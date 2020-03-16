package mightyelemental.opensharez.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigSetting<T> {

	private String settingName;
	private T      value;

	public ConfigSetting(String settingName, T value) { this.settingName = settingName; this.value = value; }

	@SuppressWarnings("rawtypes")
	public static ConfigSetting settingFromString(String s) {
		Matcher m = Pattern.compile( "(.*?)\\<(.*?)\\>=(.*)" ).matcher( s );
		if (m.find()) {
			String name = m.group( 1 );
			String classType = m.group( 2 );
			String value = m.group( 3 );
			System.out.printf( "%s, %s, %s\n", name, classType, value );
			switch (classType) {
			case "java.lang.Boolean":
				return new ConfigSetting<Boolean>( name, Boolean.parseBoolean( value ) );
			case "java.lang.Integer":
				return new ConfigSetting<Integer>( name, Integer.parseInt( value ) );
			}
		}
		return null;
	}

	public String getSettingName() { return settingName; }

	public T getValue() { return value; }

	public String getSaveString() {
		return String.format( "%s<%s>=%s", settingName, value.getClass().getName(), value.toString() );
	}

	public void setValue(T value) {
		this.value = value;
	}

}
