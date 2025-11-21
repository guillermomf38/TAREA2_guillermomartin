/**
 *Clase EspectaculoService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package servicios;

import java.util.List;

import dao.ArtistaDAO;
import dao.EspectaculoDAO;
import dao.NumeroDAO;
import dao.ParticipaDAO;
import modelo.Artista;
import modelo.Espectaculo;
import modelo.Numero;
import modelo.Perfiles;
import modelo.Sesion;

public class EspectaculoService {
	private EspectaculoDAO espectaculoDAO;
	private NumeroDAO numeroDAO;
	private ArtistaDAO artistaDAO;
	private ParticipaDAO participaDAO;
	private SesionService sesionService;

	public EspectaculoService(SesionService sesionService) {
		this.espectaculoDAO = new EspectaculoDAO();
		this.numeroDAO = new NumeroDAO();
		this.artistaDAO = new ArtistaDAO();
		this.participaDAO = new ParticipaDAO();
		this.sesionService = sesionService;

	}

	public List<Espectaculo> listarEspectaculosBasicos() {
		try {

			return espectaculoDAO.listarEspectaculos();

		} catch (Exception e) {
			System.out.println("Error al listar espectaculos ");
			return List.of();
		}
	}

	public Espectaculo verEspectaculoCompleto(Long id) {
		Espectaculo e = espectaculoDAO.buscarPorId(id);
		if (e != null) {

			for (Numero n : e.getNumeros()) {
				List<Long> idsArtistas = participaDAO
						.listarArtistasPorNumero(n.getId());
				for (Long idArt : idsArtistas) {
					Artista a = artistaDAO.buscarPorId(idArt);
					if (a != null) {
						n.getArtistas().add(a);
					}
				}
			}
		}
		return e;
	}

	public Long crearEspectaculo(Espectaculo e,
			String coordinadorSeleccionado) {
		Sesion sesion = sesionService.getSesion();
		if (sesion.getPerfil() != Perfiles.ADMIN
				&& sesion.getPerfil() != Perfiles.COORDINACION) {
			System.out.println("No tienes permisos para crear espectaculos");
			return null;
		}

		if (e.getNombre() == null || e.getNombre().isEmpty()) {
			System.out
					.println("El nombre del espectaculo no puede estar vacio");
			return null;
		}
		if (e.getNombre().length() > 25) {
			System.out.println(
					"El nombre del espectaculo no puede superar los 25 caracteres");
			return null;
		}

		List<Espectaculo> espectaculos = espectaculoDAO.listarEspectaculos();
		for (Espectaculo es : espectaculos) {
			if (es.getNombre().equals(e.getNombre())) {
				System.out.println("Ya existe un espectaculo con ese nombre");
				return null;
			}
		}

		if (e.getFechaini() == null || e.getFechafin() == null) {
			System.out.println("Las fechas no pueden ser nulas");
			return null;
		}
		if (e.getFechaini().isAfter(e.getFechafin())) {
			System.out.println(
					"La fecha de inicio no puede ser posterior a la fecha de fin");
			return null;
		}
		if (e.getFechaini().plusYears(1).isBefore(e.getFechafin())) {
			System.out.println("El periodo de vigencia no puede superar 1 año");
			return null;
		}

		if (sesion.getPerfil() == Perfiles.COORDINACION) {
			e.setIdCoord(Long.valueOf(sesion.getUsuarioActual()));

		} else {
			e.setIdCoord(Long.valueOf(coordinadorSeleccionado));
		}

		return espectaculoDAO.insertar(e);
	}

	public Espectaculo modificarEspectaculo(Espectaculo e,
			String nuevoCoordinador) {
		Sesion sesion = sesionService.getSesion();
		if (sesion.getPerfil() != Perfiles.ADMIN
				&& sesion.getPerfil() != Perfiles.COORDINACION) {
			System.out
					.println("No tienes permisos para modificar espectaculos");
			return null;
		}

		if (e.getNombre() == null || e.getNombre().isEmpty()) {
			System.out
					.println("El nombre del espectaculo no puede estar vacio");
			return null;
		}
		if (e.getNombre().length() > 25) {
			System.out.println(
					"El nombre del espectaculo no puede superar los 25 caracteres");
			return null;
		}
		if (e.getFechaini() == null || e.getFechafin() == null) {
			System.out.println("Las fechas no pueden ser nulas");
			return null;
		}
		if (e.getFechaini().isAfter(e.getFechafin())) {
			System.out.println(
					"La fecha de inicio no puede ser posterior a la fecha de fin");
			return null;
		}
		if (e.getFechaini().plusYears(1).isBefore(e.getFechafin())) {
			System.out.println("El periodo de vigencia no puede superar 1 año");
			return null;
		}

		if (sesion.getPerfil() == Perfiles.ADMIN && nuevoCoordinador != null) {
			e.setIdCoord(Long.valueOf(nuevoCoordinador));
		} else if (sesion.getPerfil() == Perfiles.COORDINACION) {
			e.setIdCoord(Long.valueOf(sesion.getUsuarioActual()));
		}

		return espectaculoDAO.actualizar(e);
	}

	public Long crearNumero(Long idEspectaculo, Numero n) {
		if (n.getNombre() == null || n.getNombre().isEmpty()) {
			System.out.println("El nombre del numero no puede estar vacio");
			return null;
		}
		if (n.getDuracion() <= 0) {
			System.out.println("La duracion debe ser positiva");
			return null;
		}
		if (n.getOrden() <= 0) {
			System.out.println("El orden debe ser positivo");
			return null;
		}

		n.setIdEspectaculo(idEspectaculo);
		return numeroDAO.insertar(n);
	}

	public Numero modificarNumero(Numero n) {
		if (n.getNombre() == null || n.getNombre().isEmpty()) {
			System.out.println("El nombre del numero no puede estar vacio");
			return null;
		}
		if (n.getDuracion() <= 0) {
			System.out.println("La duracion debe ser positiva");
			return null;
		}
		if (n.getOrden() <= 0) {
			System.out.println("El orden debe ser positivo");
			return null;
		}

		return numeroDAO.actualizar(n);
	}

	public Espectaculo eliminarEspectaculo(Long id) {
		Sesion sesion = sesionService.getSesion();
		if (sesion.getPerfil() != Perfiles.ADMIN
				&& sesion.getPerfil() != Perfiles.COORDINACION) {
			System.out.println("No tienes permisos para eliminar espectaculos");
			return null;
		}

		try {
			return espectaculoDAO.eliminar(id);
		} catch (Exception ex) {
			System.out.println("Error al eliminar espectaculo ");
			return null;
		}
	}

	public boolean asignarArtistasANumero(Long idNumero,
			List<Long> idArtistas) {
		participaDAO.eliminarPorNumero(idNumero);
		for (Long idArt : idArtistas) {
			participaDAO.insertar(idNumero, idArt);
		}
		return true;
	}
}
