package com.ipartek.formacion.api.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ipartek.formacion.model.Noticia;
import com.ipartek.formacion.model.dao.NoticiaDao;

@Path("/noticias")
@Produces("application/json")
@Consumes("application/json")
public class NoticiaController {

	private static final Logger LOGGER = Logger.getLogger(NoticiaController.class.getCanonicalName());

	@Context
	private ServletContext context;
	
	
	private NoticiaDao dao = (NoticiaDao) NoticiaDao.getInstancia();
	
	public NoticiaController() {}
	
	
	@GET
	public Object getAll() {
		LOGGER.info("getAll ");
		
		Response response;
		try {
			ArrayList<Noticia> cursos = new ArrayList<Noticia>();
			cursos = (ArrayList<Noticia>)dao.getAll();
			
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
