package com.xworkz.service;

import com.xworkz.dto.LoginDTO;

public interface EnquiryLoginService {

	public String validateLogin(LoginDTO loginDTO);
	
	public boolean validateAndResetPassword(LoginDTO loginDTO);

}
