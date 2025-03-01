package es.gmm.psp.virtualScape.model;

public class Fecha {

    private int horaReserva;
    private int diaReserva;

    public Fecha(int horaReserva, int diaReserva) {
        this.horaReserva = horaReserva;
        this.diaReserva = diaReserva;
    }

    public int getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(int horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(int diaReserva) {
        this.diaReserva = diaReserva;
    }
}
