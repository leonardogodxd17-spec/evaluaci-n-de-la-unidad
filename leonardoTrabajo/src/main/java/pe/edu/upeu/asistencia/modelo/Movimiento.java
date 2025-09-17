package pe.edu.upeu.asistencia.modelo;

import pe.edu.upeu.asistencia.enums.Moneda;
import pe.edu.upeu.asistencia.enums.TipoMovimiento;

public class Movimiento {
    private String descripcion;
    private double monto;
    private TipoMovimiento tipo;
    private Moneda moneda;


    public Movimiento() {}

    public Movimiento(String descripcion, double monto, TipoMovimiento tipo, Moneda moneda) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.moneda = moneda;
    }




    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public Moneda getMoneda() {return moneda;}
    public void setMoneda(Moneda moneda){ this.moneda = moneda;}

}
