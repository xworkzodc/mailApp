package com.xworkz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChimpList implements Serializable {

	private static final long serialVersionUID = 1L;

	List<MailChimpMailDetails> lists;
	int totalItems;
	List<Links> links;

	public MailChimpList() {
	}

}