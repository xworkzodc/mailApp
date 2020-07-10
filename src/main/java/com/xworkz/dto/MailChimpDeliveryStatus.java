package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpDeliveryStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	boolean enabled;

	public MailChimpDeliveryStatus() {
		super();
	}

}