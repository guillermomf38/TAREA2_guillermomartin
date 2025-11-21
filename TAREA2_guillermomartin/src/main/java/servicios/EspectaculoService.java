/**
 *Clase EspectaculoService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package servicios;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
	// Lista espectáculos básicos
	public List<Espectaculo> listarEspectaculosBasicos() {
		try {
			List<Espectaculo> espectaculos = espectaculoDAO
					.listarEspectaculos();

			if (espectaculos.isEmpty()) {
				System.out.println("No hay espectaculos disponibles");
			}

			return espectaculos;
		} catch (Exception e) {
			System.out.println("Error al listar espectaculos");
			return List.of();
		}

	}
	   // Devuelve un espectáculo completo con sus números y artistas asociados

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

	public Long crearEspectaculo(String nombre, String fechaini,
			String fechafin, String coordinadorSeleccionado) {
		try {
			if (nombre == null || nombre.isEmpty() || fechaini == null
					|| fechaini.isEmpty() || fechafin == null
					|| fechafin.isEmpty()) {
				System.out.println("Nombre y fechas son obligatorios");
				return null;
			}

			LocalDate ini;
			LocalDate fin;
			try {
				ini = LocalDate.parse(fechaini.trim());
				fin = LocalDate.parse(fechafin.trim());
			} catch (DateTimeParseException d) {
				System.out.println("Formato de fecha invalido. Usa YYYY-MM-DD");
				return null;
			}

			Espectaculo e = new Espectaculo();
			e.setNombre(nombre.trim());
			e.setFechaini(ini);
			e.setFechafin(fin);

			return crearEspectaculo(e, coordinadorSeleccionado);
		} catch (Exception ex) {
			System.out.println("Error al crear espectaculo ");
			return null;
		}
	}
	// Modifica un espectáculo existente validando permisos
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
		 // Asignación de coordinador según perfil

		if (sesion.getPerfil() == Perfiles.ADMIN && nuevoCoordinador != null) {
			e.setIdCoord(Long.valueOf(nuevoCoordinador));
		} else if (sesion.getPerfil() == Perfiles.COORDINACION) {
			e.setIdCoord(Long.valueOf(sesion.getUsuarioActual()));
		}

		return espectaculoDAO.actualizar(e);
	}
	
	public Espectaculo modificarEspectaculo(Long id, String nombre, String fechaini, String fechafin, String nuevoCoordinador) {
	    try {
	        LocalDate ini = LocalDate.parse(fechaini.trim());
	        LocalDate fin = LocalDate.parse(fechafin.trim());

	        Espectaculo e = new Espectaculo();
	        e.setId(id);
	        e.setNombre(nombre.trim());
	        e.setFechaini(ini);
	        e.setFechafin(fin);

	        return modificarEspectaculo(e, nuevoCoordinador);
	    } catch (DateTimeParseException d) {
	        System.out.println("Formato de fecha invalido. Usa YYYY-MM-DD");
	        return null;
	    } catch (Exception ex) {
	        System.out.println("Error al modificar espectaculo ");
	        return null;
	    }
	}
	// Crea un número dentro de un espectáculo

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
	
	public Long crearNumero(Long idEspectaculo, String nombre, Integer duracion, Integer orden) {
	    Numero n = new Numero();
	    n.setNombre(nombre);
	    n.setDuracion(duracion);
	    n.setOrden(orden);
	    return crearNumero(idEspectaculo, n);
	}
	// Modifica un número existente

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
	public Numero modificarNumero(Long id, String nombre, Integer duracion, Integer orden) {
	    Numero n = new Numero();
	    n.setId(id);
	    n.setNombre(nombre);
	    n.setDuracion(duracion);
	    n.setOrden(orden);
	    return modificarNumero(n);
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
	public boolean asignarArtistasANumero(Long idNumero, String ids) {
	    List<Long> idArtistas = new ArrayList<>();
	    for (String parte : ids.split(",")) {
	        parte = parte.trim();
	        if (!parte.isEmpty()) {
	            try {
	                idArtistas.add(Long.valueOf(parte));
	            } catch (NumberFormatException e) {
	                System.out.println("Id de artista invalido: " + parte);
	            }
	        }
	    }
	    return asignarArtistasANumero(idNumero, idArtistas);
	}
}
