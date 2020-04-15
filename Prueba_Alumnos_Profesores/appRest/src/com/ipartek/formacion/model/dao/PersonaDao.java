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

	private ArrayList<Persona> registros = new ArrayList<Persona>();

	private final static String sql_get_all = "SELECT  id, nombre, avatar, sexo FROM persona ORDER BY id DESC LIMIT 500";
	private final static String sql_get_by_id = "SELECT  id, nombre, avatar, sexo FROM persona WHERE id=?";
	private final static String sql_delete_by_id = "DELETE FROM persona WHERE id=?";
	private final static String sql_insert = "INSERT INTO persona(nombre, avatar,sexo) VALUES(?,?,?)";
	private final static String sql_update = "UPDATE persona SET nombre=?, avatar=?, sexo=? WHERE id=?;";

	@Override
	public List<Persona> getAll() {

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_all);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				Persona p = new Persona();
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setAvatar(rs.getString("avatar"));
				p.setSexo(rs.getString("sexo"));

				registros.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return registros;
	}

	@Override
	public Persona getById(int id) throws Exception {
		Persona persona = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql_get_by_id);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery();) {
				if (rs.next()) {
					persona = new Persona();
					persona.setId(rs.getInt("id"));
					persona.setNombre(rs.getString("nombre"));
					persona.setAvatar(rs.getString("avatar"));
					persona.setSexo(rs.getString("sexo"));
				}
			}

		} catch (Exception e) {
			throw new Exception("Ha habido algun problema con la conexion sql");
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
					throw new Exception("No se Eliminado ningun registro para el Alumno=" + id + ".");
				}

			} catch (Exception e) {
				throw new Exception("No se Eliminado ningun registro para el Alumno=" + id + ".");
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
			pst.setString(3,persona.getSexo());
			int numeroRegistrosModificados = pst.executeUpdate();
			if (numeroRegistrosModificados != 1) {
				persona = null;
			} else {
				ResultSet keys = pst.getGeneratedKeys();
				if (keys.next()) {
					persona.setId(keys.getInt(1));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ha habido algun problema con la conexion");
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

		} catch (Exception e) {
			throw new Exception("Ha habido un problema con la conexión sql");
		}
		return persona;
	}

}
