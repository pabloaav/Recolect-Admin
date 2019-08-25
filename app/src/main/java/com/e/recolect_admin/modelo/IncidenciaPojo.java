package com.e.recolect_admin.modelo;

import java.util.Map;

public class IncidenciaPojo {
    private String tipo;
    private String fecha;
    private String imagen;
    private String descripcion;
    private String direccion;
    private Map<String, Object> ubicacion;
    private String cadenaUbicacion;
    private Map<String, Boolean> estado;

    public IncidenciaPojo() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Map<String, Object> getUbicacion() {
        return ubicacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setUbicacion(Map<String, Object> ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCadenaUbicacion() {
        String cadena = String.valueOf(this.ubicacion.get("cadenaUbicacion"));
        return cadena;
    }

    public void setCadenaUbicacion(String cadenaUbicacion) {
        this.cadenaUbicacion = cadenaUbicacion;
    }

    public Map<String, Boolean> getEstado() {
        return estado;
    }

    public void setEstado(Map<String, Boolean> estado) {
        this.estado = estado;
    }
}
