package com.ipartek.formacion.model;

public class Persona {

	private int id;
	private String nombre;
	private String avatar;
	
	public Persona() {
		super();
		this.id = 0;
		this.nombre = "";
		this.avatar = "avatar1.png";
	}

	public Persona(int id, String nombre, String avatar) {		
		this();
		this.id = id;
		this.nombre = nombre;
		this.avatar = avatar;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", avatar=" + avatar + "]";
	}
	
}
