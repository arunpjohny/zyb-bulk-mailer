package in.co.zybotech.mailer.bulk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class Mailer {
	private Logger logger;

	private JavaMailSender mailSender;
	private List<String> fields;
	private String from;
	private String subject;
	private String html;
	private String plain;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSubject(String subject) {
		this.subject = subject;

	}

	public void html(String html) {
		this.html = html;
	}

	public void plain(String plain) {
		this.plain = plain;
	}

	public void send(File source) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(source));
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				try {
					final String email = strLine;
					MimeMessagePreparator preparator = new MimeMessagePreparator() {
						public void prepare(MimeMessage mimeMessage)
								throws Exception {
							MimeMessageHelper messageHelper = new MimeMessageHelper(
									mimeMessage, true, "UTF-8");
							messageHelper.setSubject(subject);
							messageHelper.setTo(email);
							messageHelper.setFrom(from);
							messageHelper.setText(plain, html);
						}
					};

					this.mailSender.send(preparator);
				} catch (MailException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Error while sending mail to source: "
								+ strLine, ex);
					} else {
						logger.error("Error while sending mail to source: {}"
								+ strLine + ", " + ex.toString());
					}
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

}
