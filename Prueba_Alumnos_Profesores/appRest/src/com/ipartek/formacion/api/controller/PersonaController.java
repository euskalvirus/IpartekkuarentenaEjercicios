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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Curso;
import com.ipartek.formacion.model.Persona;
import com.ipartek.formacion.model.PersonaCurso;
import com.ipartek.formacion.model.Rol;
import com.ipartek.formacion.model.dao.CursoDao;
import com.ipartek.formacion.model.dao.PersonaCursoDao;
import com.ipartek.formacion.model.dao.PersonaDao;

@Path("/personas/")
@Produces("application/json")
@Consumes("application/json")
public class PersonaController {

	private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getCanonicalName());

	@Context
	private ServletContext context;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	private PersonaDao dao = (PersonaDao) PersonaDao.getInstancia();

	public PersonaController() {
	}
	
	class CursosINyNotIn {
		
		ArrayList<Curso> cursosIn;
		ArrayList<Curso> cursosNotIn;
		
		public CursosINyNotIn(ArrayList<Curso> cursosIn, ArrayList<Curso> cursosNotIn) {
			this.cursosIn = cursosIn;
			this.cursosNotIn = cursosNotIn;
		}
		
		public CursosINyNotIn() {}

		public ArrayList<Curso> getCursosIn() {
			return cursosIn;
		}

		public void setCursosIn(ArrayList<Curso> cursosIn) {
			this.cursosIn = cursosIn;
		}

		public ArrayList<Curso> getCursosNotIn() {
			return cursosNotIn;
		}

		public void setCursosNotIn(ArrayList<Curso> cursosNotIn) {
			this.cursosNotIn = cursosNotIn;
		}

		@Override
		public String toString() {
			return "CursosINyNotIn [cursosIn=" + cursosIn + ", cursosNotIn=" + cursosNotIn + "]";
		}	
	}

	@GET
	public Object getAll(@QueryParam("rol") int rol,@QueryParam("filtro") String filtro) {
		LOGGER.info("getAll" + rol);
		Response response;
		if(filtro!= null) {
			LOGGER.info("getAll" + filtro);

			
			try {
				Boolean existe =  dao.getByName(filtro);
				if(existe) {
					response = Response.status(Status.OK).entity(new ArrayList<String>()).build();
				}else {
					response = Response.status(Status.NOT_FOUND).entity(new ArrayList<String>()).build();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ArrayList<String> errores = new ArrayList<String>();
				errores.add(e.getMessage());
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			}
			return response;
		}

		try {
			ArrayList<Persona> personas = new ArrayList<Persona>();
			if(rol ==0) {
				personas = (ArrayList<Persona>) dao.getAll();
				System.out.println(personas);
			}else {
				personas = (ArrayList<Persona>) dao.getAllByRol(new Rol(rol,""));
				System.out.println(personas);
			}
			response = Response.status(Status.OK).entity(personas).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ArrayList<String> errores = new ArrayList<String>();
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
		}
		return response;
	}
	

	@GET
	@Path("/{id}")
	public Object getPersona(@PathParam("id") int id) {
		LOGGER.info("getPersona");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;

		Persona persona;
		try {
			persona = dao.getById(id);

			if (persona != null) {
				response = Response.status(Status.OK).entity(persona).build();
			}
		} catch (SQLException e) {
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			e.printStackTrace();
		} catch (Exception e) {
			errores.add(e.getMessage());
			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}
		return response;

	}
	
	@GET
	@Path("/profesor/{id}")
	public Object getProfesor(@PathParam("id") int id) {
		LOGGER.info("getPersona");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;

		Persona persona;
		try {
			persona = dao.getProfesorById(id);

			if (persona != null) {
				response = Response.status(Status.OK).entity(persona).build();
			}
		} catch (SQLException e) {
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			e.printStackTrace();
		} catch (Exception e) {
			errores.add(e.getMessage());
			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}
		return response;

	}
	
	@GET
	@Path("/{idPersona}/cursos/")
	public Object getPersonaCursos(@PathParam("idPersona") int id) {
		LOGGER.info("getPersonaCursos");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		
		ArrayList<Curso> cursosIn = new ArrayList<Curso>();
		ArrayList<Curso> cursosNotIn = new ArrayList<Curso>();
		try {
			cursosIn = (ArrayList<Curso>) PersonaCursoDao.getInstancia().getPersonaCursos(id);
			cursosNotIn = (ArrayList<Curso>) PersonaCursoDao.getInstancia().getPersonaNoCursos(id);
			CursosINyNotIn cursosGrupo = new CursosINyNotIn(cursosIn, cursosNotIn);
			response = Response.status(Status.OK).entity(cursosGrupo).build();
		} catch (SQLException e) {
			LOGGER.warning("SQLException INTERNAL_SERVER_ERROR");
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ninguna persona con ese id");
			errores.add("No se ha encontrado ninguna persona con ese id.");
			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}
		return response;

	}
	
	@GET
	@Path("/profesor/{idPersona}/cursos/")
	public Object getProfesorCursos(@PathParam("idPersona") int id) {
		LOGGER.info("getPersonaCursos");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		
		ArrayList<Curso> cursosIn = new ArrayList<Curso>();
		ArrayList<Curso> cursosNotIn = new ArrayList<Curso>();
		try {
			cursosIn = (ArrayList<Curso>) PersonaCursoDao.getInstancia().getProfesorCursos(id);
			cursosNotIn = (ArrayList<Curso>) ((CursoDao)CursoDao.getInstancia()).getAllWithoutProfessor();
			CursosINyNotIn cursosGrupo = new CursosINyNotIn(cursosIn, cursosNotIn);
			response = Response.status(Status.OK).entity(cursosGrupo).build();
		} catch (SQLException e) {
			LOGGER.warning("SQLException INTERNAL_SERVER_ERROR");
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ninguna persona con ese id");
			errores.add("No se ha encontrado ninguna persona con ese id.");
			response = Response.status(Status.NOT_FOUND).entity(errores).build();
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
				dao.insert(persona);
				response = Response.status(Status.CREATED).entity(persona).build();
			} catch (SQLException e) {
				errores.add(e.getMessage());
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				errores.add("Hay un conflicto al intentar agregar el registro: " + e.getMessage());
				response = Response.status(Status.CONFLICT).entity(errores).build();
			}
		} else {
			for (ConstraintViolation<Persona> violation : violations) {
				errores.add(violation.getPropertyPath() + ": " + violation.getMessage());
			}
			response = Response.status(Status.BAD_REQUEST).entity(errores).build();
		}

		return response;
	}
	
	@POST
	@Path("/{idPersona}/cursos/{idCurso}")
	public Object addPersonaCurso(@PathParam("idPersona") int idPersona,@PathParam("idCurso") int idCurso) {
		LOGGER.info("deletePersonaCurso");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		try {
			PersonaCurso pc =PersonaCursoDao.getInstancia().addPersonaCurso(idPersona, idCurso);
			response = Response.status(Status.OK).entity(pc).build();
		} catch (SQLException e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			response = Response.status(Status.CONFLICT).entity(errores).build();
		} catch (Exception e) {
			e.printStackTrace();
			errores.add(e.getMessage());

			response = Response.status(Status.NOT_FOUND).entity(errores).build();
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

		

		Response response = null;

		if (violations.isEmpty()) {
			try {
				// INSERT DE PERSONA CON EL DAO
				Persona pers = dao.update(persona);
				if (pers != null) {
					response = Response.status(Status.OK).entity(pers).build();
				}
			} catch (SQLException e) {
				errores.add(e.getMessage());
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
				e.printStackTrace();
			} catch (Exception e) {
				errores.add("O No se ha encontrado ninguna persona con ese id, o ha habido algun conflicto de constrain: " + e.getMessage());
				response = Response.status(Status.CONFLICT).entity(null).build();
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
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		try {
			Persona persona = dao.delete(id);
			response = Response.status(Status.OK).entity(persona).build();
		} catch (SQLException e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			response = Response.status(Status.CONFLICT).entity(errores).build();
		} catch (Exception e) {
			e.printStackTrace();
			errores.add(e.getMessage());

			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}

		return response;

	}
	
	@DELETE
	@Path("/{idPersona}/cursos/{idCurso}")
	public Object deletePersonaCurso(@PathParam("idPersona") int idPersona,@PathParam("idCurso") int idCurso) {
		LOGGER.info("deletePersonaCurso");
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		try {
			PersonaCurso pc = PersonaCursoDao.getInstancia().deletePersonaCurso(idPersona, idCurso);
			response = Response.status(Status.OK).entity(pc).build();
		} catch (SQLException e) {
			e.printStackTrace();
			errores.add("Hay un conflicto al intentar eliminar el registro: " + e.getMessage());
			response = Response.status(Status.CONFLICT).entity(errores).build();
		} catch (Exception e) {
			e.printStackTrace();
			errores.add(e.getMessage());

			response = Response.status(Status.NOT_FOUND).entity(errores).build();
		}

		return response;

	}

}
