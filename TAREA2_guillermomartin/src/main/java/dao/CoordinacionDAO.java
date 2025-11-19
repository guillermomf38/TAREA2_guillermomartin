/**
 *Clase CoordinacionDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modelo.Coordinacion;
import utils.ConexBD;

public class CoordinacionDAO {
	private Connection conexion;

	public CoordinacionDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();

	}

	public Long insertar(Coordinacion c) {
		String sql = "INSERT INTO coordinacion(senior,fechasenior) VALUES (?,?)";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setBoolean(1, c.isSenior());
			ps.setDate(2,c.getFechasenior() != null? Date.valueOf(c.getFechasenior()): null);
			ps.executeUpdate();

			Statement sta = conexion.createStatement();
			ResultSet rs = sta.executeQuery("SELECT MAX(idCoord) AS id From coordinacion");

			if (rs.next()) {
				return rs.getLong("id");
			}
		} catch (SQLException e) {
			System.out.println("Error insertnado coordinador");
		}
		return null;
	}
}
