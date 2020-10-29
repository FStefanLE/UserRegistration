package com.example.UserAuthentication.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.UserAuthentication.Model.MongoUser;
import com.example.UserAuthentication.Model.Response;
import com.example.UserAuthentication.Repository.UserRepository;
import com.example.UserAuthentication.Service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	FirebaseInteraction firebaseInteraction;
	@Autowired
	UserRepository userRepository;

	@Override
	public Response createUser(String username, String name, String email, String password) {

		MongoUser user = new MongoUser();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		user.setUsername(username);

		JsonNode fireResponse = null;
		Response response;
		try {
			user = userRepository.save(user);
			fireResponse = firebaseInteraction.saveUser(user);
			response = new Response(HttpStatus.OK.getReasonPhrase(), "Usuario dado de alta correctamente",
					fireResponse);
		} catch (Exception e) {
			response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error al dar de alta usuario",
					e.getMessage());
		}

		return response;
	}

	@Override
	public Response editUser(String id, String name, String username, String email, String password) {

		Response response = null;
		try {

			MongoUser mUser = userRepository.findById(id).get();
			if (mUser != null) {

				mUser.setEmail(email);
				mUser.setName(name);
				mUser.setPassword(password);
				mUser.setUsername(username);

				userRepository.save(mUser);

				JsonNode fireResponse = firebaseInteraction.getUserDetail(id);
				JsonNode fResponse;

				if (fireResponse != null) {
					fResponse = firebaseInteraction.editUser(fireResponse, mUser);
					response = new Response(HttpStatus.OK.getReasonPhrase(), "Usuario editado", fResponse);
				}

			} else {
				response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Usuario no existe", null);
			}

		} catch (Exception e) {
			response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error al editar usuario",
					e.getMessage());
			System.out.println(e.getMessage());
		}

		return response;

	}

	@Override
	public Response deleteUser(String id) {

		Response response;
		try {
			MongoUser mUser = userRepository.findById(id).get();
			if (mUser != null) {
				userRepository.delete(mUser);
				JsonNode fireResponse = firebaseInteraction.getUserDetail(id);
				if (fireResponse != null) {

					String name = fireResponse.fieldNames().next();
					fireResponse = firebaseInteraction.deleteUser(name);

				}
				response = new Response(HttpStatus.OK.getReasonPhrase(), "Usuario eliminado", fireResponse);
			} else {
				response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Ese id no existe", null);
			}

		} catch (Exception e) {
			response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error al eliminar usuario",
					e.getMessage());
			System.out.println(e.getMessage());
		}

		return response;

	}

	@Override
	public Response getUsers() {

		Response response;
		try {
			JsonNode fireResponse = firebaseInteraction.getUsers();
			response = new Response(HttpStatus.OK.getReasonPhrase(), "Listado de usuarios", fireResponse);
		} catch (Exception e) {
			response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error al consultar usuarios",
					e.getMessage());
		}

		return response;

	}

	@Override
	public Response getUserDetail(String id) {

		Response response;
		try {
			JsonNode fireResponse = firebaseInteraction.getUserDetail(id);
			response = new Response(HttpStatus.OK.getReasonPhrase(), "Usuario obtenido", fireResponse);
		} catch (Exception e) {
			response = new Response(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error al consultar usuario",
					e.getMessage());
		}

		return response;

	}

}
