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
	private String adminPass;

	public SesionService() {
		cargarAdmin();
		sesion = new Sesion("Invitado", Perfiles.INVITADO);
	}

	private void cargarAdmin() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("configuracion.properties")) {
            props.load(fis);
            admin = props.getProperty("admin");       
            adminPass = props.getProperty("contraseña");
		} catch (IOException e) {
			System.out.println("Error cargando credenciales de Admin ");
			admin = null;
			adminPass = null;
		}

	}

	public boolean login(String usuario, String password) {
		try {

			if (sesion.getPerfil() != Perfiles.INVITADO) {
				System.out.println("Ya hay una sesion activa");
				return false;
			}

			if (usuario == null || usuario.isEmpty() || password == null
					|| password.isEmpty()) {
				System.out
						.println("Usuario y contraseña no pueden estar vacios");
				return false;
			}

			if (usuario.equals(admin) && password.equals(adminPass)) {
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

	public void logout() {
		sesion = new Sesion("Invitado", Perfiles.INVITADO);
		System.out.println("Sesion cerrada. Perfil actual: Invitado");
	}

	public Sesion getSesion() {
		return sesion;
	}

	public boolean haySesionActiva() {
		return sesion.getPerfil() != Perfiles.INVITADO;
	}

}
