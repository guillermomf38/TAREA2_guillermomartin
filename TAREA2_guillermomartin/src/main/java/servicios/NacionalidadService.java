/**
 *Clase NacionalidadService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package servicios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class NacionalidadService {
	// Mapa que almacena los países leídos desde el XML 
	private final Map<String, String> paises;

	public NacionalidadService() {
		this.paises = leerPaises();
	}
	 
	 // Lee el archivo XML de países usando la ruta configurada en ruta.properties

	private static Map<String, String> leerPaises() {
		Map<String, String> paises = new LinkedHashMap<>();
		Properties prop = new Properties();

		try (FileInputStream fis = new FileInputStream(
				"src/main/resources/ruta.properties")) {
			prop.load(fis);
		} catch (IOException e) {
			System.out.println("Error leyendo archivo ruta.properties");
			return paises;
		}

		String ruta = prop.getProperty("rutaPaisesxml");
		if (ruta == null) {
			System.out.println(
					"No esta configurada la ruta del XML en ruta.properties");
			return paises;
		}

		File xmlFile = new File(ruta);
		if (!xmlFile.exists()) {
			System.out.println(
					"No se encontro el archivo XML en la ruta: " + ruta);
			return paises;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList lista = doc.getElementsByTagName("pais");
			for (int i = 0; i < lista.getLength(); i++) {
				org.w3c.dom.Element pais = (org.w3c.dom.Element) lista.item(i);
				String id = pais.getElementsByTagName("id").item(0)
						.getTextContent();
				String nombre = pais.getElementsByTagName("nombre").item(0)
						.getTextContent();
				paises.put(id.trim(), nombre.trim());
			}
		} catch (Exception e) {
			System.out.println("Error leyendo paises.xml ");
		}

		return paises;
	}
	 // Verifica si una nacionalidad es válida 
	public boolean esValida(String nacionalidad) {
		return paises.containsValue(nacionalidad);
	}
	// Devuelve el mapa completo de países
	public Map<String, String> getPaises() {
		return paises;
	}

}
