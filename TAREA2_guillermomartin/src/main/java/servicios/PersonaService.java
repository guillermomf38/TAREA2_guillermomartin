/**
 *Clase PersonaService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package servicios;

import java.time.LocalDate;
import java.util.List;


import dao.ArtistaDAO;
import dao.CoordinacionDAO;
import dao.CredencialesDAO;
import dao.PersonaDAO;
import modelo.Artista;
import modelo.Coordinacion;
import modelo.Credenciales;
import modelo.Especialidad;
import modelo.Perfiles;
import modelo.Persona;
import modelo.Sesion;

public class PersonaService {
	  private  PersonaDAO personaDAO;
	    private  CredencialesDAO credDAO;
	    private  ArtistaDAO artistaDAO;
	    private  CoordinacionDAO coordDAO;
	    private  SesionService sesionService;
	    private  NacionalidadService nacionalidadService;
	    
	    public PersonaService(SesionService sesionService, NacionalidadService nacionalidadService) {
	        this.personaDAO = new PersonaDAO();
	        this.credDAO = new CredencialesDAO();
	        this.artistaDAO = new ArtistaDAO();
	        this.coordDAO = new CoordinacionDAO();
	        this.sesionService = sesionService;
	        this.nacionalidadService = nacionalidadService;
	    }
	    
	  
	    public Long registrarPersona(Persona persona, Credenciales credenciales, Coordinacion coord, Artista art) {
	        Sesion sesion = sesionService.getSesion();
	        if (sesion.getPerfil() != Perfiles.ADMIN) {
	            System.out.println("Solo el Admin puede registrar personas");
	            return null;
	        }

	        if (persona.getNombre() == null || persona.getNombre().isEmpty()) {
	            System.out.println("El nombre real no puede estar vacio");
	            return null;
	        }
	        if (persona.getEmail() == null || persona.getEmail().isEmpty()) {
	            System.out.println("El email no puede estar vacio");
	            return null;
	        }
	        if (personaDAO.emailExiste(persona.getEmail())) {
	            System.out.println("Ya existe una persona con ese email");
	            return null;
	        }
	        if (persona.getNacionalidad() == null || !nacionalidadService.esValida(persona.getNacionalidad())) {
	            System.out.println("La nacionalidad no es valida");
	            return null;
	        }

	   
	        if (credenciales.getNombre() == null || credenciales.getNombre().length() < 3 || credenciales.getNombre().contains(" ")) {
	            System.out.println("Nombre de usuario invalido");
	            return null;
	        }
	        if (!credenciales.getNombre().matches("(?i)^[a-z]+$")) {
	            System.out.println("El nombre de usuario solo acepta letras sin tildes ni dieresis");
	            return null;
	        }
	        credenciales.setNombre(credenciales.getNombre().toLowerCase());

	        if (credenciales.getPassword() == null || credenciales.getPassword().length() < 3 || credenciales.getPassword().contains(" ")) {
	            System.out.println("Contraseña invalida");
	            return null;
	        }

	        Credenciales existente = credDAO.buscarPorNombre(credenciales.getNombre());
	        if (existente != null) {
	            System.out.println("Ya existe un usuario con ese nombre");
	            return null;
	        }

	      
	        Long idPersona = personaDAO.insertar(persona);

	        
	        credDAO.insertar(credenciales);

	        
	        if (credenciales.getPerfil() == Perfiles.COORDINACION) {
	            if (coord == null) {
	                System.out.println("Debe indicar datos de Coordinacion");
	                return null;
	            }
	            coord.setId(idPersona);
	            coordDAO.insertar(coord);
	        } else if (credenciales.getPerfil() == Perfiles.ARTISTA) {
	            if (art == null) {
	                System.out.println("Debe indicar datos de Artista");
	                return null;
	            }
	            List<Especialidad> especialidades = art.getEspecialidades();
	            if (especialidades == null || especialidades.isEmpty()) {
	                System.out.println("Debe indicar al menos una especialidad");
	                return null;
	            }
	            art.setId(idPersona);
	            artistaDAO.insertar(art);
	        }

	        return idPersona;
	    }

	    
	    public Persona modificarDatosPersonales(Persona persona) {
	        Sesion sesion = sesionService.getSesion();
	        if (sesion.getPerfil() != Perfiles.ADMIN) {
	            System.out.println("Solo el Admin puede modificar personas");
	            return null;
	        }

	        if (persona.getNombre() == null || persona.getNombre().isEmpty()) {
	            System.out.println("El nombre real no puede estar vacio");
	            return null;
	        }
	        if (persona.getEmail() == null || persona.getEmail().isEmpty()) {
	            System.out.println("El email no puede estar vacio");
	            return null;
	        }
	        if (persona.getNacionalidad() == null || !nacionalidadService.esValida(persona.getNacionalidad())) {
	            System.out.println("La nacionalidad no es valida");
	            return null;
	        }

	        return personaDAO.actualizar(persona);
	    }

	
	    public Coordinacion modificarCoordinacion(Long idPersona, boolean senior, LocalDate fechaSenior) {
	        Sesion sesion = sesionService.getSesion();
	        if (sesion.getPerfil() != Perfiles.ADMIN) {
	            System.out.println("Solo el Admin puede modificar Coordinacion");
	            return null;
	        }

	        Coordinacion coord = coordDAO.buscarPorId(idPersona);
	        if (coord == null) {
	            System.out.println("La persona no tiene perfil de Coordinacion");
	            return null;
	        }

	        coord.setSenior(senior);
	        coord.setFechasenior(fechaSenior);
	        coordDAO.actualizar(coord);

	        return coord;
	    }

	   
	    public Artista modificarArtista(Long idPersona, String apodo, List<Especialidad> especialidades) {
	        Sesion sesion = sesionService.getSesion();
	        if (sesion.getPerfil() != Perfiles.ADMIN) {
	            System.out.println("Solo el Admin puede modificar Artistas");
	            return null;
	        }

	        Artista artista = artistaDAO.buscarPorId(idPersona);
	        if (artista == null) {
	            System.out.println("La persona no tiene perfil de Artista");
	            return null;
	        }

	        artista.setApodo(apodo);
	        if (especialidades == null || especialidades.isEmpty()) {
	            System.out.println("Debe indicar al menos una especialidad");
	            return null;
	        }
	        artista.setEspecialidades(especialidades);

	        return artistaDAO.actualizar(artista);
	    }

	   
	    public Persona eliminarPersona(Long idPersona) {
	        Sesion sesion = sesionService.getSesion();
	        if (sesion.getPerfil() != Perfiles.ADMIN) {
	            System.out.println("Solo el Admin puede eliminar personas");
	            return null;
	        }

	        
	        credDAO.eliminar(idPersona);
	        artistaDAO.eliminar(idPersona);
	        coordDAO.eliminar(idPersona);

	        return personaDAO.eliminar(idPersona);
	    }


}
