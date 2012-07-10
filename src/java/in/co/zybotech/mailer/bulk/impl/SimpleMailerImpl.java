package in.co.zybotech.mailer.bulk.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SimpleMailerImpl extends AbstractMailer {

	public void send(File source) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"Sending mails using source: [{}], fields: [{}]"
							+ ", subject: [{}], html: [{}], plain: [{}] and attachments: [{}].",
					new Object[] { source, fields, subject, html, plain,
							attachments });
		} else {
			logger.info("Sending mails for source: {}", source);
		}

		if (StringUtils.isBlank(html) && StringUtils.isBlank(plain)) {
			throw new IllegalArgumentException(
					"Both HTML and plain contents cannot be blank.");
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(source));
			String line;
			List<String> emails = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				logger.debug("Reading line {}", line);
				try {
					logger.info("Processing line {}", line);

					String[] split = StringUtils.split(line, ",");
					for (String string : split) {
						if (StringUtils.isNotBlank(string)) {
							emails.add(StringUtils.trimToEmpty(string));
						}
					}

					if (emails.size() == 50) {
						mail(emails.toArray(new String[] {}), html, plain);
						emails.clear();
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) {
						logger.warn("Error while sending mail to source: ", ex);
					} else {
						logger.error("Error while sending mail to source: {}",
								ex.toString());
					}
				}
			}
			if (emails.size() != 0) {
				mail(emails.toArray(new String[] {}), html, plain);
				emails.clear();
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}
}
