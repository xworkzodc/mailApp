package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CampaignDefault implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String from_name;
	String from_email;
	String subject;
	String language;
	
	
	public CampaignDefault() {
		super();
	}
	
}