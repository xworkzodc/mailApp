package com.xworkz.dao;

import com.xworkz.entity.LoginEntity;

public interface EnquiryLoginDAO {
	
	public LoginEntity fecthByUserName(String email);

	public boolean upadtePassByEmail(String newRandomPassword, String userName);


}
