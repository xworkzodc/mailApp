package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpTracker implements Serializable {

	private static final long serialVersionUID = 1L;

	boolean opens;
	boolean html_clicks;
	boolean text_clicks;
	boolean goal_tracking;
	boolean ecomm360;
	String google_analytics;
	String clicktale;

	public MailChimpTracker() {

	}

}