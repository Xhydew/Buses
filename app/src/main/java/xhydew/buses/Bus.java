package xhydew.buses;

/**
 * Created by Xhyde on 28/10/2016.
 */
public class Bus {
    private int tiempo;
    private int bus;
    private int metros;
    private String linea;

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getBus() {
        return bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public int getMetros() {
        return metros;
    }

    public void setMetros(int metros) {
        this.metros = metros;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public Bus(int tiempo, int bus, int metros, String linea) {
        this.tiempo = tiempo;
        this.bus = bus;
        this.metros = metros;
        this.linea = linea;
    }
}
