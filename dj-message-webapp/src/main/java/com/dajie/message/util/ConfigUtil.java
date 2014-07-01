package com.dajie.message.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigUtil.class);

	private Properties tgProperties = new Properties();

	private static final String GOUDA_CONFIG = "/gouda_config.properties";

	private static ConfigUtil configUtil = new ConfigUtil();

	private ConfigUtil() {
		InputStream in = null;
		try {
			in = ConfigUtil.class.getResourceAsStream(GOUDA_CONFIG);
			tgProperties.load(in);
		} catch (IOException e) {
			LOGGER.error("failed to load properties from file, error is {}.",
					e, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("failed to close stream,error is {}.", e, e);
				}
			}
		}
	}

	public static final ConfigUtil getInstance() {
		return configUtil;
	}

	public String getConfig(String propertyName) {
		return tgProperties.getProperty(propertyName);
	}

}
