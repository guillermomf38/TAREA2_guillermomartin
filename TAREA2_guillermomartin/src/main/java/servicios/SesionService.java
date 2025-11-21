/**
 *Clase SesionService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package servicios;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import dao.CredencialesDAO;
import modelo.Credenciales;
import modelo.Perfiles;
import modelo.Sesion;

public class SesionService {
	private CredencialesDAO credDAO = new CredencialesDAO();
	private Sesion sesion;
	private String admin;
	private String contraseña;

	public SesionService() {
		cargarAdmin();
		sesion = new Sesion("Invitado", Perfiles.INVITADO);
	}
	 // Carga las credenciales del admin desde properties
	private void cargarAdmin() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/configuracion.properties")) {
            props.load(fis);
            admin = props.getProperty("admin");       
            contraseña = props.getProperty("contraseña");
		} catch (IOException e) {
			System.out.println("Error cargando credenciales de Admin ");
			admin = null;
			contraseña = null;
		}

	}
	// Iniciar sesión con usuario y contraseña

	public boolean login(String usuario, String password) {
		try {

			if (sesion.getPerfil() != Perfiles.INVITADO) {
				System.out.println("Ya hay una sesion activa");
				return false;
			}
			// Validacion de campos vacíos

			if (usuario == null || usuario.isEmpty() || password == null
					|| password.isEmpty()) {
				System.out
						.println("Usuario y contraseña no pueden estar vacios");
				return false;
			}
			 // Login como administrador

			if (usuario.equals(admin) && password.equals(contraseña)) {
				sesion.setUsuarioActual("Admin");
				sesion.setPerfil(Perfiles.ADMIN);
				System.out.println("Login correcto como Admin");
				return true;
			}

			Credenciales cred = credDAO.buscarPorNombre(usuario);
			if (cred != null && cred.getPassword().equals(password)) {
				sesion.setUsuarioActual(cred.getNombre());
				sesion.setPerfil(cred.getPerfil());
				System.out.println(
						"Login correcto. Perfil: " + sesion.getPerfil());
				return true;
			} else {
				System.out.println("Credenciales incorrectas");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error en login");
			return false;
		}
	}

    // Cierra la sesión actual y vuelve a invitado

	public void logout() {
		sesion = new Sesion("Invitado", Perfiles.INVITADO);

	}
	// Devuelve la sesión actual
	public Sesion getSesion() {
		return sesion;
	}



}
