package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.ipartek.formacion.model.Curso;
import com.ipartek.formacion.model.Persona;
import com.ipartek.formacion.model.Rol;

import java.sql.Statement;

public class PersonaDao implements IPersonaDAO {

	private static final Logger LOGGER = Logger.getLogger(PersonaDao.class.getCanonicalName());
	private ArrayList<Persona> registros;

	private final static String SQL_GET_ALL = "SELECT  id, nombre, avatar, sexo FROM persona ORDER BY id DESC LIMIT 500";
	private final static String SQL_GET_ALL_WITH_CURSOS = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, p.rol_id, rol_id, r.nombre rol_nombre, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id LEFT JOIN rol r ON p.rol_id = r.id";
	private final static String SQL_GET_ALL_WITH_CURSOS_BY_ROL_ID = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, p.rol_id rol_id, r.id rol_nombre, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio, c.persona_id profesor_id, prof.nombre profesor_nombre FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id LEFT JOIN rol r ON p.rol_id = r.id LEFT JOIN persona prof ON c.persona_id = prof.id WHERE p.rol_id = ?";
	private final static String SQL_GET_ALL_PROFESORES_WITH_CURSOS_BY_ROL_ID = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, p.rol_id rol_id, r.id rol_nombre, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio, c.persona_id profesor_id FROM persona p LEFT JOIN curso c ON c.persona_id = p.id LEFT JOIN rol r ON p.rol_id = r.id WHERE p.rol_id = ?";
	
	private final static String SQL_GET_BY_ID = "SELECT  id, nombre, avatar, sexo FROM persona WHERE id=?";
	private final static String SQL_GET_BY_ID_WITH_CURSOS = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id WHERE p.id = ?; ";
	private final static String SQL_GET_BY_ID_WITH_CURSOS_WITH_ROL = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, p.rol_id rol_id, r.id rol_nombre, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio, c.persona_id profesor_id, prof.nombre profesor_nombre FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id LEFT JOIN rol r ON p.rol_id = r.id LEFT JOIN persona prof ON c.persona_id = prof.id WHERE p.id = ?; ";
	
	private final static String SQL_GET_BY_NAME = "SELECT  id, nombre FROM persona WHERE nombre = ?";

	
	private final static String SQL_GET_PROFESOR_BY_ID_WITH_CURSOS_WITH_ROL = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, p.rol_id rol_id, r.id rol_nombre, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio, c.persona_id profesor_id FROM persona p LEFT JOIN curso c ON c.persona_id = p.id LEFT JOIN rol r ON p.rol_id = r.id WHERE p.id = ?; ";
	private final static String SQL_DELETE_BY_ID = "DELETE FROM persona WHERE id=?";
	private final static String SQL_INSERT = "INSERT INTO persona(nombre, avatar,sexo, rol_id) VALUES(?,?,?,?)";
	private final static String SQL_UPDATE = "UPDATE persona SET nombre=?, avatar=?, sexo=? WHERE id=?;";

	private static PersonaDao INSTANCIA = null;

	private PersonaDao() {
	}

	public static IDAO<Persona> getInstancia() {
		LOGGER.info("getInstancia");
		if (INSTANCIA == null) {
			INSTANCIA = new PersonaDao();
		}
		return INSTANCIA;
	}

