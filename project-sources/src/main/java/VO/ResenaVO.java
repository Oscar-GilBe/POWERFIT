package VO;

/*
 * Clase ResenaVO que representa la entidad resena de la BD y guarda la puntuacion y cuerpo de la reseña, asi como
 * el cliente que la puso y la actividad a la que hace referencia
 */
public class ResenaVO {
    Integer _puntuacion;
    String _texto;
    String _cliente;
    String _actividad;

    public ResenaVO(Integer puntuacion, String texto, String cliente, String actividad) {
        this._puntuacion = puntuacion;
        this._texto = texto;
        this._cliente = cliente;
        this._actividad = actividad;
    }

    public Integer getPuntuacion() {
        return this._puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this._puntuacion = puntuacion;
    }

    public String getTexto() {
        return this._texto;
    }

    public void setTexto(String texto) {
        this._texto = texto;
    }

    public String getCliente() {
        return this._cliente;
    }

    public void setCliente(String cliente) {
        this._cliente = cliente;
    }

    public String getActividad() {
        return this._actividad;
    }

    public void setActividad(String actividad) {
        this._actividad = actividad;
    }
}
