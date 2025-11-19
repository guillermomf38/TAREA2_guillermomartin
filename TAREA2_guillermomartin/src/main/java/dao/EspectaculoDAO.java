/**
 *Clase EspectaculoDAO.java
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
import java.util.ArrayList;
import java.util.List;

import modelo.Espectaculo;
import utils.ConexBD;

public class EspectaculoDAO {

	private Connection conexion;

	public EspectaculoDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public Long insertar(Espectaculo e) {
		String sql = "INSERT INTO espectaculo(nombre,fechaini,fechafin,idCoord) VALUES (?,?,?,?)";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, e.getNombre());
			ps.setDate(2, Date.valueOf(e.getFechaini()));
			ps.setDate(3, Date.valueOf(e.getFechafin()));
			ps.setLong(4, e.getIdCoord());

			ps.executeUpdate();
			Statement sta = conexion.createStatement();
			ResultSet rs = sta
					.executeQuery("SELECT MAX(id) AS id From espectaculo");

			if (rs.next()) {
				return rs.getLong("id");
			}
		} catch (SQLException ex) {
			System.out.println("Error insertando espectaculo");

		}
		return null;
	}

	public List<Espectaculo> listaEspectaculo() {

		List<Espectaculo> lista = new ArrayList<>();

		String sql = "SELECT * FROM espectaculo ORDER BY nombre";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Espectaculo e = new Espectaculo(rs.getLong("id"),
						rs.getString("nombre"),
						rs.getDate("fechaini").toLocalDate(),
						rs.getDate("fechafin").toLocalDate(),
						rs.getLong("idCoord"));
				lista.add(e);
			}

		} catch (SQLException e) {
			System.out.println("Error listando espectaculos");

		}

		return lista;
	}
}
