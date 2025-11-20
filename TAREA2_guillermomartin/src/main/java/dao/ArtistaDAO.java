/**
 *Clase ArtistaDAO.java
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
import modelo.Especialidad;
import modelo.Espectaculo;
import modelo.Numero;
import modelo.Persona;
import utils.ConexBD;

public class ArtistaDAO {
	private Connection conexion;

	public ArtistaDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();
	}

	public Long insertar(Artista a) {
		String sql = "INSERT INTO artista(idArt, apodo) values (?,?)";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, a.getIdArt());
			ps.setString(2, a.getApodo());
			ps.executeUpdate();
			return a.getIdArt();
		} catch (SQLException e) {
			System.out.println("Error insertando artista ");
		}
		return null;
	}

	public Artista buscarPorId(Long idArt) {

		String sql = "SELECT idArt, apodo FROM artista WHERE idArt = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArt);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Artista a = new Artista();
					a.setIdArt(rs.getLong("idArt"));
					a.setApodo(rs.getString("apodo"));
					PersonaDAO personaDAO = new PersonaDAO();
					Persona persona = personaDAO.buscarId(idArt);
					if (persona != null) {
					    a.setNombre(persona.getNombre());
					    a.setNacionalidad(persona.getNacionalidad());
					    a.setEmail(persona.getEmail());
					}

					a.setEspecialidades(obtenerEspecialidades(idArt));
					a.setNumParticipa(obtenerNumerosParticipa(idArt));
					return a;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error buscando artista");
		}
		return null;
	}

	public Artista actualizar(Artista a) {
		String sql = "UPDATE artista SET apodo=? WHERE idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, a.getApodo());
			ps.setLong(2, a.getIdArt());
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return buscarPorId(a.getIdArt());
			}
		} catch (SQLException e) {
			System.out.println("Error actualizando artista ");
		}
		return null;
	}

	public List<Artista> listarPorNumero(Long idNumero) {
		List<Artista> lista = new ArrayList<>();
		String sql = "SELECT a.idArt, a.apodo FROM artista a JOIN participa p ON a.idArt = p.idArt WHERE p.idNumero = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idNumero);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Long idArt = rs.getLong("idArt");
					Artista a = new Artista();
					a.setIdArt(idArt);
					a.setApodo(rs.getString("apodo"));

					PersonaDAO personaDAO = new PersonaDAO();
					Persona persona = personaDAO.buscarId(idArt);
					if (persona != null) {
						a.setNombre(persona.getNombre());
						a.setNacionalidad(persona.getNacionalidad());
					}
					a.setEspecialidades(obtenerEspecialidades(idArt));
					lista.add(a);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error listando artistas por numero ");
		}
		return lista;
	}

	public boolean asignarANumero(Long idArt, Long idNumero) {
		String sql = "INSERT INTO participa (idArt, idNumero) VALUES (?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArt);
			ps.setLong(2, idNumero);
			int filas = ps.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.out.println("Error asignando artista a numero ");
		}
		return false;
	}

	public Artista verFicha(Long idArt) {
	    Artista a = buscarPorId(idArt);
	    if (a != null) {
	        EspectaculoDAO espectaculoDAO = new EspectaculoDAO();
	        List<Numero> numerosParticipa = a.getNumParticipa();

	        for (Numero n : numerosParticipa) {
	            Espectaculo e = espectaculoDAO.buscarInfoBasico(n.getIdEspectaculo());
	            n.setEspectaculo(e);
	        }

	        a.setNumParticipa(numerosParticipa);
	    }

	    return a;
	}

	private List<Especialidad> obtenerEspecialidades(Long idArt) {
		List<Especialidad> lista = new ArrayList<>();
		String sql = "SELECT especialidad FROM especialidades_artista WHERE idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArt);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(
							Especialidad.valueOf(rs.getString("especialidad")));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error obteniendo especialidades ");
		}
		return lista;
	}

	private List<Numero> obtenerNumerosParticipa(Long idArt) {
		List<Numero> lista = new ArrayList<>();
		String sql = "SELECT n.id, n.orden, n.nombre, n.duracion, n.idEspectaculo FROM numero n JOIN participa p ON n.id = p.idNumero WHERE p.idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArt);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Numero n = new Numero(rs.getLong("id"), rs.getInt("orden"),
							rs.getString("nombre"), rs.getDouble("duracion"),
							rs.getLong("idEspectaculo"));
					lista.add(n);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error obteniendo numeros de artista: ");
		}
		return lista;
	}

	public Artista eliminar(Long idArt) {
		Artista a = buscarPorId(idArt);
		if (a == null) {
			return null;
		}
		String sql = "DELETE FROM artista WHERE idArt=?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setLong(1, idArt);
			int filas = ps.executeUpdate();
			if (filas > 0) {
				return a;
			}
		} catch (SQLException e) {
			System.out.println("Error eliminando artista ");
		}
		return null;
	}

}
