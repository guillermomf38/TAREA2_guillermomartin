/**
 *Clase MenuAdmin.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package vista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import modelo.Artista;
import modelo.Coordinacion;
import modelo.Credenciales;
import modelo.Especialidad;
import modelo.Espectaculo;
import modelo.Numero;
import modelo.Perfiles;
import modelo.Persona;
import servicios.ArtistaService;
import servicios.EspectaculoService;
import servicios.NacionalidadService;
import servicios.PersonaService;
import servicios.SesionService;

public class MenuAdmin {
	private SesionService sesionService;
	private EspectaculoService espectaculoService;
	private PersonaService personaService;
	private NacionalidadService nacionalidadService;
	private ArtistaService artistaService;

	public MenuAdmin(SesionService sesionService, PersonaService personaService,
			NacionalidadService nacionalidadService,
			ArtistaService artistaService) {
		this.sesionService = sesionService;
		this.espectaculoService = new EspectaculoService(sesionService);
		this.personaService = personaService;
		this.nacionalidadService = nacionalidadService;
		this.artistaService = artistaService;
	}

	public void mostrarAdmin() {
		Scanner leer = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("--- Menu Admin ---");
			System.out.println("1- Ver espectaculos");
			System.out.println("2- Gestionar personas y credenciales");
			System.out.println("3- Gestionar espectaculos");
			System.out.println("4- Gestionar artistas");
			System.out.println("5- Cerrar sesion ");
			System.out.println("0- Salir");
			System.out.print("Elige una opcion: ");

			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:
				mostrarSubmenuVerEspectaculos(leer);
				break;
			case 2:
				mostrarSubmenuPersonas(leer);
				break;
			case 3:
				mostrarSubmenuEspectaculos(leer);
				break;
			case 4:
				mostrarSubmenuArtistas(leer);
				break;
			case 5:
				sesionService.logout();
				System.out.println("Sesion cerrada. Volviendo a perfil Invitado");

				return;
			case 0:
				  System.out.println("Saliendo del programa");
				    System.exit(0);    
				    break;
			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0 );

	}

	private void mostrarSubmenuVerEspectaculos(Scanner leer) {
		int opcion;
		do {
			System.out.println("-- Ver espectaculos --");
			System.out.println("1- Listar espectaculos basicos");
			System.out.println("2- Ver detalle de espectaculo ");
			System.out.println("0- Volver");
			System.out.print("Elige una opcion: ");
			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:
				espectaculoService.listarEspectaculosBasicos();
				break;
			case 2:
				System.out.print("Id de espectaculo: ");
				String idEsp = leer.nextLine();
				espectaculoService.verEspectaculoCompleto(Long.valueOf(idEsp));
				break;
			case 0:
				break;
			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0);
	}

	private void mostrarSubmenuPersonas(Scanner leer) {
		int opcion;
		do {
			System.out.println("-- Gestion de Personas --");
			System.out.println("1- Registrar persona ");
			System.out.println("2- Modificar persona");
			System.out.println("3- Eliminar persona ");
			System.out.println("0- Volver");
			System.out.print("Elige una opcion: ");
			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:
				System.out.print("Nombre: ");
				String nombrePersona = leer.nextLine();
				System.out.print("Email: ");
				String email = leer.nextLine();
				System.out.println("Selecciona nacionalidad:");
				nacionalidadService.getPaises()
						.forEach((id, nombrePais) -> System.out
								.println(id + " - " + nombrePais));
				String nacionalidad = leer.nextLine();
				System.out.print("Usuario: ");
				String usuario = leer.nextLine();
				System.out.print("Contrasena: ");
				String password = leer.nextLine();
				System.out.print(
						"Perfil (INVITADO/ARTISTA/COORDINACION/ADMIN): ");
				String perfil = leer.nextLine();

				Persona persona = new Persona();
				persona.setNombre(nombrePersona);
				persona.setEmail(email);
				persona.setNacionalidad(nacionalidad);

				Credenciales cred = new Credenciales();
				cred.setNombre(usuario);
				cred.setPassword(password);
				cred.setPerfil(Perfiles.valueOf(perfil.toUpperCase()));

				Coordinacion coord = null;
				Artista art = null;

				personaService.registrarPersona(persona, cred, coord, art);
				break;

			case 2:
				System.out.print("Id de persona a modificar: ");
				String idPersona = leer.nextLine();
				System.out.print("Nuevo nombre: ");
				String nuevoNombre = leer.nextLine();
				System.out.print("Nuevo email: ");
				String nuevoEmail = leer.nextLine();
				System.out.print("Nueva nacionalidad: ");
				String nuevaNacionalidad = leer.nextLine();

				Persona personaMod = new Persona();
				personaMod.setId(Long.valueOf(idPersona));
				personaMod.setNombre(nuevoNombre);
				personaMod.setEmail(nuevoEmail);
				personaMod.setNacionalidad(nuevaNacionalidad);

				personaService.modificarDatosPersonales(personaMod);
				break;

			case 3:
				System.out.print("Id de persona a eliminar: ");
				Long idEliminar = Long.valueOf(leer.nextLine());
				personaService.eliminarPersona(idEliminar);
				break;

			case 0:
				break;

			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0);
	}

	private void mostrarSubmenuEspectaculos(Scanner leer) {
		int opcion;
		do {
			System.out.println("-- Gestion de Espectaculos --");
			System.out.println("1- Crear espectaculo ");
			System.out.println("2- Modificar espectaculo");
			System.out.println("3- Eliminar espectaculo ");
			System.out.println("4- Crear numero ");
			System.out.println("5- Modificar numero ");
			System.out.println("6- Asignar artistas a numero");
			System.out.println("0- Volver");
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
				System.out.print("Id del coordinador: ");
				String coordinadorSeleccionado = leer.nextLine();

				espectaculoService.crearEspectaculo(nombreEsp, fechaini,
						fechafin, coordinadorSeleccionado);
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
				System.out.print("Id del nuevo coordinador: ");
				String nuevoCoord = leer.nextLine();
				Espectaculo espectaculoMod = new Espectaculo();
				espectaculoMod.setId(idMod);
				espectaculoMod.setNombre(nuevoNombre);
				espectaculoMod.setFechaini(LocalDate.parse(nuevaFechaini));
				espectaculoMod.setFechafin(LocalDate.parse(nuevaFechafin));
				espectaculoService.modificarEspectaculo(espectaculoMod,
						nuevoCoord);

			case 3:
				System.out.print("Id de espectaculo a eliminar: ");
				Long idEliminar = Long.valueOf(leer.nextLine());
				espectaculoService.eliminarEspectaculo(idEliminar);
				break;

			case 4:
				System.out.print("Id de espectaculo: ");
				Long idEspectaculo = Long.valueOf(leer.nextLine());
				System.out.print("Nombre del numero: ");
				String nombreNum = leer.nextLine();
				System.out.print("Duracion (en minutos): ");
				Integer duracion = Integer.valueOf(leer.nextLine());
				System.out.print("Orden: ");
				Integer orden = Integer.valueOf(leer.nextLine());

				Numero numero = new Numero();
				numero.setNombre(nombreNum);
				numero.setDuracion(duracion);
				numero.setOrden(orden);

				espectaculoService.crearNumero(idEspectaculo, numero);
				break;

			case 5:
				System.out.print("Id del numero a modificar: ");
				Long idNum = Long.valueOf(leer.nextLine());
				System.out.print("Nuevo nombre: ");
				String nuevoNomNum = leer.nextLine();
				System.out.print("Nueva duracion (en minutos): ");
				Integer nuevaDuracion = Integer.valueOf(leer.nextLine());
				System.out.print("Nuevo orden: ");
				Integer nuevoOrden = Integer.valueOf(leer.nextLine());

				Numero numeroMod = new Numero();
				numeroMod.setId(idNum);
				numeroMod.setNombre(nuevoNomNum);
				numeroMod.setDuracion(nuevaDuracion);
				numeroMod.setOrden(nuevoOrden);
				espectaculoService.modificarNumero(numeroMod);
				break;

			case 6:
				System.out.print("Id del numero: ");
				Long idNumero = Long.valueOf(leer.nextLine());
				System.out.print("Ids de artistas: ");
				String ids = leer.nextLine();

				List<Long> idArtistas = new ArrayList<>();
				for (String parte : ids.split(",")) {
					parte = parte.trim();
					if (!parte.isEmpty()) {
						idArtistas.add(Long.valueOf(parte));
					}
				}

				espectaculoService.asignarArtistasANumero(idNumero, idArtistas);

			case 0:
				break;

			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0);
	}

	private void mostrarSubmenuArtistas(Scanner leer) {
		int opcion;
		do {
			System.out.println("- Gestion de Artistas--");
			System.out.println("1- Modificar artista");
			System.out.println("0- Volver");
			System.out.print("Elige una opción: ");
			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {
			case 1:
				System.out.print("Id del artista a modificar: ");
				Long idArtista = Long.valueOf(leer.nextLine());
				System.out.print("Nuevo apodo: ");
				String nuevoApodo = leer.nextLine();
				System.out.print("Nuevas especialidades: ");
				String nuevasEspStr = leer.nextLine();

				List<Especialidad> nuevasEsp = new ArrayList<>();
				for (String esp : nuevasEspStr.split(",")) {
					try {
						nuevasEsp.add(
								Especialidad.valueOf(esp.trim().toUpperCase()));
					} catch (IllegalArgumentException e) {
						System.out.println("Especialidad invalida ");
					}
				}

				personaService.modificarArtista(idArtista, nuevoApodo,
						nuevasEsp);
				break;

			case 0:
				break;

			default:
				System.out.println("Opcion invalida");
			}
		} while (opcion != 0);
	}

}
