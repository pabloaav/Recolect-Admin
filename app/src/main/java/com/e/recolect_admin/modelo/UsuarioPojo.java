package com.e.recolect_admin.modelo;

@SuppressWarnings("serial")
public class UsuarioPojo {

    //region Atributos
    private String nombre;
    private String apellido;
    private String email;
    private String usuario;
    private String password;
    private String idUsuario;
    private IncidenciaPojo incidencias;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public IncidenciaPojo getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(IncidenciaPojo incidencias) {
        this.incidencias = incidencias;
    }

    @Override
    public String toString() {
        return this.getNombre() + '\'' + this.getApellido();
    }

    //endregion

}