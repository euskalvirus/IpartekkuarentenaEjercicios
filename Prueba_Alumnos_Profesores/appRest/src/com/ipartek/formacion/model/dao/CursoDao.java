package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ipartek.formacion.model.Curso;


public class CursoDao implements IDAO<Curso> {
	
	private static final Logger LOGGER = Logger.getLogger(PersonaDao.class.getCanonicalName());
	private ArrayList<Curso> registros;
	
	private final static String sql_get_all = "SELECT  id, nombre, imagen, precio FROM curso ORDER BY id ASC LIMIT 500";
	private final static String sql_get_by_id = "SELECT  id, nombre, imagen, precio FROM curso WHERE id= ?";
	
	private static CursoDao INSTANCIA = null;
	
	private CursoDao() {}
	
	public static IDAO<Curso> getInstancia() {
		LOGGER.info("getInstancia");
		if(INSTANCIA == null) {
			INSTANCIA = new CursoDao();
		}
		return INSTANCIA;
	}

	@Override
	public List<Curso> getAll() throws Exception {
		LOGGER.info("getAll");
		registros = new ArrayList<Curso>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_all);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				Curso c = mapper(rs);
				registros.add(c);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registros;
	}

	@Override
	public Curso getById(int id) throws Exception, SQLException {
		LOGGER.info("getById(" + id + ")");
		Curso curso = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_by_id);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				if (rs.next()) {
					curso = mapper(rs);
				} else {
					throw new Exception();
				}
			}

		} catch (SQLException e) {
			LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());

		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ningun registro para el id: " + id);
			e.printStackTrace();
			throw new Exception("No se ha encontrado ningun registro para el id: " + id);
		}
		return curso;
	}

	@Override
	public Curso delete(int id) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso insert(Curso pojo) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso update(Curso pojo) throws Exception, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	private Curso mapper(ResultSet rs) throws SQLException {
		LOGGER.info("mapper()");
		Curso curso = new Curso();
		curso.setId(rs.getInt("id"));
		curso.setNombre(rs.getString("nombre"));
		curso.setImagen(rs.getString("imagen"));
		curso.setPrecio(rs.getDouble("precio"));
		return curso;
	}
}
