/**
 *Clase PersonaDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Persona;
import utils.ConexBD;

public class PersonaDAO {
	private Connection conexion;

	public PersonaDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public Long insertar(Persona p) {
		String sql = "INSERT INTO persona(email,nombre,nacionalidad, idCredencial) VALUES (?,?,?,?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, p.getEmail());
			ps.setString(2, p.getNombre());
			ps.setString(3, p.getNacionalidad());
			ps.setLong(4, p.getIdCredencial());

			ps.executeUpdate();
			try (Statement sta = conexion.createStatement();
					ResultSet rs = sta.executeQuery(
							"SELECT MAX(id) AS id FROM persona")) {
				if (rs.next()) {
					return rs.getLong("id");
				}
			}
		} catch (SQLException e) {
			System.out.println("error insertando persona");

		}
		return null;
	}

	public Persona buscarId(Long id) {
		String sql = "SELECT id, email, nombre, nacionalidad, idCredencial FROM persona WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setLong(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Persona p = new Persona(rs.getLong("id"),
							rs.getString("email"), rs.getString("nombre"),
							rs.getString("nacionalidad"),
							rs.getLong("idCredencial"));
					return p;
				}

			}

		} catch (SQLException e) {
			System.out.println("Error buscando persona por el id");

		}
		return null;
	}

	public boolean emailExiste(String email) {
		String sql = "SELECT id FROM persona WHERE email=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}

		} catch (SQLException e) {
			System.out.println("Error comprobando el email");
		}
		return false;
	}

	public Persona actualizar(Persona p) {
		String sql = "UPDATE persona SET email=?, nombre=?, nacionalidad=?, idCredencial=? WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, p.getEmail());
			ps.setString(2, p.getNombre());
			ps.setString(3, p.getNacionalidad());
			ps.setLong(4, p.getIdCredencial());
			ps.setLong(5, p.getId());
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return buscarId(p.getId());
			}

		} catch (SQLException e) {
			System.out.println("Error actualizando persona");

		}
		return null;
	}

	public List<Persona> listaCoordinador() {
		List<Persona> lista = new ArrayList<>();
		String sql = "SELECT p.id, p.nombre FROM persona p JOIN credenciales c ON p.idCredencial = c.id WHERE c.perfil = 'COORDINACION' ORDER BY p.nombre";
		try (PreparedStatement ps = conexion.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Persona p = new Persona();
				p.setId(rs.getLong("id"));
				p.setNombre(rs.getString("nombre"));
				lista.add(p);
			}
		} catch (SQLException e) {
			System.out.println("Error obteniendo lista de coordinador");

		}
		return lista;
	}

	public Persona obtenerCoordinador(Long idCoord) {
		String sql = "SELECT p.id, p.nombre, p.email  FROM persona p WHERE p.id = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idCoord);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Persona p = new Persona();
					p.setId(rs.getLong("id"));
					p.setNombre(rs.getString("nombre"));
					p.setEmail(rs.getString("email"));

					return p;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error obteniendo coordinador ");
		}
		return null;
	}

	public Persona eliminar(Long id) {
		Persona p = buscarId(id);
		if (p == null) {
			return null;
		}
		String sql = "DELETE FROM persona WHERE id = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return p;
			}

		} catch (SQLException e) {

			System.out.println("Error eliminando la persona");

		}
		return null;

	}
}
