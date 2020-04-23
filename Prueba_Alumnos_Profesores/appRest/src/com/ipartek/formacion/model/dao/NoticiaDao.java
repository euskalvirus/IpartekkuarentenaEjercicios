package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ipartek.formacion.model.Noticia;

public class NoticiaDao implements IDAO<Noticia> {
	
	private static final Logger LOGGER = Logger.getLogger(PersonaDao.class.getCanonicalName());
	private ArrayList<Noticia> registros;
	
	private static final String SQL_GET_ALL = "SELECT id, titulo, fecha, contenido FROM noticia";
	private static final String SQL_GET_BY_ID = "SELECT id, titulo, fecha, contenido FROM noticia WHERE id=?";
	
	private static NoticiaDao INSTANCIA;
	
	private NoticiaDao() {}
	
	public static IDAO<Noticia> getInstancia() {
		LOGGER.info("getInstancia");
		
		if(INSTANCIA == null) {
			INSTANCIA = new NoticiaDao();
		}
		return INSTANCIA;
		
	}

	@Override
	public List<Noticia> getAll() throws Exception {
		LOGGER.info("getAll");
		registros = new ArrayList<Noticia>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				Noticia n = mapper(rs);
				registros.add(n);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registros;
	}

	@Override
	public Noticia getById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Noticia delete(int id) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Noticia insert(Noticia pojo) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Noticia update(Noticia pojo) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Noticia mapper(ResultSet rs) throws SQLException {
		LOGGER.info("mapper()");
		Noticia noticia = new Noticia();
		noticia.setId(rs.getInt("id"));
		noticia.setTitulo(rs.getString("titulo"));
		noticia.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
		noticia.setContenido(rs.getString("contenido"));
		return noticia;
	}

}
