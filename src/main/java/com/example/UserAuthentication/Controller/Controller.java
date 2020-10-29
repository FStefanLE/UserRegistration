package com.example.UserAuthentication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.UserAuthentication.Model.Response;
import com.example.UserAuthentication.Service.UserService;

@RestController
@RequestMapping("/api")
public class Controller {

	@Autowired
	UserService userService;

	@PostMapping("/createUser")
	public Response createUser(String username, String name, String email, String password) {
		return userService.createUser(username, name, email, password);
	}

	@PutMapping("/editUser")
	public Response editUser(String id, String name, String username, String email, String password) {
		return userService.editUser(id, name, username, email, password);
	}

	@DeleteMapping("/deleteUser")
	public Response deleteUser(String id) {
		return userService.deleteUser(id);
	}

	@GetMapping("/users")
	public Response getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/userDetail")
	public Response getUserDetail(String id) {
		return userService.getUserDetail(id);
	}

}
