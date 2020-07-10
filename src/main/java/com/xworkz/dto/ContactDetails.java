package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ContactDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;

	String company;
	String address1;
	String address2;
	String city;
	String state;
	String zip;
	String country;
	String phone;
	
	public ContactDetails() {
		super();
	}
}