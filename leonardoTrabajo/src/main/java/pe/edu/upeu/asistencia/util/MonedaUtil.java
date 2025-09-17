package pe.edu.upeu.asistencia.util;
import pe.edu.upeu.asistencia.enums.Moneda;

public class MonedaUtil {
    public static double convertir(double monto, Moneda origen, Moneda destino) {
        if (origen == destino) return monto;
        // Ejemplo de tasas (actualízalas según tu necesidad)
        if (origen == Moneda.PEN && destino == Moneda.USD) return monto * 0.27;
        if (origen == Moneda.USD && destino == Moneda.PEN) return monto * 3.70;
        if (origen == Moneda.PEN && destino == Moneda.EUR) return monto * 0.25;
        if (origen == Moneda.EUR && destino == Moneda.PEN) return monto * 4.0;
        if (origen == Moneda.USD && destino == Moneda.EUR) return monto * 0.92;
        if (origen == Moneda.EUR && destino == Moneda.USD) return monto * 1.09;
        return monto; // fallback
    }

}
