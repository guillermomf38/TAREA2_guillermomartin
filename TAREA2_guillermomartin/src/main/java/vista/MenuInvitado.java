/**
 *Clase MenuInvitado.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package vista;

import java.util.Scanner;

import servicios.EspectaculoService;
import servicios.SesionService;

public class MenuInvitado {
	private SesionService sesionService;
	private EspectaculoService espectaculoService;

	public MenuInvitado(SesionService sesionService) {
		this.sesionService = sesionService;
		this.espectaculoService = new EspectaculoService(sesionService);
	}

	public void mostrarInvitado() {
		Scanner leer = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("--- Menu Invitado ---");
			System.out.println("1- Ver espectaculos");
			System.out.println("2- Iniciar sesion");
			System.out.println("0- Salir del programa");
			System.out.print("Elige una opcion: ");
			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:

				espectaculoService.listarEspectaculosBasicos();
				break;

			case 2:
				System.out.print("Usuario: ");
				String usuario = leer.nextLine();
				System.out.print("Password: ");
				String password = leer.nextLine();
				sesionService.login(usuario, password);
				break;

			case 0:
				System.out.println("Adios");

				break;

			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0);
	}

}
