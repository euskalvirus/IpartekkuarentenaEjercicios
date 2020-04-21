package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ipartek.formacion.model.Curso;
import com.ipartek.formacion.model.Persona;
import com.ipartek.formacion.model.PersonaCurso;

public class PersonaCursoDao implements IDAOPersonaCurso<Curso> {

	private static final Logger LOGGER = Logger.getLogger(PersonaCursoDao.class.getCanonicalName());
	private ArrayList<Curso> registrosCurso;
	private ArrayList<PersonaCurso> registrosPersonaCurso;

	private static PersonaCursoDao INSTANCIA = null;

	private final static String SQL_GET_ALL = "SELECT p.id idpersona, p.nombre nombrepersona, p.avatar avatarpersona, p.sexo sexo, c.id idcurso, c.nombre nombrecurso, c.imagen imagencurso, c.precio preciocurso, pc.precio_pagado precio_pagado " + 
			"FROM persona p, curso c, personacurso pc " + 
			"WHERE pc.persona_id = p.id AND pc.curso_id = c.id;";
	private final static String SQL_GET_BY_IDPERSONA = "SELECT c.id id, c.nombre nombre, c.imagen imagen, pc.precio_pagado precio FROM personacurso pc, curso c WHERE pc.curso_id = c.id AND pc.persona_id = ? ";
	private final static String SQL_GET_BY_NOTIDPERSONA = "SELECT c.id id, c.nombre nombre, c.imagen imagen, pc.precio_pagado precio FROM personacurso pc RIGHT JOIN curso c on pc.curso_id = c.id WHERE c.id NOT IN (SELECT curso_id FROM personacurso WHERE persona_id = ?) GROUP BY id";
	private final static String SQL_ADD_PERSONACURSO =  "INSERT INTO personacurso VALUES(?,?,?)";
	private final static String SQL_DELETE_PERSONACURSO =  "DELETE FROM personacurso WHERE persona_id = ? AND curso_id = ?";

	private PersonaCursoDao() {
	}

	public static PersonaCursoDao getInstancia() {
		LOGGER.info("getInstancia");
		if (INSTANCIA == null) {
			INSTANCIA = new PersonaCursoDao();
		}
		return INSTANCIA;
	}

	@Override
	public List<PersonaCurso> getAll() throws Exception {
		LOGGER.info("getAll");
		registrosPersonaCurso = new ArrayList<PersonaCurso>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				PersonaCurso pc = mapperPersonaCurso(rs);
				registrosPersonaCurso.add(pc);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registrosPersonaCurso;
	}



	@Override
	public List<Curso> getPersonaCursos(int id) throws Exception {
		LOGGER.info("getPersonaCursos(" + id + ")");
		ArrayList<Curso> cursos = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_IDPERSONA);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				cursos = new ArrayList<Curso>();
				while (rs.next()) {
					cursos.add(mapperCurso(rs));
				}
			}

		} catch (Exception e) {
			LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + id);
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return cursos;
	}

	@Override
	public List<Curso> getPersonaNoCursos(int id) throws Exception{
		LOGGER.info("getPersonaNoCursos(" + id + ")");
		ArrayList<Curso> cursos = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_NOTIDPERSONA);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				cursos = new ArrayList<Curso>();
				while (rs.next()) {
					cursos.add(mapperCurso(rs));
				}
			}

		} catch (Exception e) {
			LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + id);
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return cursos;
	}
	
	private PersonaCurso mapperPersonaCurso(ResultSet rs) throws SQLException {
		PersonaCurso pc = new PersonaCurso();
		new PersonaCurso(mapperPersona(rs), mapperCurso(rs));
		
		return pc;
	}
	
	private Persona mapperPersona(ResultSet rs) throws SQLException {
		LOGGER.info("mapper()");
		Persona persona = new Persona();
		persona.setId(rs.getInt("id"));
		persona.setNombre(rs.getString("nombre"));
		persona.setAvatar(rs.getString("avatar"));
		persona.setSexo(rs.getString("sexo"));
		return persona;
	}

	private Curso mapperCurso(ResultSet rs) throws SQLException {
		LOGGER.info("mapper()");
		Curso curso = new Curso();
		curso.setId(rs.getInt("id"));
		curso.setNombre(rs.getString("nombre"));
		curso.setImagen(rs.getString("imagen"));
		curso.setPrecio(rs.getDouble("precio"));
		return curso;
	}

	@Override
	public PersonaCurso addPersonaCurso(int idPersona, int idCurso) throws Exception, SQLException {
		LOGGER.info("addPersonaCurso(idPersona: " + idPersona + ", idCurso: " + idCurso + ")");
		Persona persona = PersonaDao.getInstancia().getById(idPersona);
		Curso curso = CursoDao.getInstancia().getById(idCurso);
		PersonaCurso pc = null;
		
		//No se deberia de dar el caso, porque en los getById  se lanza throw si no existe
		if (persona != null && curso !=null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(SQL_ADD_PERSONACURSO);) {
				pst.setInt(1, idPersona);
				pst.setInt(2, idCurso);
				pst.setDouble(3, curso.getPrecio());
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					LOGGER.warning(
							"Error no esperado. Se ha agregado mas de un registro");
					throw new SQLException(
							"Error no esperado. Se ha agregado mas de un registro");
				}
				pc = new PersonaCurso(persona,curso,curso.getPrecio());
				LOGGER.info("Agregado correctamente el curso para la persona");

			} catch (SQLException e) {
				LOGGER.warning("El alumno ya tiene comprado ese curso: " + e.getMessage());
				throw new Exception("El alumno ya tiene comprado ese curso: ");
			}
		}else {
			if( persona == null && curso == null) {
				throw new Exception("No existen ni el alumno ni el curso introducidos");
			}else if(persona == null) {
				throw new Exception("No existe el alumno introducido");
			}else {
				throw new Exception("No existe el curso introducido");
			}
			
		}

		return pc;
	}

	@Override
	public PersonaCurso deletePersonaCurso(int idPersona, int idCurso) throws Exception,  SQLException {
		LOGGER.info("deletePersonaCurso(idPersona: " + idPersona + ", idCurso: " + idCurso + ")");
		Persona persona = PersonaDao.getInstancia().getById(idPersona);
		Curso curso = CursoDao.getInstancia().getById(idCurso);
		PersonaCurso pc = null;
		
		//No se deberia de dar el caso, porque en los getById  se lanza throw si no existe
		if (persona != null && curso !=null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(SQL_DELETE_PERSONACURSO);) {
				pst.setInt(1, idPersona);
				pst.setInt(2, idCurso);
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					LOGGER.warning(
							"Error no esperado. Se ha eliminado mas de un registro");
					throw new SQLException(
							"Error no esperado. Se ha eliminado mas de un registro");
				}
				pc = new PersonaCurso(persona,curso,curso.getPrecio());
				
				LOGGER.info("Eliminado correctamente el curso para la persona");
			} catch (SQLException e) {
				LOGGER.warning("No existe ningun registro que eliminar");
				throw new Exception("No existe ningun registro que eliminar");
			}
		}else {
			if( persona == null && curso == null) {
				throw new Exception("No existen ni el alumno ni el curso introducidos");
			}else if(persona == null) {
				throw new Exception("No existe el alumno introducido");
			}else {
				throw new Exception("No existe el curso introducido");
			}
		}

		return pc;
	}

}
