package es.gmm.psp.virtualScape.model;

public class RespuestaApi {

    private boolean exito;
    private String mensajeError;
    private String idGenerado;

    public RespuestaApi(boolean exito, String mensajeError, String idGenerado) {
        this.exito = exito;
        this.mensajeError = mensajeError;
        this.idGenerado = idGenerado;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getIdGenerado() {
        return idGenerado;
    }

    public void setIdGenerado(String idGenerado) {
        this.idGenerado = idGenerado;
    }
}
