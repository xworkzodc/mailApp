package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChimpEcommerce implements Serializable {

	private static final long serialVersionUID = 1L;

	long totalOrders;
	long totalSpent;
	long totalRevenue;

	public MailChimpEcommerce() {

	}

}