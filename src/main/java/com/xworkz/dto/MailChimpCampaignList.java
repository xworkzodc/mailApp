package com.xworkz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChimpCampaignList implements Serializable{
	
	private static final long serialVersionUID = 1L;

	List<MailChimpCampaign> campaigns;
	long totalItems;
	List<Links> links;
	
	public MailChimpCampaignList() {
	}
	
	
}