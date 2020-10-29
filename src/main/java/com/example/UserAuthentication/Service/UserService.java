package com.example.UserAuthentication.Service;

import com.example.UserAuthentication.Model.Response;

public interface UserService {

	Response createUser(String username, String name, String email, String password);

	Response editUser(String id, String name, String username, String email, String password);

	Response deleteUser(String username);
	
	Response getUsers();
	
	Response getUserDetail(String id); 

}
