package com.xworkz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Links implements Serializable{
	
	private static final long serialVersionUID = 1L;

	String rel;
	String href;
	String method;
	String targetSchema;
	String schema;

	public Links() {
	}

}