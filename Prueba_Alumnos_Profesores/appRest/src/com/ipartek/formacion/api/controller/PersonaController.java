package com.ipartek.formacion.api.controller;

import java.sql.SQLException;
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
import com.ipartek.formacion.model.dao.PersonaDao;

@Path("/personas")
@Produces("application/json")
@Consumes("application/json")
public class PersonaController {

	private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getCanonicalName());

	@Context
	private ServletContext context;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	private PersonaDao dao = new PersonaDao();

	private static ArrayList<Persona> personas = new ArrayList<Persona>();

	static {
		personas.add(new Persona(1, "Arantxa", "avatar1.png", "M"));
		personas.add(new Persona(2, "Idoia", "avatar2.png", "M"));
		personas.add(new Persona(3, "Iker", "avatar3.png", "H"));
		personas.add(new Persona(4, "Hodei", "avatar4.png", "H"));
	}

	public PersonaController() {
		super();

	}

	@GET
	public ArrayList<Persona> getAll() {
		LOGGER.info("getAll");
		personas = (ArrayList<Persona>) dao.getAll();
		return personas;
	}

	@GET
	@Path("/{id}")
	public Object getPersona(@PathParam("id") int id) {
		LOGGER.info("getPersona");
		ArrayList<String> errores = new ArrayList<String>();
		errores.add("No se ha encontrado ninguna persona con ese id.");

		Response response = Response.status(Status.NOT_FOUND).entity(errores).build();

		Persona persona;
		try {
			persona = dao.getById(id);

			if (persona != null)
				for (Persona per : personas) {
					if (per.getId() == id) {
						response = Response.status(Status.OK).entity(persona).build();
					}
				}

		} catch (Exception e) {
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			e.printStackTrace();
		}
		return response;

	}

	@POST
	public Object addPersona(Persona persona) {
		LOGGER.info("addPersona");
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(null).build();
		ArrayList<String> errores = new ArrayList<String>();

		// Validar pojo
		Set<ConstraintViolation<Persona>> violations = validator.validate(persona);

		if (violations.isEmpty()) {
			try {
				// INSERT DE PERSONA CON EL DAO
				Persona pers = dao.insert(persona);
				response = Response.status(Status.OK).entity(pers).build();
			} catch (Exception e) {
				errores.add(e.getMessage());
				response = Response.status(Status.BAD_REQUEST).entity(errores).build();
				e.printStackTrace();
			}
		} else {
			for (ConstraintViolation<Persona> violation : violations) {
				errores.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			response = Response.status(Status.BAD_REQUEST).entity(errores).build();
		}

		return response;
	}

	@PUT
	@Path("/{id}")
	public Object modifyPersona(Persona persona, @PathParam("id") Long id) {
		LOGGER.info("modifyPersona");
		
		// Validar pojo
		Set<ConstraintViolation<Persona>> violations = validator.validate(persona);
		ArrayList<String> errores = new ArrayList<String>();

		errores.add("No se ha encontrado ninguna persona con ese id.");

		Response response = Response.status(Status.NOT_FOUND).entity(null).build();

		if (violations.isEmpty()) {
			try {
				// INSERT DE PERSONA CON EL DAO
				Persona pers = dao.update(persona);
				if (pers != null) {
					response = Response.status(Status.OK).entity(pers).build();
				}
			} catch (Exception e) {
				errores.add(e.getMessage());
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
				e.printStackTrace();
			}
		} else {
			for (ConstraintViolation<Persona> violation : violations) {
				errores.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			response = Response.status(Status.BAD_REQUEST).entity(errores).build();
		}

		return response;
	}

	@DELETE
	@Path("/{id}")
	public Object deletePersona(@PathParam("id") int id) {
		LOGGER.info("deletePersona");
		Persona persona;
		ArrayList<String> errores = new ArrayList<String>();
		errores.add("No se ha encontrado ninguna persona con ese id.");

		Response response = Response.status(Status.NOT_FOUND).entity(errores).build();
		try {
			persona = dao.delete(id);
			if (persona != null) {
				response = Response.status(Status.OK).entity(persona).build();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

}
