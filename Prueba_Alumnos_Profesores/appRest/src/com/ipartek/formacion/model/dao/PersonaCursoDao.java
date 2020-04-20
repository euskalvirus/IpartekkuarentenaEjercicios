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
	private ArrayList<Curso> registros;

	private static PersonaCursoDao INSTANCIA = null;

	private final static String sql_get_all = "SELECT id, nombre, imagen, precio FROM curso";
	private final static String sql_get_by_idpersona = "SELECT c.id id, c.nombre nombre, c.imagen imagen, pc.precio_pagado precio FROM personacurso pc, curso c WHERE pc.curso_id = c.id AND pc.persona_id = ? ";
	private final static String sql_get_by_notidpersona = "SELECT c.id id, c.nombre nombre, c.imagen imagen, pc.precio_pagado precio FROM personacurso pc RIGHT JOIN curso c on pc.curso_id = c.id WHERE c.id NOT IN (SELECT curso_id FROM personacurso WHERE persona_id = ?) GROUP BY id";
	private final static String sql_add_personacurso =  "INSERT INTO personacurso VALUES(?,?,?)";
	private final static String sql_delete_personacurso =  "DELETE FROM personacurso WHERE persona_id = ? AND curso_id = ?";

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
	public List<Curso> getPersonaCursos(int id) throws Exception, SQLException {
		LOGGER.info("getPersonaCursos(" + id + ")");
		ArrayList<Curso> cursos = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_by_idpersona);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				cursos = new ArrayList<Curso>();
				while (rs.next()) {
					cursos.add(mapper(rs));
				}
			}

		} catch (SQLException e) {
			LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());

		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ningun registro para el idPersona: " + id);
			e.printStackTrace();
			throw new Exception("No se ha encontrado ningun registro para el id: " + id);
		}
		return cursos;
	}

	@Override
	public List<Curso> getPersonaNoCursos(int id) throws Exception, SQLException {
		LOGGER.info("getPersonaNoCursos(" + id + ")");
		ArrayList<Curso> cursos = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_by_notidpersona);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				cursos = new ArrayList<Curso>();
				while (rs.next()) {
					cursos.add(mapper(rs));
				}
			}

		} catch (SQLException e) {
			LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());

		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ningun registro para el idPersona: " + id);
			e.printStackTrace();
			throw new Exception("No se ha encontrado ningun registro para el id: " + id);
		}
		return cursos;
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

	public PersonaCurso addPersonaCurso(int idPersona, int idCurso) throws Exception, SQLException {
		LOGGER.info("addPersonaCurso(idPersona: " + idPersona + ", idCurso: " + idCurso + ")");
		Persona persona = PersonaDao.getInstancia().getById(idPersona);
		Curso curso = CursoDao.getInstancia().getById(idCurso);
		PersonaCurso pc = null;
		System.out.println("persona: " + persona);
		System.out.println("curso: " + curso);
		if (persona != null && curso !=null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(sql_add_personacurso);) {
				pst.setInt(1, idPersona);
				pst.setInt(2, idCurso);
				pst.setDouble(3, curso.getPrecio());
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					LOGGER.warning(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=" + idPersona + ", Curso=" + idCurso + ".");
					throw new SQLException(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=" + idPersona + ", Curso=" + idCurso + ".");
				}
				pc = new PersonaCurso(idPersona,idCurso,curso.getPrecio());

			} catch (SQLException e) {
				LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
				throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			}
		}else {
			
		}

		return pc;
	}

	public PersonaCurso deletePersonaCurso(int idPersona, int idCurso) throws Exception,  SQLException {
		LOGGER.info("deletePersonaCurso(idPersona: " + idPersona + ", idCurso: " + idCurso + ")");
		Persona persona = PersonaDao.getInstancia().getById(idPersona);
		Curso curso = CursoDao.getInstancia().getById(idCurso);
		PersonaCurso pc = null;
		if (persona != null && curso !=null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(sql_delete_personacurso);) {
				pst.setInt(1, idPersona);
				pst.setInt(2, idCurso);
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					LOGGER.warning(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=.");
					throw new SQLException(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=.");
				}
				pc = new PersonaCurso(idPersona,idCurso,curso.getPrecio());

			} catch (SQLException e) {
				LOGGER.warning("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
				throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			}
		}else {
			
		}

		return pc;
	}

}
