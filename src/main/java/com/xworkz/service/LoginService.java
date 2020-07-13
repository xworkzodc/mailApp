package com.xworkz.service;

import javax.servlet.http.HttpServletRequest;

import com.xworkz.dto.LoginDTO;


public interface LoginService {

	public boolean generateOTP(HttpServletRequest request);
	
	public String genarateRandomOTP();

	public boolean validateAndLogin(LoginDTO dto);

}
