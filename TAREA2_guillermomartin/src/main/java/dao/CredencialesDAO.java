/**
 *Clase CredencialesDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Credenciales;
import modelo.Perfiles;
import utils.ConexBD;

public class CredencialesDAO {
	private Connection conexion;

	public CredencialesDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public Long insertar(Credenciales c) {
		String sql = "INSERT INTO credenciales (id, nombre, password, perfil) VALUES (?, ?,?,?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, c.getId());
			ps.setString(2, c.getNombre());
			ps.setString(3, c.getPassword());
			ps.setString(4, c.getPerfil().toString());
			ps.executeUpdate();
			return c.getId();
		} catch (SQLException e) {
			System.out.println("Error insertando credenciales");
		}
		return null;
	}

	public Credenciales buscarPorNombre(String nombre) {
		String sql = "SELECT id, nombre, password, perfil FROM credenciales WHERE nombre=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, nombre);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Credenciales(rs.getLong("id"),
							rs.getString("nombre"), rs.getString("password"),
							Perfiles.valueOf(rs.getString("perfil")));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error buscando credenciales por nombre");
		}
		return null;
	}

	public Credenciales buscarPorId(Long id) {
		String sql = "SELECT id, nombre, password, perfil FROM credenciales WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Credenciales(rs.getLong("id"),
							rs.getString("nombre"), rs.getString("password"),
							Perfiles.valueOf(rs.getString("perfil")));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error buscando credenciales por id");
		}
		return null;
	}

	public Credenciales actualizar(Credenciales c) {
		String sql = "UPDATE credenciales SET nombre=?, password=?, perfil=? WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, c.getNombre());
			ps.setString(2, c.getPassword());
			ps.setString(3, c.getPerfil().toString());
			ps.setLong(4, c.getId());
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return buscarPorId(c.getId());
			}
		} catch (SQLException e) {
			System.out.println("Error actualizando credenciales");
		}
		return null;
	}

	public Credenciales eliminar(Long id) {
		Credenciales c = buscarPorId(id);
		if (c == null) {
			return null;
		}
		String sql = "DELETE FROM credenciales WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return c;
			}
		} catch (SQLException e) {
			System.out.println("Error eliminando credenciales");
		}
		return null;
	}

}
