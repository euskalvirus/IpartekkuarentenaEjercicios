package com.ipartek.formacion.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ipartek.formacion.model.Curso;

public interface IDAOPersonaCurso<T> {
	
	List<T> getAll() throws Exception;
	
	/***
	 * busca cursos que tenga idPersona
	 * @param id
	 * @return
	 * @throws Exception si no encuentra cursos
	 */
	List<T> getPersonaCursos(int id) throws Exception, SQLException;
	
	/***
	 * busca cursos que no tenga idPersona
	 * @param id
	 * @return
	 * @throws Exception si no encuentra cursos
	 */
	List<T> getPersonaNoCursos(int id) throws Exception, SQLException;
}
