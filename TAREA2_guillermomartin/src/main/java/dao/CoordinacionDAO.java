/**
 *Clase CoordinacionDAO.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Coordinacion;
import utils.ConexBD;

public class CoordinacionDAO {
	private Connection conexion;

	public CoordinacionDAO() {
		this.conexion = ConexBD.getGestorConex().getConexion();

	}

	public Long insertar(Coordinacion c) {
		  String sql = "INSERT INTO coordinacion (idCoord, senior, fechasenior) VALUES (?, ?, ?)";
	        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
	        	  ps.setLong(1, c.getIdCoord());
	              ps.setBoolean(2, c.isSenior());

	            if (c.getFechasenior() != null) {
	            	ps.setDate(3, Date.valueOf(c.getFechasenior()));
	            }
	            else {
	            	ps.setDate(3, null);
	            }
	            ps.executeUpdate();


	        } catch (SQLException e) {
	            System.out.println("Error insertando coordinacion");
	          
	        }
	        return null;
	    }
	public Coordinacion buscarPorId(Long idCoord) {
	    String sql = "SELECT idCoord, senior, fechasenior FROM coordinacion WHERE idCoord = ?";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setLong(1, idCoord);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Coordinacion c = new Coordinacion();
	                c.setIdCoord(rs.getLong("idCoord"));
	                c.setSenior(rs.getBoolean("senior"));

	                Date d = rs.getDate("fechasenior");
	                if (d != null) {
	                    c.setFechasenior(d.toLocalDate());
	                }
	                return c;
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error buscando el coordinador ");
	      
	    }
	    return null;
	}
	 public Coordinacion actualizar(Coordinacion c) {
	        String sql = "UPDATE coordinacion SET senior = ?, fechasenior = ? WHERE idCoord = ?";
	        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	            ps.setBoolean(1, c.isSenior());
	            if (c.getFechasenior() != null) {
	                ps.setDate(2, Date.valueOf(c.getFechasenior()));
	            } else {
	                ps.setDate(2, null);
	            }
	            ps.setLong(3, c.getIdCoord());

	            int filas = ps.executeUpdate();
	            if (filas > 0) {
	                return buscarPorId(c.getIdCoord()); 
	            }
	        } catch (SQLException e) {
	            System.out.println("Error actualizando coordinacion");
	        }
	        return null;
	    }
	   
	    public Coordinacion eliminar(Long idCoord) {
	        Coordinacion c = buscarPorId(idCoord);
	        if (c==null) {
	        	return null;
	        }
	        String sql = "DELETE FROM coordinacion WHERE idCoord = ?";
	        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	            ps.setLong(1, idCoord);
	            int filas = ps.executeUpdate();
	            if (filas > 0) {
	            	return c; 
	            }
	        } catch (SQLException e) {
	            System.out.println("Error eliminando coordinacion ");
	        }
	        return null;
	    }
	    public List<Coordinacion> listarCoordinadores() {
	        List<Coordinacion> listaCoord = new ArrayList<>();
	        String sql = "SELECT idCoord, senior, fechasenior FROM coordinacion ORDER BY idCoord";

	        try (PreparedStatement ps=conexion.prepareStatement(sql);
	             ResultSet rs=ps.executeQuery()) {

	            while (rs.next()) {
	                Coordinacion c = new Coordinacion();
	                c.setIdCoord(rs.getLong("idCoord"));
	                c.setSenior(rs.getBoolean("senior"));

	                Date d = rs.getDate("fechasenior");
	                if (d != null) {
	                    c.setFechasenior(d.toLocalDate());
	                }

	                listaCoord.add(c);
	            }

	        } catch (SQLException e) {
	          
	            System.out.println("Error listando coordinadores" );
	        }

	        return listaCoord;
	    }

	
	
}
