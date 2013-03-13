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
package com.emo.ananas.reporters;

import java.io.File;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emo.ananas.configs.EmailConfig;
import com.emo.ananas.configs.EmailSenderConfig;
import com.google.common.base.Preconditions;

public class EmailReporter {

	private final EmailSenderConfig sender;
	private final EmailConfig email;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public EmailReporter(final EmailSenderConfig sender, final EmailConfig email) {
		this.sender = sender;
		this.email = email;
	}

	public void report(final String reportName, final File report) {
		logger.info("send email report {} for {}", reportName, email);

		Preconditions.checkNotNull(reportName, "report name is required for email reporting");
		Preconditions.checkNotNull(report, "report file is required for email reporting");
		
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(report.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription(reportName);

		final String extension;

		int i = report.getName().lastIndexOf('.');
		if (i > 0) {
			extension = report.getName().substring(i);
		} else {
			extension = "";
		}

		attachment.setName(reportName + extension);

		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(sender.smtp);

		try {
			for (final String aTo : this.email.to) {
				email.addTo(aTo);
			}

			email.setFrom(this.email.from);
			email.setSubject(this.email.subject);
			email.setMsg("Report for " + reportName);

			email.attach(attachment);

			email.send();
		} catch (EmailException e) {
			throw new RuntimeException("failed to send email for report "
					+ reportName + " with " + this.email, e);
		}
	}
}
