/**
 *Clase MenuPrincipal.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package vista;

import java.util.InputMismatchException;
import java.util.Scanner;

import servicios.ArtistaService;
import servicios.EspectaculoService;
import servicios.NacionalidadService;
import servicios.PersonaService;
import servicios.SesionService;

public class MenuPrincipal {
	private SesionService sesionService;
	private EspectaculoService espectaculoService;
	private PersonaService personaService;
	private NacionalidadService nacionalidadService;
	private ArtistaService artistaService;

	public MenuPrincipal(SesionService sesionService,
			EspectaculoService espectaculoService,
			PersonaService personaService,
			NacionalidadService nacionalidadService,
			ArtistaService artistaService) {
		this.sesionService = sesionService;
		this.espectaculoService = espectaculoService;
		this.personaService = personaService;
		this.nacionalidadService = nacionalidadService;
		this.artistaService = artistaService;
	}

	public void mostrarMenuPrincipal() {
		Scanner leer = new Scanner(System.in);
		int opcion = -1;

		do {
			System.out.println("-- Menu Principal --");
			System.out.println("1- Ver espectaculos");
			System.out.println("2- Iniciar Sesion");
			System.out.println("0- Salir del programa");
			System.out.print("Selecciona una opcion: ");
			try {

				opcion = leer.nextInt();
				leer.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Error debes introducir un numero valido");
				leer.nextLine();
				opcion = -1;
			}

			switch (opcion) {

			case 1:
				espectaculoService.listarEspectaculosBasicos();
				break;

			case 2:
				System.out.print("Usuario: ");
				String usuario = leer.nextLine();
				System.out.print("Contraseña: ");
				String password = leer.nextLine();

				if (sesionService.login(usuario, password)) {
					switch (sesionService.getSesion().getPerfil()) {
					case ADMIN:
						new MenuAdmin(sesionService, personaService,
								nacionalidadService, artistaService)
								.mostrarAdmin();
						break;
					case COORDINACION:
						new MenuCoordinacion(sesionService, espectaculoService)
								.mostrarCoordinacion();
						break;
					case ARTISTA:
						new MenuArtista(espectaculoService, sesionService,
								artistaService).mostrarMenu();
						break;
					default:
						System.out.println("Error");
					}
				}
				break;

			case 0:
				System.out.println("Saliendo del programa");
				break;

			default:
				System.out.println("Opcion invalida");
			}

		} while (opcion != 0);
	}

	public static void main(String[] args) {
		SesionService sesionService = new SesionService();
		EspectaculoService espectaculoService = new EspectaculoService(
				sesionService);
		NacionalidadService nacionalidadService = new NacionalidadService();
		ArtistaService artistaService = new ArtistaService(sesionService);

		PersonaService personaService = new PersonaService(sesionService,
				nacionalidadService);

		MenuPrincipal menuPrincipal = new MenuPrincipal(sesionService,
				espectaculoService, personaService, nacionalidadService,
				artistaService);

		menuPrincipal.mostrarMenuPrincipal();
	}

}
