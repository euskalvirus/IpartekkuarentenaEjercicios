package com.ipartek.formacion.api.controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Persona;

@Path("/personas")
@Produces("application/json")
@Consumes("application/json")
public class PersonaController {

	private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getCanonicalName());
	
	@Context
	private ServletContext context;
	
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();
	
	private static ArrayList<Persona> personas = new ArrayList<Persona>();
	
	static {
		personas.add( new Persona(1,"Arantxa","avatar1.png", "F") );
		personas.add( new Persona(2,"Idoia","avatar2.png", "F") );
		personas.add( new Persona(3,"Iker","avatar3.png", "M") );
		personas.add( new Persona(4,"Hodei","avatar4.png", "M") );
	}
	
	
	public PersonaController() {
		super();
		
	}

	@GET
	public ArrayList<Persona> getAll() {	
		LOGGER.info("getAll");
		return personas;
	}
	
	@GET
	@Path("/{id}")
	public Object getPersona(@PathParam("id") Long id) {	
		LOGGER.info("getPersona");
		ArrayList<String> errores = new ArrayList<String>();
		errores.add("No se ha encontrado ninguna persona con ese id.");
		
		Response response = Response.status(Status.NOT_FOUND).entity(errores).build();
		
		for (Persona per : personas) {
			if(per.getId()==id) {
				response = Response.status(Status.OK).entity(per).build();
			}
		}
		return response;
	}
	
	@POST
	public Object addPersona(Persona persona){
		LOGGER.info("addPersona");
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(null).build();
		//Validar pojo
		Set<ConstraintViolation<Persona>> violations = validator.validate(persona);
		
		if(violations.isEmpty()) {
			persona.setId(personas.get(personas.size()-1).getId()+1);
			personas.add(persona);
			response = Response.status(Status.CREATED).entity(persona).build();
		}else {
			ArrayList<String> errores = new ArrayList<String>();
			
			for (ConstraintViolation<Persona> violation : violations) {
				errores.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			response = Response.status(Status.BAD_REQUEST).entity(errores).build();
		}
		
		return response;
	}
	
	@PUT
	@Path("/{id}")
	public Object modifyPersona(Persona persona,@PathParam("id") Long id){
		LOGGER.info("modifyPersona");
		
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(null).build();
		//Validar pojo
		Set<ConstraintViolation<Persona>> violations = validator.validate(persona);
		ArrayList<String> errores = new ArrayList<String>();
		
		if(violations.isEmpty()) {
			for(int i=0;i<personas.size();i++) {
				if(personas.get(i).getId()==id) {
					personas.set(i, persona);
					response = Response.status(Status.OK).entity(persona).build();
					break;
				}
			}
			errores.add("No se ha encontrado ninguna persona con ese id.");
			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}else {
			for (ConstraintViolation<Persona> violation : violations) {
				errores.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			response = Response.status(Status.BAD_REQUEST).entity(errores).build();
		}
		
		return response;
	}
	
	@DELETE
	@Path("/{id}")
	public Object deletePersona(@PathParam("id") Long id){
		LOGGER.info("deletePersona");
		Persona persona = null;
		for(int i=0;i<personas.size();i++) {
			if(personas.get(i).getId()==id) {
				persona = personas.get(i);
				personas.remove(i);
				return Response.status(Status.OK).entity(persona).build();
			}
		}
		
		return Response.status(Status.NOT_FOUND).build();
		
	}
	
}
