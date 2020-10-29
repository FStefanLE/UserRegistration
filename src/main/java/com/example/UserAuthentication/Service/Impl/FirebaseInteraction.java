package com.example.UserAuthentication.Service.Impl;

import java.net.URI;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.UserAuthentication.Model.MongoUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class FirebaseInteraction {

	@Autowired
	Environment env;

	public JsonNode getUsers() {

		ObjectMapper mapper = new ObjectMapper();

		// External service URL
		String url = env.getProperty("firebase.post.add.url") + "?print=pretty";

		// External service request
		WebClient webClient = WebClient.create();
		JsonNode response = null;
		try {
			response = mapper
					.readTree(webClient.get().uri(URI.create(url)).retrieve().bodyToMono(String.class).block());
		} catch (Exception e) {
			System.out.println("Response error");
		}

		return response;

	}

	public JsonNode saveUser(MongoUser user) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("_id", user.getId());
		objectNode.put("email", user.getEmail());
		objectNode.put("name", user.getName());
		objectNode.put("username", user.getUsername());
		objectNode.put("password", user.getPassword());
		String body = objectNode.toPrettyString();

		// External service URL
		String url = env.getProperty("firebase.post.add.url");

		// External service request
		WebClient webClient = WebClient.create();
		JsonNode response = null;
		try {
			response = mapper.readTree(webClient.post().uri(URI.create(url)).body(BodyInserters.fromValue(body))
					.retrieve().bodyToMono(String.class).block());
		} catch (Exception e) {
			System.out.println("Response error");
		}

		return response;

	}

	public JsonNode getUserDetail(String id) {

		ObjectMapper mapper = new ObjectMapper();

		String url = null;
		try {
			String encoded = URLEncoder.encode("\"", java.nio.charset.StandardCharsets.UTF_8.toString());
			// External service URL
			url = env.getProperty("firebase.post.add.url") + "?orderBy=" + encoded + "_id" + encoded + "&equalTo="
					+ encoded + id + encoded + "&print=pretty";
		} catch (Exception e) {
			System.out.println("URL error");
		}

		// External service request
		WebClient webClient = WebClient.create();
		JsonNode response = null;
		try {
			response = mapper
					.readTree(webClient.get().uri(URI.create(url)).retrieve().bodyToMono(String.class).block());
		} catch (Exception e) {
			System.out.println("Response error");
		}

		return response;

	}

	public JsonNode deleteUser(String user) {

		ObjectMapper mapper = new ObjectMapper();

		String url = null;
		// External service URL
		url = env.getProperty("firebase.delete.user.url") + user + ".json";

		// External service request
		WebClient webClient = WebClient.create();
		JsonNode response = null;
		try {
			response = mapper
					.readTree(webClient.delete().uri(URI.create(url)).retrieve().bodyToMono(String.class).block());
		} catch (Exception e) {
			System.out.println("Response error");
		}

		return response;

	}
	
	public JsonNode editUser(JsonNode user, MongoUser mUser) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("_id", user.get(user.fieldNames().next()).get("_id").asText());
		objectNode.put("email", mUser.getEmail());
		objectNode.put("name", mUser.getName());
		objectNode.put("password", mUser.getPassword());
		objectNode.put("username", mUser.getUsername());
		
		ObjectNode requestNode = mapper.createObjectNode();
		requestNode.put(user.fieldNames().next(), objectNode);
		
		String body = requestNode.toPrettyString();
		//System.out.println(body);
		// External service URL
		String url = env.getProperty("firebase.post.add.url");

		// External service request
		WebClient webClient = WebClient.create();
		JsonNode response = null;
		try {
			response = mapper.readTree(webClient.put().uri(URI.create(url)).body(BodyInserters.fromValue(body))
					.retrieve().bodyToMono(String.class).block());
		} catch (Exception e) {
			System.out.println("Response error");
		}

		return response;

	}

}
