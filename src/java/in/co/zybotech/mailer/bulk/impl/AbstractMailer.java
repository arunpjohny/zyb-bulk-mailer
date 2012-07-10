package in.co.zybotech.mailer.bulk.impl;

import in.co.zybotech.mailer.bulk.Mailer;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public abstract class AbstractMailer implements Mailer {

	protected Logger logger;
	protected JavaMailSender mailSender;
	protected List<String> fields;
	protected String from;
	protected String subject;
	protected String html;
	protected String plain;
	protected Map<String, File> attachments;

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

	public void setAttachments(Map<String, File> attachments) {
		this.attachments = attachments;
	}

	protected void mail(final String email, final String html,
			final String plain) {
		mail(new String[] { email }, html, plain);
	}

	protected void mail(final String[] emails, final String html,
			final String plain) {
		logger.debug("Sending emails to {}", StringUtils.join(emails, ", "));
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(
						mimeMessage, true, "UTF-8");
				messageHelper.setSubject(subject);
				for (String string : emails) {
					messageHelper.addTo(string);
				}
				messageHelper.setFrom(from);
				if (StringUtils.isNotBlank(plain)
						&& StringUtils.isNotBlank(html)) {
					messageHelper.setText(plain, html);
				} else if (StringUtils.isNotBlank(plain)) {
					messageHelper.setText(plain);
				} else if (StringUtils.isNotBlank(html)) {
					messageHelper.setText(html, true);
				}

				if (attachments != null) {
					for (Map.Entry<String, File> entry : attachments.entrySet()) {
						messageHelper.addAttachment(entry.getKey(),
								entry.getValue());
					}
				}
			}
		};

		this.mailSender.send(preparator);
	}

}
