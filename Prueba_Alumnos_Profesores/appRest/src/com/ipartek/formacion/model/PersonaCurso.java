package com.ipartek.formacion.model;

public class PersonaCurso {

	private Persona persona;
	private Curso curso;
	private Double precio;
	
	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public PersonaCurso(Persona persona, Curso curso, Double precio) {
		setPersona(persona);
		setCurso(curso);
		setPrecio(precio);
	}
	
	public PersonaCurso(Persona persona, Curso curso) {
		this(persona, curso, 0.0);
	}
	
	public PersonaCurso() {
		this(null,null,null);
	}



	@Override
	public String toString() {
		return "PersonaCurso [persona=" + persona + ", curso=" + curso + ", precio=" + precio + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((persona == null) ? 0 : persona.hashCode());
		result = prime * result + ((precio == null) ? 0 : precio.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaCurso other = (PersonaCurso) obj;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (persona == null) {
			if (other.persona != null)
				return false;
		} else if (!persona.equals(other.persona))
			return false;
		if (precio == null) {
			if (other.precio != null)
				return false;
		} else if (!precio.equals(other.precio))
			return false;
		return true;
	}
	
	
}
