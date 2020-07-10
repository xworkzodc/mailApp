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

	String member_count;
	String unsubscribe_count;
	String cleaned_count;
	String member_count_since_send;
	String unsubscribe_count_since_send;
	String cleaned_count_since_send;
	String campaign_count;
	String campaign_last_sent;
	String merge_field_count;
	String avg_sub_rate;
	String avg_unsub_rate;
	String target_sub_rate;
	String open_rate;
	String click_rate;
	String last_sub_date;
	String last_unsub_date;

	public Stats() {

	}

}