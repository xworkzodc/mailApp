package com.xworkz.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.xworkz.dto.Subscriber;

public interface MailSchedularService {

	public void birthadyMailSender() throws URISyntaxException, IOException;
	
	public List<Subscriber> getListOfSubscribersFromExcel() throws IOException;
}
