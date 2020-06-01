package com.ipartek.formacion.api.controller;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Logeo;



@Path("/login/")
@Produces("application/json")
@Consumes("application/json")
public class LoginController {
	
	private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
	

	@POST
	public Object login(Logeo login){
		System.out.println(login);
		System.out.println(login.getEmail());
		System.out.println(login.getPassword());
		Response response;
		if(login !=null && login.getEmail().equals("admin@admin.com") && login.getPassword().equals("admin")) {
			byte[] randomBytes = new byte[24];
		    secureRandom.nextBytes(randomBytes);
		    String token = base64Encoder.encodeToString(randomBytes);
			response = Response.status(Status.OK).entity(token).build();
		}else {
			ArrayList<String> errores = new ArrayList<String>();
			errores.add("Login erroneo");
			response = Response.status(Status.CONFLICT).entity(errores).build();
		}
		return response;
		
	}

}
