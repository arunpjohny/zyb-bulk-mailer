package in.co.zybotech.mailer.bulk;

import in.co.zybotech.mailer.bulk.impl.MailerImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class Main {
	private static Logger logger = LoggerFactory
			.getLogger("in.co.zybotech.mailer.bulk");

	public static void main(String[] args) {

		if (args.length < 6 || args.length > 7) {
			logger.error("Usage: Main <properties> <source> <fields> <subject> <html> <plain> [attachment-location]");
			System.exit(1);
		}

		try {
			String propertiesPath = args[0];
			String db = args[1];
			String fields = args[2];
			String subject = args[3];
			String html = args[4];
			String plain = args[5];
			String attachLocation = args.length > 6 ? args[6] : null;

			Properties properties = getProperties(propertiesPath);
			JavaMailSender mailSender = getMailSender(properties);

			File dbFile = getFile(db);
			File htmlFile = getFile(html);
			File plainFile = getFile(plain);

			if (StringUtils.isEmpty(subject)) {
				throw new IllegalArgumentException("Subject cannot be empty.");
			}

			Mailer mailer = new MailerImpl();
			mailer.setLogger(logger);
			mailer.setSender(mailSender);
			mailer.setFields(getFields(fields));
			mailer.setFrom(properties.getProperty("mailer.from"));
			mailer.setSubject(subject);
			mailer.html(FileUtils.readFileToString(htmlFile));
			mailer.plain(FileUtils.readFileToString(plainFile));
			mailer.setAttachments(getAttachments(attachLocation));

			mailer.send(dbFile);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	private static Map<String, File> getAttachments(String attachLocation) {
		Map<String, File> attachments = new HashMap<String, File>();
		if (StringUtils.isNotBlank(attachLocation)) {
			File dir = new File(attachLocation);
			if (!dir.exists() || !dir.isDirectory()) {
				throw new IllegalArgumentException("Attachment location "
						+ dir.getAbsolutePath()
						+ " does not exists or is not a directory.");
			}

			File[] listFiles = dir.listFiles();
			for (File file : listFiles) {
				if (file.isFile()) {
					attachments.put(file.getName(), file);
				}
			}
		}
		return attachments;
	}

	private static List<String> getFields(String fields) {
		String[] stringList = fields.split(",");
		List<String> list = new ArrayList<String>();
		for (String string : stringList) {
			list.add(StringUtils.trimToEmpty(string));
		}
		return list;
	}

	private static File getFile(String name) throws FileNotFoundException {
		File file = new File(name);
		if (!file.exists()) {
			throw new FileNotFoundException("Unable to find the file: "
					+ file.getAbsolutePath() + ".");
		}
		return file;
	}

	private static JavaMailSender getMailSender(Properties properties) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(properties.getProperty("mailer.host"));
		mailSender.setPort(NumberUtils.toInt(properties
				.getProperty("mailer.port")));

		if (properties.containsKey("mailer.username")) {
			mailSender.setUsername(properties.getProperty("mailer.username"));

			if (properties.containsKey("mailer.password")) {
				mailSender.setPassword(properties
						.getProperty("mailer.password"));
			}
		}

		Properties javaMailProperties = new Properties();
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = entry.getKey().toString();
			if (StringUtils.startsWith(key, "java.mail.")) {
				javaMailProperties.put(
						StringUtils.substringAfter(key, "java.mail."),
						entry.getValue());
			}
		}
		mailSender.setJavaMailProperties(javaMailProperties);

		return mailSender;
	}

	private static Properties getProperties(String propertiesPath)
			throws IOException, FileNotFoundException {
		File propertiesFile = new File(propertiesPath);

		Properties properties = new Properties();

		if (logger.isInfoEnabled()) {
			logger.info("Loading properites file: "
					+ propertiesFile.getAbsolutePath());
		}
		properties.load(new FileInputStream(propertiesFile));
		return properties;
	}

}