	@Override
	public List<Persona> getAll() throws Exception {
		LOGGER.info("getAll");
		try (Connection con = ConnectionManager.getConnection();
				// PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_WITH_CURSOS);
				ResultSet rs = pst.executeQuery();) {
			HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
			while (rs.next()) {
				// Persona p = mapper(rs);
				mapperWithCursos(rs, hm);
			}
			registros = new ArrayList<Persona>(hm.values());

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registros;
	}

	@Override
	public Persona getById(int id) throws Exception {
		LOGGER.info("getById(" + id + ")");
		Persona persona = null;
		try (Connection con = ConnectionManager.getConnection();
				// PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);) {
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID_WITH_CURSOS_WITH_ROL);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
				if (rs.next()) {
					mapperWithCursos(rs, hm);
					while (rs.next()) {
						mapperWithCursos(rs, hm);
						// persona = mapper(rs);
						// persona = mapperWithCursos(rs);
					}
				} else {
					throw new SQLException();
				}

				persona = hm.get(id);
			}

		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ningun alumno para el id: " + id);
			e.printStackTrace();
			throw new Exception("No se ha encontrado ningun alumno para el id: " + id);
		}
		return persona;
	}

	@Override
	public Persona delete(int id) throws Exception, SQLException {
		LOGGER.info("delete(" + id + ")");
		Persona persona = getById(id);

		// No se deberia de dar el caso, porque en el getById se lanza throw si no
		// existe
		if (persona != null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(SQL_DELETE_BY_ID);) {
				pst.setInt(1, id);
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					LOGGER.warning(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=" + id + ".");
					throw new SQLException(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=" + id + ".");
				}

			} catch (SQLException e) {
				LOGGER.warning("No se puede eliminar, la persona tiene cursos relacionados.");
				throw new SQLException("No se puede eliminar, la persona tiene cursos relacionados.");
			}
		}

		return persona;
	}

	@Override
	public Persona insert(Persona persona) throws Exception, SQLException {
		LOGGER.info("insert(" + persona + ")");
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);) {
			pst.setString(1, persona.getNombre());
			pst.setString(2, persona.getAvatar());
			pst.setString(3, persona.getSexo());
			pst.setInt(4, persona.getRol().getId());
			int numeroRegistrosModificados = pst.executeUpdate();
			if (numeroRegistrosModificados != 1) {
				LOGGER.warning("Error no esperado, se ha guardado mas de un registro.");
				throw new Exception("Error no esperado, se ha guardado mas de un registro.");
			} else {
				ResultSet keys = pst.getGeneratedKeys();
				if (keys.next()) {
					persona.setId(keys.getInt(1));
					LOGGER.info("Persona Insert correctamente: " + persona);
				} else {
					LOGGER.warning("Hay algun error de constrain en la DDBB.");
					throw new Exception("Hay algun error de constrain en la DDBB.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 1062) {
				LOGGER.warning("El nombre introducido ya existe.");
				throw new SQLException("El nombre introducido ya existe.");
			} else {
				LOGGER.warning("Ha habido otro problema de constrain: " + e.getMessage());
				throw new SQLException("Ha habido otro problema de constrain: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warning(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return persona;
	}

	@Override
	public Persona update(Persona persona) throws Exception, SQLException {
		LOGGER.info("update(" + persona + ")");
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE);) {
			pst.setString(1, persona.getNombre());
			pst.setString(2, persona.getAvatar());
			pst.setString(3, persona.getSexo());
			pst.setInt(4, persona.getId());
			int numeroRegistrosModificados = pst.executeUpdate();
			if (numeroRegistrosModificados != 1) {
				LOGGER.warning("No se modificado ningun registro para el idAlumno=" + persona.getId() + ".");
				throw new Exception("No se modificado ningun registro para el idAlumno=" + persona.getId() + ".");
			}
			LOGGER.info("Persona UPDATE correctamente: " + persona);

		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 1062) {
				LOGGER.warning("El nombre introducido ya existe.");
				throw new SQLException("El nombre introducido ya existe.");
			} else {
				LOGGER.warning("Ha habido otro problema de constrain: " + e.getMessage());
				throw new SQLException("Ha habido otro problema de constrain: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return persona;
	}

	private Persona mapper(ResultSet rs) throws SQLException {
		LOGGER.info("mapper()");
		Persona persona = new Persona();
		persona.setId(rs.getInt("id"));
		persona.setNombre(rs.getString("nombre"));
		persona.setAvatar(rs.getString("avatar"));
		persona.setSexo(rs.getString("sexo"));
		return persona;
	}

	private void mapperWithCursos(ResultSet rs, HashMap<Integer, Persona> hm) throws SQLException {
		LOGGER.info("mapperWithCursos()");

		int key = rs.getInt("persona_id");

		Persona p = hm.get(key);

		if (p == null) {
			p = new Persona();
			p.setId(key);
			p.setNombre(rs.getString("persona_nombre"));
			p.setAvatar(rs.getString("persona_avatar"));
			p.setSexo(rs.getString("persona_sexo"));
			p.setRol(new Rol(rs.getInt("rol_id"), rs.getString("rol_nombre")));
		}

		int idCurso = rs.getInt("curso_id");
		if (idCurso != 0) {
			Curso c = new Curso();
			c.setId(rs.getInt("curso_id"));
			c.setNombre(rs.getString("curso_nombre"));
			c.setImagen(rs.getString("curso_imagen"));
			c.setPrecio(rs.getDouble("curso_precio"));

			if (rs.getInt("rol_id") == 1) {
				Persona prof = new Persona();
				prof.setId(rs.getInt("profesor_id"));
				prof.setNombre(rs.getString("profesor_nombre"));

				c.setProfesor(prof);
			}
			p.getCursos().add(c);
		}

		hm.put(key, p);
	}

	@Override
	public List<Persona> getAllByRol(Rol rol) throws Exception {
		LOGGER.info("getAllByRol");

		String sql_llamada = "";
		if (rol.getId() == 1) {
			sql_llamada = SQL_GET_ALL_WITH_CURSOS_BY_ROL_ID;
		} else if (rol.getId() == 2) {
			LOGGER.info("SQL_GET_ALL_PROFESORES_WITH_CURSOS_BY_ROL_ID");
			sql_llamada = SQL_GET_ALL_PROFESORES_WITH_CURSOS_BY_ROL_ID;
		}
		try (Connection con = ConnectionManager.getConnection();
				// PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				PreparedStatement pst = con.prepareStatement(sql_llamada);) {
			pst.setInt(1, rol.getId());
			ResultSet rs = pst.executeQuery();
			HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
			while (rs.next()) {
				// Persona p = mapper(rs);
				mapperWithCursos(rs, hm);
			}
			registros = new ArrayList<Persona>(hm.values());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registros;
	}

	public Persona getProfesorById(int id) throws Exception {
		LOGGER.info("getById(" + id + ")");
		Persona persona = null;
		try (Connection con = ConnectionManager.getConnection();
				// PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);) {
				PreparedStatement pst = con.prepareStatement(SQL_GET_PROFESOR_BY_ID_WITH_CURSOS_WITH_ROL);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
				if (rs.next()) {
					mapperWithCursos(rs, hm);
					while (rs.next()) {
						mapperWithCursos(rs, hm);
						// persona = mapper(rs);
						// persona = mapperWithCursos(rs);
					}
				} else {
					throw new SQLException();
				}

				persona = hm.get(id);
			}

		} catch (Exception e) {
			LOGGER.warning("No se ha encontrado ningun alumno para el id: " + id);
			e.printStackTrace();
			throw new Exception("No se ha encontrado ningun alumno para el id: " + id);
		}
		return persona;
	}

	public Boolean getByName(String filtro) throws Exception {
		LOGGER.info("getByName(" + filtro + ")");
		Persona persona = null;
		try (Connection con = ConnectionManager.getConnection();
				// PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);) {
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_NAME);) {
			pst.setString(1, filtro);
			try (ResultSet rs = pst.executeQuery();) {
				HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
				if (rs.next()) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			LOGGER.warning("Ha habido algun error");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

}
