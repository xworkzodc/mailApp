package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpRecipients implements Serializable {

	private static final long serialVersionUID = 1L;

	String list_id;
	boolean list_is_active;
	String list_name;
	String segment_text;
	String recipient_count;

	public MailChimpRecipients() {
	}

}