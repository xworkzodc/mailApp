package com.xworkz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChimpMailDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String webId;
	String name;

	String permissionReminder;
	String useArchiveBar;

	String notifyOnSubscribe;
	String notifyOnUnsubscribe;
	String dateCreated;
	String listRating;
	String emailTypeOption;
	String subscribeUrlShort;
	String subscribeUrlLong;
	String beamerAddress;
	String visibility;
	String doubleOptin;
	String hasWelcome;
	String marketingPermissions;
	String[] modules;

	Stats stats;
	CampaignDefault campaignDefaults;
	List<Links> links;
	ContactDetails contact;

	public MailChimpMailDetails() {
	}

}