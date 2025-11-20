/**
 *Clase Espectaculos.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Espectaculo {

	private Long id;
	private String nombre;
	private LocalDate fechaini;
	private LocalDate fechafin;
	private Long idCoord;
	private List<Numero> numeros = new ArrayList<>();

	public Espectaculo() {

	}

	public Espectaculo(String nombre, LocalDate fechaini, LocalDate fechafin,
			Long idCoord, List<Numero> numeros) {
		super();
		this.nombre = nombre;
		this.fechaini = fechaini;
		this.fechafin = fechafin;
		this.idCoord = idCoord;
		this.setNumeros(numeros);
	}

	public Espectaculo(Long id, String nombre, LocalDate fechaini,
			LocalDate fechafin, Long idCoord, List<Numero> numeros) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaini = fechaini;
		this.fechafin = fechafin;
		this.idCoord = idCoord;
		this.setNumeros(numeros);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaini() {
		return fechaini;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaini = fechaini;
	}

	public LocalDate getFechafin() {
		return fechafin;
	}

	public void setFechafin(LocalDate fechafin) {
		this.fechafin = fechafin;
	}

	public Long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(Long idCoord) {
		this.idCoord = idCoord;
	}

	public List<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<Numero> numeros) {
		this.numeros = numeros;
	}

	@Override
	public String toString() {
		return "Espectaculo [id=" + id + ", nombre=" + nombre + ", fechaini="
				+ fechaini + ", fechafin=" + fechafin + ", idCoord=" + idCoord
				+ ", numeros=" + numeros + "]";
	}

}
