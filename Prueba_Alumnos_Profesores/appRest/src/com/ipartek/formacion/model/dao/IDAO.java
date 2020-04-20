package com.ipartek.formacion.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T> {
	
	List<T> getAll() throws Exception;
	/***
	 * busca un pojo por su id
	 * @param id
	 * @return
	 * @throws Exception si no encuentra pojo
	 */
	T getById(int id) throws Exception;
	
	/***
	 * Elimina pojo por su id
	 * @param id
	 * @return
	 * @throws Exception si no encuentra id
	 * @throws SQLException si existe alguna constrain con otras tablas
	 */
	T delete(int id) throws Exception, SQLException;

	/***
	 * Crea un nuevo Pojo
	 * @param pojo
	 * @return el pojo con el id actualizado
	 * @throws Exception si no cumple las validaciones
	 * @throws SQLException si existe alguna constrain, por ejemplo UNIQUE_INDEX
	 */
	T insert(T pojo) throws Exception, SQLException;
	
	/***
	 * Modifica pojo
	 * @param pojo
	 * @return el pojo modificado
	 * @throws Exception si no pasa la validacion o si no existe el pojo modificar
	 * @throws SQLException si existe algun constrain
	 */
	T update(T pojo) throws Exception, SQLException;
}
