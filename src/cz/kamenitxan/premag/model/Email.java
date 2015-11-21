package cz.kamenitxan.premag.model;

public class Email {
	public String subject = null;
	public String text = null;
	public String recipient = null;

	public Email(String recipient, String subject, String text) {
		this.subject = subject;
		this.text = text;
		this.recipient = recipient;
	}
}
