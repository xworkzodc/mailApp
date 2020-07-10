package com.xworkz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpMailDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String web_id;
	String name;

	String permission_reminder;
	String use_archive_bar;

	String notify_on_subscribe;
	String notify_on_unsubscribe;
	String date_created;
	String list_rating;
	String email_type_option;
	String subscribe_url_short;
	String subscribe_url_long;
	String beamer_address;
	String visibility;
	String double_optin;
	String has_welcome;
	String marketing_permissions;
	String[] modules;

	Stats stats;
	CampaignDefault campaign_defaults;
	List<Links> _links;
	ContactDetails contact;

	public MailChimpMailDetails() {
	}

}