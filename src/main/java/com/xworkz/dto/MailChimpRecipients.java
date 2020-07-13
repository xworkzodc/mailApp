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

	String listId;
	boolean listIsActive;
	String listName;
	String segmentText;
	String recipientCount;

	public MailChimpRecipients() {
	}

}