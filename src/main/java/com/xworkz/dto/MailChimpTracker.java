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
	boolean htmlClicks;
	boolean textClicks;
	boolean goalTracking;
	boolean ecomm360;
	String googleAnalytics;
	String clicktale;

	public MailChimpTracker() {

	}

}