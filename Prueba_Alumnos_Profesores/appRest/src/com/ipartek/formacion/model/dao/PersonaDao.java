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

import java.sql.Statement;

public class PersonaDao implements IDAO<Persona> {

	private static final Logger LOGGER = Logger.getLogger(PersonaDao.class.getCanonicalName());
	private ArrayList<Persona> registros;

	private final static String SQL_GET_ALL = "SELECT  id, nombre, avatar, sexo FROM persona ORDER BY id DESC LIMIT 500";
	private final static String SQL_GET_ALL_WITH_CURSOS = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id";
	private final static String SQL_GET_BY_ID = "SELECT  id, nombre, avatar, sexo FROM persona WHERE id=?";
	private final static String SQL_GET_BY_ID_WITH_CURSOS = "SELECT p.id persona_id, p.nombre persona_nombre, p.avatar persona_avatar, p.sexo persona_sexo, c.id curso_id, c.nombre curso_nombre, c.imagen curso_imagen, c.precio curso_precio FROM (persona p LEFT JOIN personacurso pc ON p.id = pc.persona_id) LEFT JOIN curso c ON pc.curso_id =  c.id WHERE p.id = ?; ";
	private final static String SQL_DELETE_BY_ID = "DELETE FROM persona WHERE id=?";
	private final static String SQL_INSERT = "INSERT INTO persona(nombre, avatar,sexo) VALUES(?,?,?)";
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
				//PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_WITH_CURSOS);
				ResultSet rs = pst.executeQuery();) {
			HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
			while (rs.next()) {
				//Persona p = mapper(rs);
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
				//PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);) {
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID_WITH_CURSOS);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				HashMap<Integer, Persona> hm = new HashMap<Integer, Persona>();
				while (rs.next()) {
					mapperWithCursos(rs, hm);
					//persona = mapper(rs);
					//persona = mapperWithCursos(rs);
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
		
		//No se deberia de dar el caso, porque en el getById se lanza throw si no existe
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
		
		int key  = rs.getInt("persona_id");
		
		Persona p = hm.get(key);
		
		if(p == null) {
			p = new Persona();
			p.setId(key);
			p.setNombre(rs.getString("persona_nombre"));
			p.setAvatar(rs.getString("persona_avatar"));
			p.setSexo(rs.getString("persona_sexo"));
		}
		
		int idCurso = rs.getInt("curso_id");
		if(idCurso!=0) {
			Curso c= new Curso();
			c.setId(rs.getInt("curso_id"));
			c.setNombre(rs.getString("curso_nombre"));
			c.setImagen(rs.getString("curso_imagen"));
			c.setPrecio(rs.getDouble("curso_precio"));
			
			p.getCursos().add(c);
		}
		
		
		hm.put(key, p);
	}

}
