/**
 *Clase ParticipaDAO.java
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

import utils.ConexBD;

public class ParticipaDAO {
	private Connection conexion;

	public ParticipaDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public boolean insertar(Long idNumero, Long idArtista) {
		String sql = "INSERT INTO participa (idNumero, idArt) VALUES (?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idNumero);
			ps.setLong(2, idArtista);
			int filas = ps.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.out.println("Error insertando participacion");
		}
		return false;
	}

	public boolean eliminarPorNumero(Long idNumero) {
		String sql = "DELETE FROM participa WHERE idNumero=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idNumero);
			int filas = ps.executeUpdate();
			return filas > 0;
		} catch (SQLException ex) {
			System.out.println("Error eliminando participaciones de numero ");
		}
		return false;
	}

	public boolean eliminarPorArtista(Long idArtista) {
		String sql = "DELETE FROM participa WHERE idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArtista);
			int filas = ps.executeUpdate();
			return filas > 0;
		} catch (SQLException ex) {
			System.out.println("Error eliminando participaciones de artista");
		}
		return false;
	}

	public List<Long> listarArtistasPorNumero(Long idNumero) {
		List<Long> artistas = new ArrayList<>();
		String sql = "SELECT idArt FROM participa WHERE idNumero=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idNumero);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					artistas.add(rs.getLong("idArt"));
				}
			}
		} catch (SQLException ex) {
			System.out.println("Error listando artistas de numero");
		}
		return artistas;
	}

	public List<Long> listarNumerosPorArtista(Long idArtista) {
		List<Long> numeros = new ArrayList<>();
		String sql = "SELECT idNumero FROM participa WHERE idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArtista);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					numeros.add(rs.getLong("idNumero"));
				}
			}
		} catch (SQLException ex) {
			System.out.println("Error listando numeros de artista ");
		}
		return numeros;
	}

}
