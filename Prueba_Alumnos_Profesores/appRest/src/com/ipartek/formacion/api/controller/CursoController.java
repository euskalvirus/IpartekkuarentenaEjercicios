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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Curso;
import com.ipartek.formacion.model.Persona;
import com.ipartek.formacion.model.dao.CursoDao;
import com.ipartek.formacion.model.dao.PersonaDao;

@Path("/cursos/")
@Produces("application/json")
@Consumes("application/json")
public class CursoController {

	private static final Logger LOGGER = Logger.getLogger(CursoController.class.getCanonicalName());

	@Context
	private ServletContext context;

	private CursoDao dao = (CursoDao) CursoDao.getInstancia();

	public CursoController() {
	}

	@GET
	public Object getAll(@QueryParam("filtro") String filtro) {
		LOGGER.info("getAll " + filtro);

		Response response;
		try {
			ArrayList<Curso> cursos = new ArrayList<Curso>();
			if (filtro == null) {
				cursos = (ArrayList<Curso>) dao.getAll();
			} else {
				cursos = (ArrayList<Curso>) dao.getFiltered("%" + filtro + "%");
			}

			response = Response.status(Status.OK).entity(cursos).build();
		} catch (Exception e) {
			e.printStackTrace();
			ArrayList<String> errores = new ArrayList<String>();
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
		}
		return response;
	}

	@GET
	@Path("/profesor")
	public Object getAllWithoutProfessor(@QueryParam("filtro") String filtro) {
		LOGGER.info("getAll " + filtro);

		Response response;
		try {
			ArrayList<Curso> cursos = new ArrayList<Curso>();
			if (filtro == null) {
				System.out.println("profesor sin filtro");
				cursos = (ArrayList<Curso>) dao.getAllWithoutProfessor();
			} else {
				System.out.println(("profesor con filtro"));
				cursos = (ArrayList<Curso>) dao.getFilteredWithoutProfessor("%" + filtro + "%");
			}

			response = Response.status(Status.OK).entity(cursos).build();
		} catch (Exception e) {
			e.printStackTrace();
			ArrayList<String> errores = new ArrayList<String>();
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
		}
		return response;
	}

	@PUT
	@Path("/{idCurso}/profesor/{idProfesor}")
	public Object insertProfesor(@PathParam("idCurso") int idCurso, @PathParam("idProfesor") int idProfesor) {
		LOGGER.info("insertProfesor: idcurso " + idCurso + ", id profesor: " + idProfesor);
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		

		try {
			Curso c = CursoDao.getInstancia().getById(idCurso);
			Persona p = PersonaDao.getInstancia().getById(idProfesor);
			
			c.setProfesor(p);

			Curso curs = dao.update(c);
			response = Response.status(Status.OK).entity(curs).build();

		} catch (Exception e) {
			if (e.getMessage().contains("fk")) {
				errores.add("Hay algun problema de constrain");
				response = Response.status(Status.CONFLICT).entity(errores).build();
			} else {
				errores.add(e.getMessage());
				response = Response.status(Status.NOT_FOUND).entity(errores).build();
			}

		}
		return response;
	}
	
	@DELETE
	@Path("/{idCurso}/profesor/{idProfesor}")
	public Object deleteProfesor(@PathParam("idCurso") int idCurso, @PathParam("idProfesor") int idProfesor) {
		LOGGER.info("insertProfesor: idcurso " + idCurso + ", id profesor: " + idProfesor);
		ArrayList<String> errores = new ArrayList<String>();
		Response response = null;
		

		try {
			Curso c = CursoDao.getInstancia().getById(idCurso);
			Persona p = PersonaDao.getInstancia().getById(idProfesor);
			
			if(c.getProfesor().getId() != idProfesor) {
				throw new Exception("El profesor detallado no imparte ese curso. (fk.error)");
			}
			
			c.getProfesor().setId(0);

			Curso curs = dao.update(c);
			response = Response.status(Status.OK).entity(curs).build();

		} catch (Exception e) {
			if (e.getMessage().contains("fk")) {
				errores.add("Hay algun problema de constrain");
				response = Response.status(Status.CONFLICT).entity(errores).build();
			} else {
				errores.add(e.getMessage());
				response = Response.status(Status.NOT_FOUND).entity(errores).build();
			}

		}
		return response;
	}

}
