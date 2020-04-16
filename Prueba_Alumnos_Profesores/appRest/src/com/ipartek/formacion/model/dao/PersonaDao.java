package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ipartek.formacion.model.Persona;

import java.sql.Statement;

public class PersonaDao implements IDAO<Persona> {

	private ArrayList<Persona> registros;

	private final static String sql_get_all = "SELECT  id, nombre, avatar, sexo FROM persona ORDER BY id DESC LIMIT 500";
	private final static String sql_get_by_id = "SELECT  id, nombre, avatar, sexo FROM persona WHERE id=?";
	private final static String sql_delete_by_id = "DELETE FROM persona WHERE id=?";
	private final static String sql_insert = "INSERT INTO persona(nombre, avatar,sexo) VALUES(?,?,?)";
	private final static String sql_update = "UPDATE persona SET nombre=?, avatar=?, sexo=? WHERE id=?;";

	private static PersonaDao INSTANCIA = null;

	private PersonaDao() {
	}

	public static IDAO<Persona> getInstancia() {
		if (INSTANCIA == null) {
			INSTANCIA = new PersonaDao();
		}
		return INSTANCIA;
	}

	@Override
	public List<Persona> getAll() throws Exception {
		registros = new ArrayList<Persona>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_all);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				Persona p = mapper(rs);
				registros.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}
		return registros;
	}

	@Override
	public Persona getById(int id) throws Exception, SQLException {
		Persona persona = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_by_id);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				if (rs.next()) {
					persona = mapper(rs);
				} else {
					throw new Exception();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());

		} catch (Exception e) {
			throw new Exception("No se ha encontrado ningun registro para el id: " + id);
		}
		return persona;
	}

	@Override
	public Persona delete(int id) throws Exception, SQLException {
		Persona persona = getById(id);
		if (persona != null) {
			try (Connection con = ConnectionManager.getConnection();
					PreparedStatement pst = con.prepareStatement(sql_delete_by_id);) {
				pst.setInt(1, id);
				int numeroRegistrosModificados = pst.executeUpdate();
				if (numeroRegistrosModificados != 1) {
					throw new SQLException(
							"El alumno existe pero no se ha podido eliminar el registro para el Alumno=" + id + ".");
				}

			} catch (SQLException e) {
				throw new Exception("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
			}
		}

		return persona;
	}

	@Override
	public Persona insert(Persona persona) throws Exception, SQLException {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_insert, Statement.RETURN_GENERATED_KEYS);) {
			pst.setString(1, persona.getNombre());
			pst.setString(2, persona.getAvatar());
			pst.setString(3, persona.getSexo());
			int numeroRegistrosModificados = pst.executeUpdate();
			if (numeroRegistrosModificados != 1) {
				throw new Exception("No se ha podido agregar el registro.");
			} else {
				ResultSet keys = pst.getGeneratedKeys();
				if (keys.next()) {
					persona.setId(keys.getInt(1));
				}else {
					throw new Exception(
							"Hay algun error de constrain en la DDBB.");
				}
			}

		}catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return persona;
	}

	@Override
	public Persona update(Persona persona) throws Exception, SQLException {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_update);) {
			pst.setString(1, persona.getNombre());
			pst.setString(2, persona.getAvatar());
			pst.setString(3, persona.getSexo());
			pst.setInt(4, persona.getId());
			int numeroRegistrosModificados = pst.executeUpdate();
			if (numeroRegistrosModificados != 1) {
				throw new Exception("No se modificado ningun registro para el idAlumno=" + persona.getId() + ".");
			}

		}catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Ha habido algun problema con la conexion DDBB: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return persona;
	}

	private Persona mapper(ResultSet rs) throws SQLException {
		Persona persona = new Persona();
		persona.setId(rs.getInt("id"));
		persona.setNombre(rs.getString("nombre"));
		persona.setAvatar(rs.getString("avatar"));
		persona.setSexo(rs.getString("sexo"));
		return persona;
	}

}
