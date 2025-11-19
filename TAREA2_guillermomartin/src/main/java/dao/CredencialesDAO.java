/**
 *Clase CredencialesDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package dao;

import java.sql.Connection;

import utils.ConexBD;

public class CredencialesDAO {
private Connection conexion;

public CredencialesDAO() {
    this.conexion = ConexBD.getGestorConex().getConexion();
}

}
