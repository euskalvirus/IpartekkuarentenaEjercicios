package com.ipartek.formacion.api.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.ipartek.formacion.model.Persona;

@Path("/personas")
@Produces("application/json")
@Consumes("application/json")
public class PersonaController {

	private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getCanonicalName());
	
	@Context
	private ServletContext context;
	
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
	public Persona getPersona(@PathParam("id") Long id) {	
		LOGGER.info("getPersona");
		for (Persona per : personas) {
			if(per.getId()==id) {return per;}
		}
		return null;
	}
	
	@POST
	public ArrayList<Persona> addPersona(Persona persona){
		LOGGER.info("addPersona");
		personas.add(persona);
		return personas;
	}
	
	@PUT
	@Path("/{id}")
	public ArrayList<Persona> modifyPersona(Persona persona,@PathParam("id") Long id){
		LOGGER.info("modifyPersona");
		
		int i = 0;
		Boolean encontrado = false;
		while(i<personas.size() && !encontrado) {
			if(personas.get(i).getId()==id) {
				personas.set(i, persona);
				encontrado=true;
			}
			i++;
		}
		return personas;
	}
	
	@DELETE
	@Path("/{id}")
	public ArrayList<Persona> deletePersona(@PathParam("id") Long id){
		LOGGER.info("deletePersona");
		int i = 0;
		Boolean encontrado = false;
		while(i<personas.size() && !encontrado) {
			if(personas.get(i).getId()==id) {
				personas.remove(i);
				encontrado=true;
			}
			i++;
		}
		return personas;
		
	}
	
}
