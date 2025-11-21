/**
 *Clase MenuCoordinacion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package vista;

import java.util.Scanner;

import modelo.Perfiles;
import servicios.EspectaculoService;
import servicios.SesionService;

public class MenuCoordinacion {
	private SesionService sesionService;
	private EspectaculoService espectaculoService;

	public MenuCoordinacion(SesionService sesionService,
			EspectaculoService espectaculoService) {
		this.sesionService = sesionService;
		this.espectaculoService = espectaculoService;
	}

	public void mostrarCoordinacion() {
		Scanner leer = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("--- Menu Coordinacion ---");
			System.out.println("1- Crear espectaculo");
			System.out.println("2- Modificar espectaculo");
			System.out.println("3- Crear numero");
			System.out.println("4- Modificar numero");
			System.out.println("5- Asignar artistas a numero");
			System.out.println("6- Ver espectaculos");
			System.out.println("7- Ver espectaculo completo");
			System.out.println("8- Cerrar sesion");
			System.out.println("0- Salir");
			System.out.print("Elige una opcion: ");

			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:
				System.out.print("Nombre del espectaculo: ");
				String nombreEsp = leer.nextLine();
				System.out.print("Fecha inicio (YYYY-MM-DD): ");
				String fechaini = leer.nextLine();
				System.out.print("Fecha fin (YYYY-MM-DD): ");
				String fechafin = leer.nextLine();

				espectaculoService.crearEspectaculo(nombreEsp, fechaini,
						fechafin, null);
				break;

			case 2:
				System.out.print("Id de espectaculo a modificar: ");
				Long idMod = Long.valueOf(leer.nextLine());
				System.out.print("Nuevo nombre: ");
				String nuevoNombre = leer.nextLine();
				System.out.print("Nueva fecha inicio (YYYY-MM-DD): ");
				String nuevaFechaini = leer.nextLine();
				System.out.print("Nueva fecha fin (YYYY-MM-DD): ");
				String nuevaFechafin = leer.nextLine();

				espectaculoService.modificarEspectaculo(idMod, nuevoNombre,
						nuevaFechaini, nuevaFechafin, null);
				break;

			case 3:
				System.out.print("Id de espectaculo: ");
				Long idEspectaculo = Long.valueOf(leer.nextLine());
				System.out.print("Nombre del numero: ");
				String nombreNum = leer.nextLine();
				System.out.print("Duracion : ");
				Integer duracion = Integer.valueOf(leer.nextLine());
				System.out.print("Orden: ");
				Integer orden = Integer.valueOf(leer.nextLine());
				espectaculoService.crearNumero(idEspectaculo, nombreNum,
						duracion, orden);
				break;

			case 4:
				System.out.print("Id del numero a modificar: ");
				Long idNum = Long.valueOf(leer.nextLine());
				System.out.print("Nuevo nombre: ");
				String nuevoNomNum = leer.nextLine();
				System.out.print("Nueva duracion: ");
				Integer nuevaDuracion = Integer.valueOf(leer.nextLine());
				System.out.print("Nuevo orden: ");
				Integer nuevoOrden = Integer.valueOf(leer.nextLine());
				espectaculoService.modificarNumero(idNum, nuevoNomNum,
						nuevaDuracion, nuevoOrden);
				break;

			case 5:
				System.out.print("Id del numero: ");
				Long idNumero = Long.valueOf(leer.nextLine());
				System.out.print("Ids de artistas ");
				String ids = leer.nextLine();

				espectaculoService.asignarArtistasANumero(idNumero, ids);
				break;

			case 6:
				espectaculoService.listarEspectaculosBasicos();
				break;

			case 7:
				System.out.print("Id del espectaculo: ");
				Long idEsp = Long.valueOf(leer.nextLine());
				espectaculoService.verEspectaculoCompleto(idEsp);
				break;

			case 8:
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
