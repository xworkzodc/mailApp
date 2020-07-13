package com.xworkz.dto;

import java.io.Serializable; 
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChimpCampaign implements Serializable {
	
	private static final long serialVersionUID = 1L;

	String id;
	long webId;
	String type;
	String createTime;
	String archiveURL;
	String longArchiveURL;
	String status;
	long emailSent;
	String sentTime;
	String contentType;
	boolean needsBlockRefresh;
	boolean resendable;
	
	
	MailChimpRecipients recipients;
	MailChimpSettings settings;
	MailChimpTracker tracking;
	MailChimpReport reportSummary;
	MailChimpDeliveryStatus deliveryStatus;
	List<Links> links;
	
	public MailChimpCampaign() {
	}
		
}