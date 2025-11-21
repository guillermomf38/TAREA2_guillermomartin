/**
 *Clase ArtistaService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package servicios;

import dao.ArtistaDAO;
import modelo.Artista;
import modelo.Perfiles;
import modelo.Sesion;

public class ArtistaService {
	private SesionService sesionService;
	private ArtistaDAO artistaDAO;

	public ArtistaService(SesionService sesionService) {
		this.sesionService = sesionService;
		this.artistaDAO = new ArtistaDAO();
	}

	public Artista verFicha() {
		Sesion sesion = sesionService.getSesion();

		if (sesion.getPerfil() != Perfiles.ARTISTA) {
			System.out.println("Solo un artista puede ver su ficha");
			return null;
		}

		Long idArtista = Long.valueOf(sesion.getUsuarioActual());

		Artista ficha = artistaDAO.verFicha(idArtista);

		if (ficha == null) {
			System.out.println("No se encontro la ficha del artista");
			return null;
		}

		return ficha;
	}

}
