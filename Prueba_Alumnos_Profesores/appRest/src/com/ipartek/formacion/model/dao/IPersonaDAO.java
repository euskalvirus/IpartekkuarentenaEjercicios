package com.ipartek.formacion.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ipartek.formacion.model.Persona;
import com.ipartek.formacion.model.Rol;

public interface IPersonaDAO extends IDAO<Persona> {
	
	static final String EXCEPTION_ROL = "No existe el rol o es nulo";
	static final String EXCEPTION_PERSONA_NO_EXISTE = "No existe la persona";
	static final String EXCEPTION_CURSO_NO_EXISTE = "No existe el curso";
	static final String EXCEPTION_CURSO_DUPLICADO = "Ya esta asociado el curso a la persona";
	
	/**
	 * 
	 * @param rol
	 * @return
	 * @throws Exception Si rol == null o No existe
	 */
	List<Persona> getAllByRol(Rol rol) throws Exception;
}
