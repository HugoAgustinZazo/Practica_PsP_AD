package es.gmm.psp.virtualScape.model;

public class SalasMasReservadas {
    private String sala;
    private int totalReservas;

    public SalasMasReservadas(String sala, int totalReservas) {
        this.sala = sala;
        this.totalReservas = totalReservas;
    }
    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(int totalReservas) {
        this.totalReservas = totalReservas;
    }

    @Override
    public String toString() {
        return "SalasMasReservadas{" +
                "sala='" + sala + '\'' +
                ", totalReservas=" + totalReservas +
                '}';
    }
}
