package VO;

/*
 * Clase ActividadVO que representa la entidad actividad de la BD y guarda el nombre, grupo_muscular que se trabaja, 
 * dificultad, material que se emplea, el monitor que la imparte, las plazas ofertadas inicialmente para cada clase,
 * la sala en la que se imparte y la imagen que la representa
 */
public class ActividadVO {
    String _nombre, _grupo_muscular, _dificultad, _material;
    String _monitor; 
    Integer _plazas;
    Integer _sala;
    byte[] _imagen;

    public ActividadVO(String nombre, String grupo_muscular, Integer plazas, String dificultad, String material, Integer sala, String monitor, byte[] imagen) {
        this._nombre = nombre;
        this._grupo_muscular = grupo_muscular;
        this._plazas = plazas;
        this._dificultad = dificultad;
        this._material = material;
        this._sala = sala;
        this._monitor = monitor;
        this._imagen = imagen;
    }
    
    public String getNombre() {
        return this._nombre;
    }

    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    public String getGrupo_muscular() {
        return this._grupo_muscular;
    }

    public void setGrupo_muscular(String grupo_muscular) {
        this._grupo_muscular = grupo_muscular;
    }

    public Integer getPlazas() {
        return this._plazas;
    }

    public void setPlazas(Integer plazas) {
        this._plazas = plazas;
    }

    public String getDificultad() {
        return this._dificultad;
    }

    public void setDificultad(String dificultad) {
        this._dificultad = dificultad;
    }

    public String getMaterial() {
        return this._material;
    }

    public void setMaterial(String material) {
        this._material = material;
    }

    public Integer getSala() {
        return this._sala;
    }

    public void setSala(Integer sala) {
        this._sala = sala;
    }

    public String getMonitor() {
        return this._monitor;
    }

    public void setMonitor(String monitor) { 
        this._monitor = monitor;
    }
    
    public byte[] getImagen() {
        return _imagen;
    }

    public void setImagen(byte[] imagen) {
        this._imagen = imagen;
    }
    
    public String toString() {
        return "ActividadVO [nombre=" + _nombre + ", grupoMuscular=" + _grupo_muscular + ", dificultad=" + _dificultad + ", sala=" + _sala + "]";
    }
}