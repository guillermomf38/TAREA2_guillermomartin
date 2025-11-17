/**
 *Clase PersonaDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */


package dao;

import java.sql.Connection;

import utils.ConexBD;

public class PersonaDAO {
	private Connection conexion;

	public PersonaDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}
}
