package com.ipartek.formacion.model;

public class PersonaCurso {

	private int idPersona;
	private int idCurso;
	private Double precio;
	
	public PersonaCurso(int idPersona, int idCurso, Double precio) {
		setIdPersona(idPersona);
		setIdCurso(idCurso);
		setPrecio(precio);
	}
	
	public PersonaCurso() {}
	
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public int getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	@Override
	public String toString() {
		return "PersonasCurso [idPersona=" + idPersona + ", idCurso=" + idCurso + ", precio=" + precio + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCurso;
		result = prime * result + idPersona;
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
		if (idCurso != other.idCurso)
			return false;
		if (idPersona != other.idPersona)
			return false;
		if (precio == null) {
			if (other.precio != null)
				return false;
		} else if (!precio.equals(other.precio))
			return false;
		return true;
	}
	
	
}
