package com.xworkz.dto;

import java.io.Serializable; 
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpCampaign implements Serializable {
	
	private static final long serialVersionUID = 1L;

	String id;
	long web_id;
	String type;
	String create_time;
	String archive_url;
	String long_archive_url;
	String status;
	long emails_sent;
	String send_time;
	String content_type;
	boolean needs_block_refresh;
	boolean resendable;
	
	
	MailChimpRecipients recipients;
	MailChimpSettings settings;
	MailChimpTracker tracking;
	MailChimpReport report_summary;
	MailChimpDeliveryStatus delivery_status;
	List<Links> _links;
	
	public MailChimpCampaign() {
	}
		
}