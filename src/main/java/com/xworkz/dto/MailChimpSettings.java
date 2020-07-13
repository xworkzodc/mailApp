package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	String title;
	boolean useConversation;
	String toName;
	String folderId;
	boolean authenticate;
	boolean autoFooter;
	boolean inlineCss;
	boolean autoTweet;
	boolean fbComments;
	boolean timewarp;
	long templateId;
	boolean dragAndDrop;

	public MailChimpSettings() {
	}

}