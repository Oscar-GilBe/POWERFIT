package VO;
/*
 * Clase UsuarioVO que representa la entidad usuario de la BD, que guarda el  nombre y apellidos, correo y contraseña encriptadas
 * del usuario, asi como el tipo de usuario (cliente, monitor o administrador) que es
 */
public class UsuarioVO {
    String _correo, _nombre, _apellidos, _contrasena;
    TipoUsuario _tipo;

    public UsuarioVO(String correo, String nombre, String apellidos, String contrasena, TipoUsuario tipo) {
        this._correo = correo;
        this._nombre = nombre;
        this._apellidos = apellidos;
        this._contrasena = contrasena;
        this._tipo = tipo;
    }

    public String getCorreo(){
        return _correo;
    }

    public void setCorreo(String correo){
        this._correo = correo;
    }

    public String getApellidos(){
        return _apellidos;
    }

    public void setApellidos(String apellidos){
        this._apellidos = apellidos;
    }

    public String getContrasena(){
        return _contrasena;
    }

    public void setContrasena(String contrasena){
        this._contrasena = contrasena;
    }

    public String getNombre(){
        return _nombre;
    }

    public void setNombre(String nombre){
        this._nombre = nombre;
    }

    public TipoUsuario getTipo() {
        return _tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this._tipo = tipo;
    }
}
