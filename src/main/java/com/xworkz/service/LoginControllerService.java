package com.xworkz.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import com.xworkz.dto.LoginDTO;


public interface LoginControllerService {

	public boolean validateAndLogin(LoginDTO dto, ModelMap model,HttpServletRequest request);

	public boolean generateOTP(HttpServletRequest request);
	
	public String genarateRandomOTP();

}
