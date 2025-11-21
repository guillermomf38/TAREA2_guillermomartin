/**
 *Clase ConexBD.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ConexBD {
	 //Instancia única (singleton) del gestor de conexión
	private static ConexBD gestorConex;
	// Objeto Connection que representa la conexión activa con la BD
	private Connection conexion;

	private ConexBD() {
		try {
			  // Cargar propiedades de configuración
			Properties props = new Properties();
			props.load(new FileInputStream ("src/main/resources/db.properties"));
			// Obtener parámetros de conexión
			String url = props.getProperty("db.url");
			String user = props.getProperty("db.user");
			String password = props.getProperty("db.password");
			conexion = DriverManager.getConnection(url, user, password);
			System.out.println("Conexion establecida");
		} catch (IOException | SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Error conectando a la base de datos.");
		}
	}
	 // Método estático para obtener la instancia única del gestor de conexión
	public static ConexBD getGestorConex() {
		if (gestorConex == null) {
			gestorConex = new ConexBD(); // crea la instancia si no existe

		}
		return gestorConex;
	}

	public Connection getConexion() {
		return conexion;
	}
}
