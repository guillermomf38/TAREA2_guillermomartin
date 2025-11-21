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

import java.util.ArrayList;
import java.util.List;
import modelo.Espectaculo;
import modelo.Numero;
import utils.ConexBD;

public class EspectaculoDAO {

	private Connection conexion;

	public EspectaculoDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public Long insertar(Espectaculo e) {
		String sql = "INSERT INTO espectaculo (id, nombre, fechaini, fechafin, idCoord) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, e.getId());
			ps.setString(2, e.getNombre());
			ps.setDate(3, Date.valueOf(e.getFechaini()));
			ps.setDate(4, Date.valueOf(e.getFechafin()));
			ps.setLong(5, e.getIdCoord());
			ps.executeUpdate();

			return e.getId();
		} catch (SQLException ex) {
			System.out.println("Error insertando espectaculo");
		}
		return null;
	}

	public Espectaculo buscarPorId(Long id) {
		String sql = "SELECT id, nombre, fechaini, fechafin, idCoord FROM espectaculo WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Espectaculo e = new Espectaculo();
					e.setId(rs.getLong("id"));
					e.setNombre(rs.getString("nombre"));
					e.setFechaini(rs.getDate("fechaini").toLocalDate());
					e.setFechafin(rs.getDate("fechafin").toLocalDate());
					e.setIdCoord(rs.getLong("idCoord"));
					NumeroDAO numDAO = new NumeroDAO();
					List<Numero> numeros = numDAO.listarPorEspectaculo(id);
					e.setNumeros(numeros);

					return e;
				}
			}
		} catch (SQLException ex) {
			System.out.println("Error buscando espectaculo");
		}
		return null;
	}

	public Espectaculo actualizar(Espectaculo e) {
		String sql = "UPDATE espectaculo SET nombre=?, fechaini=?, fechafin=?, idCoord=? WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getNombre());
			ps.setDate(2, Date.valueOf(e.getFechaini()));
			ps.setDate(3, Date.valueOf(e.getFechafin()));
			ps.setLong(4, e.getIdCoord());
			ps.setLong(5, e.getId());
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return buscarPorId(e.getId());
			}
		} catch (SQLException ex) {
			System.out.println("Error actualizando espectáculo");
		}
		return null;
	}

	public Espectaculo eliminar(Long id) {
		Espectaculo e = buscarPorId(id);
		if (e == null) {
			return null;
		}
		String sql = "DELETE FROM espectaculo WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return e;
			}
		} catch (SQLException ex) {
			System.out.println("Error eliminando espectaculo");
		}
		return null;
	}

	public Espectaculo buscarInfoBasico(Long id) {
		String sql = "SELECT id, nombre FROM espectaculo WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Espectaculo e = new Espectaculo();
					e.setId(rs.getLong("id"));
					e.setNombre(rs.getString("nombre"));
					return e;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error buscando espectaculo ");
		}
		return null;
	}

	public List<Espectaculo> listarEspectaculos() {
		List<Espectaculo> listaEspec = new ArrayList<>();
		String sql = "SELECT id, nombre, fechaini, fechafin, idCoord FROM espectaculo ORDER BY fechaini";
		try (PreparedStatement ps = conexion.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Espectaculo e = new Espectaculo();
				e.setId(rs.getLong("id"));
				e.setNombre(rs.getString("nombre"));
				e.setFechaini(rs.getDate("fechaini").toLocalDate());
				e.setFechafin(rs.getDate("fechafin").toLocalDate());
				e.setIdCoord(rs.getLong("idCoord"));
				listaEspec.add(e);
			}
		} catch (SQLException ex) {
			System.out.println("Error listando espectaculos");
		}
		return listaEspec;
	}

}
