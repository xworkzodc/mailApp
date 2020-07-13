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
	long uniqueOpens;
	String openRate;
	long clicks;
	long subscriberClicks;
	long clickRate;

	MailChimpEcommerce ecommerce;

	public MailChimpReport() {
		super();
	}

}