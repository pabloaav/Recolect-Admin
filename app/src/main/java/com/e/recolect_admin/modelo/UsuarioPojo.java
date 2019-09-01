package com.e.recolect_admin.modelo;

@SuppressWarnings("serial")
public class UsuarioPojo {

    //region Atributos
    private String nombre;
    private String apellido;
    private String email;
    private String idUsuario;
    private int cantInc;
    //endregion

    //region Metodos

    public UsuarioPojo() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return this.getNombre() + " " + this.getApellido();
    }

    public void setCantidad(int cuentaIncPorUsuario) {
        this.cantInc = cuentaIncPorUsuario;
    }

    public int getCantidad(){
        return this.cantInc;
    }

    //endregion

}