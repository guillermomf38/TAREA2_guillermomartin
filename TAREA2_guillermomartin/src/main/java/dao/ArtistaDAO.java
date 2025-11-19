/**
 *Clase ArtistaDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import modelo.Artista;
import modelo.Especialidad;
import utils.ConexBD;

public class ArtistaDAO {
private Connection conexion;

public ArtistaDAO() {
	this.conexion=ConexBD.getGestorConex().getConexion();
}
public Long insertar(Artista a) {
	String sql="INSERT INTO artista(apodo) values (?)";
	try {
		PreparedStatement ps=conexion.prepareStatement(sql);
		
		ps.setString(1, a.getApodo());
		ps.executeUpdate();
		
		Statement sta= conexion.createStatement();
		ResultSet rs= sta.executeQuery("SELECT MAX(idArt AS id From artista");
		if(rs.next()) {
			
			return rs.getLong("id");
		}
	}catch(SQLException e) {
		System.out.println("Error insertando el artista");
	}
	return null;
	 
}

public void insertarEspecialidad(Long idArtista, List<Especialidad> lista) {
	String sql="INSERT INTO artista_especialidad(idArtista, especialidad) VALUES (?,?)";
	
	try {
		PreparedStatement ps=conexion.prepareStatement(sql);
		for(Especialidad es: lista) {
			ps.setLong(1, idArtista);
			
		}
	}catch(SQLException e) {
		System.out.println("Error insertando especialidades");
	}	
}
}
