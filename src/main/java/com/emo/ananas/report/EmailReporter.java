/*
 * Copyright 2013 Cédric Boufflers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emo.ananas.report;

import java.io.File;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailReporter {

	private final List<String> to;
	private final String from;
	private final String subject;

	private final String smtp;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public EmailReporter(final String smtp, final List<String> to,
			final String from, final String subject) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.smtp = smtp;
	}

	public void report(final String reportName, final List<File> reportings) {
		logger.info("send email report {} for {}", reportName, to);

		final MultiPartEmail email = new MultiPartEmail();
		email.setHostName(smtp);

		try {
			for (final File reporting : reportings) {
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(reporting.getAbsolutePath());
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(reportName);

				final String extension;

				int i = reporting.getName().lastIndexOf('.');
				if (i > 0) {
					extension = reporting.getName().substring(i);
				} else {
					extension = "";
				}

				attachment.setName(reportName + extension);
				email.attach(attachment);
			}

			for (final String aTo : to) {
				email.addTo(aTo);
			}

			email.setFrom(from);
			email.setSubject(subject);
			email.setMsg("Report for " + reportName);

			email.send();
		} catch (EmailException e) {
			throw new RuntimeException("failed to send email for report "
					+ reportName + " to " + to, e);
		}
	}
}
