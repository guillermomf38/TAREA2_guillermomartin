/**
 *Clase MenuArtista.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package vista;

import java.util.Scanner;

import modelo.Artista;
import servicios.ArtistaService;
import servicios.EspectaculoService;
import servicios.SesionService;

public class MenuArtista {

	private EspectaculoService espectaculoService;
	private SesionService sesionService;
	private ArtistaService artistaService;

	public MenuArtista(EspectaculoService espectaculoService,
			SesionService sesionService, ArtistaService artistaService) {
		this.espectaculoService = espectaculoService;
		this.sesionService = sesionService;
		this.artistaService = artistaService;
	}

	public void mostrarMenu() {
		Scanner leer = new Scanner(System.in);
		int opcion;
		do {
			System.out.println("-- Menu Artista -- ");
			System.out.println("1- Ver espectaculos ");
			System.out.println("2- Ver datos completos de un espectaculo");
			System.out.println("3- Ver mi ficha personal");
			System.out.println("4- Cerrar sesion");
			System.out.println("0- Salir ");
			System.out.print("Selecciona una opcion: ");
			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:

				espectaculoService.listarEspectaculosBasicos();
				break;

			case 2:

				System.out.print("Id del espectaculo: ");
				Long idEsp = Long.valueOf(leer.nextLine());
				espectaculoService.verEspectaculoCompleto(idEsp);
				break;

			case 3:

				artistaService.verFicha();

				break;

			case 4:

				sesionService.logout();
				System.out
						.println("Sesion cerrada. Volviendo a perfil Invitado");
				return;
			case 0:
				System.out.println("Saliendo del programa");
				System.exit(0);
				break;
			default:
				System.out.println("Opcion invalida");
			}

		} while (opcion != 0);
	}
}
