package com.ipartek.formacion.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ipartek.formacion.model.PersonaCurso;


public interface IDAOPersonaCurso<T> {
	
	List<T> getAll() throws Exception;
	
	/***
	 * busca cursos que tenga idPersona
	 * @param id
	 * @return
	 * @throws Si hay algun problema en la DDBB
	 */
	List<T> getPersonaCursos(int id) throws Exception;
	
	/***
	 * busca cursos que no tenga idPersona
	 * @param id
	 * @return
	 * @throws Si hay algun problema en la DDBB
	 */
	List<T> getPersonaNoCursos(int id) throws Exception;

	PersonaCurso addPersonaCurso(int idPersona, int idCurso) throws Exception, SQLException;

	PersonaCurso deletePersonaCurso(int idPersona, int idCurso) throws Exception, SQLException;
}
