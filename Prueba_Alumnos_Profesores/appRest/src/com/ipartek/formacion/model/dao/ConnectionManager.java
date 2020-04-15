package com.ipartek.formacion.model.dao;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ipartek.formacion.api.controller.PersonaController;


public class ConnectionManager {

	private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getCanonicalName());
	private static Connection conn;

	public static Connection getConnection() {

		conn = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/personas");

			if (ds == null) {
				throw new Exception("Data source no encontrado!");
			}

			conn = ds.getConnection();

		} catch (Exception e) {
			
			//TODO LOOGER falta
			//LOGGER.fatal(e);
			e.printStackTrace();
		}

		return conn;

	}
	
}