/**
 *Clase NumeroDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Artista;
import modelo.Numero;
import utils.ConexBD;

public class NumeroDAO {
	private Connection conexion;

	public NumeroDAO() {

		this.conexion = ConexBD.getGestorConex().getConexion();

	}

	public Long insertar(Numero n) {
		String sql = "INSERT INTO numero (id, orden, nombre, duracion, idEspectaculo) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, n.getId());
			ps.setInt(2, n.getOrden());
			ps.setString(3, n.getNombre());
			ps.setDouble(4, n.getDuracion());
			ps.setLong(5, n.getIdEspectaculo());
			ps.executeUpdate();
			return n.getId();
		} catch (SQLException e) {
			System.out.println("Error insertando numero");
		}
		return null;
	}

	public Numero buscarPorId(Long id) {
		String sql = "SELECT id, orden, nombre, duracion, idEspectaculo FROM numero WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Numero n = new Numero();
					n.setId(rs.getLong("id"));
					n.setOrden(rs.getInt("orden"));
					n.setNombre(rs.getString("nombre"));
					n.setDuracion(rs.getDouble("duracion"));
					n.setIdEspectaculo(rs.getLong("idEspectaculo"));

					ArtistaDAO artistaDAO = new ArtistaDAO();
					n.setArtistas(artistaDAO.listarPorNumero(id));

					return n;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error buscando numero");
		}
		return null;
	}

	public Numero actualizar(Numero n) {
		String sql = "UPDATE numero SET orden=?, nombre=?, duracion=?, idEspectaculo=? WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setInt(1, n.getOrden());
			ps.setString(2, n.getNombre());
			ps.setDouble(3, n.getDuracion());
			ps.setLong(4, n.getIdEspectaculo());
			ps.setLong(5, n.getId());
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return buscarPorId(n.getId());
			}
		} catch (SQLException e) {
			System.out.println("Error actualizando numero");
		}
		return null;
	}

	public Numero eliminar(Long id) {
		Numero n = buscarPorId(id);
		if (n == null) {
			return null;
		}
		String sql = "DELETE FROM numero WHERE id=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, id);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return n;
			}
		} catch (SQLException e) {
			System.out.println("Error eliminando numero");
		}
		return null;
	}

	public List<Numero> listarPorEspectaculo(Long idEspectaculo) {
		List<Numero> lista = new ArrayList<>();
		String sql = "SELECT id, orden, nombre, duracion, idEspectaculo FROM numero WHERE idEspectaculo=? ORDER BY orden";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idEspectaculo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Numero n = new Numero();
					n.setId(rs.getLong("id"));
					n.setOrden(rs.getInt("orden"));
					n.setNombre(rs.getString("nombre"));
					n.setDuracion(rs.getDouble("duracion"));
					n.setIdEspectaculo(rs.getLong("idEspectaculo"));
					lista.add(n);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error listando numeros de espectaculo");
		}
		return lista;
	}

	public List<Artista> obtenerArtistas(Long idNumero) {
		ParticipaDAO participaDAO = new ParticipaDAO();
		List<Long> idsArtistas = participaDAO.listarArtistasPorNumero(idNumero);

		ArtistaDAO artistaDAO = new ArtistaDAO();
		List<Artista> artistas = new ArrayList<>();
		for (Long idArt : idsArtistas) {
			Artista a = artistaDAO.buscarPorId(idArt);
			if (a != null) {
				artistas.add(a);
			}
		}
		return artistas;
	}
}

