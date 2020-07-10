package com.xworkz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailChimpList implements Serializable {

	private static final long serialVersionUID = 1L;

	List<MailChimpMailDetails> lists;
	int total_items;
	List<Links> _links;

	public MailChimpList() {
	}

}