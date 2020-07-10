package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpEcommerce implements Serializable {

	private static final long serialVersionUID = 1L;

	long total_orders;
	long total_spent;
	long total_revenue;

	public MailChimpEcommerce() {

	}

}