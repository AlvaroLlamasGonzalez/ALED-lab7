package es.upm.dit.aled.lab7;

import java.io.Serializable;

public class Paciente implements Serializable {

	private static final long serialVersionUID = -7040445160832391615L;
	
	private String nombre;
	private String apellido;
	private String dni;
	
	public Paciente(String nombre, String apellido, String dni){
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String toString() {
		return nombre + ";" + apellido + ";" + dni;
	}

}
