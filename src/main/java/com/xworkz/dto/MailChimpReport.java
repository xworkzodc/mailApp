package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpReport implements Serializable {

	private static final long serialVersionUID = 1L;

	long opens;
	long unique_opens;
	String open_rate;
	long clicks;
	long subscriber_clicks;
	long click_rate;

	MailChimpEcommerce ecommerce;

	public MailChimpReport() {
		super();
	}

}