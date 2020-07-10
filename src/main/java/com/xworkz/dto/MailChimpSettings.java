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
	boolean use_conversation;
	String to_name;
	String folder_id;
	boolean authenticate;
	boolean auto_footer;
	boolean inline_css;
	boolean auto_tweet;
	boolean fb_comments;
	boolean timewarp;
	long template_id;
	boolean drag_and_drop;

	public MailChimpSettings() {
	}

}