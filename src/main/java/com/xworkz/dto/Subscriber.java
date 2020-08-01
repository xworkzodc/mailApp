package com.xworkz.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Subscriber implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullName;
	private String emailId;
	private Date dob;

	public Subscriber() {

	}

	public Subscriber(Date date, String fullName, String email) {
		this.dob = date;
		this.fullName = fullName;
		this.emailId = email;
	}

}
