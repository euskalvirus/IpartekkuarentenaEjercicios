package com.ipartek.formacion.api.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Curso;
import com.ipartek.formacion.model.dao.CursoDao;

@Path("/cursos/")
@Produces("application/json")
@Consumes("application/json")
public class CursoController {

	private static final Logger LOGGER = Logger.getLogger(CursoController.class.getCanonicalName());
	
	@Context
	private ServletContext context;
	
	private CursoDao dao = (CursoDao) CursoDao.getInstancia();
	
	public CursoController() {}
	
	@GET
	public Object getAll(@QueryParam("filter") String filtro) {
		LOGGER.info("getAll " + filtro);
		
		Response response;
		try {
			ArrayList<Curso> cursos = new ArrayList<Curso>();
			if(filtro==null) {
				cursos = (ArrayList<Curso>)dao.getAll();
			}else {
				cursos = (ArrayList<Curso>)dao.getFiltered("%"+filtro+"%");
			}
			
			response = Response.status(Status.OK).entity(cursos).build();
		}catch (Exception e) {
			e.printStackTrace();
			ArrayList<String> errores = new ArrayList<String>();
			errores.add(e.getMessage());
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(errores).build();
		}
		return response;
	}
	
}
