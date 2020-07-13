package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Stats implements Serializable {

	private static final long serialVersionUID = 1L;

	String memberCount;
	String unsubscribeCount;
	String cleanedCount;
	String memberCountSinceSend;
	String unsubscribeCountSinceSend;
	String cleanedCountSinceSend;
	String campaignCount;
	String campaignLastSent;
	String mergeFieldCount;
	String avgSubRate;
	String avgUnsubRate;
	String targetSubRate;
	String openRate;
	String clickRate;
	String lastSubDate;
	String lastUnsubDate;

	public Stats() {

	}

}