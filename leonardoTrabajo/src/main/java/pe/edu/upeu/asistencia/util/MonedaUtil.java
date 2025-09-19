package pe.edu.upeu.asistencia.util;
import pe.edu.upeu.asistencia.enums.Moneda;

public class MonedaUtil {
    
    // Tasas de cambio (actualizar según necesidad)
    private static final double PEN_TO_USD = 0.27;
    private static final double USD_TO_PEN = 3.70;
    private static final double PEN_TO_EUR = 0.25;
    private static final double EUR_TO_PEN = 4.0;
    private static final double USD_TO_EUR = 0.92;
    private static final double EUR_TO_USD = 1.09;
    
    public static double convertir(double monto, Moneda origen, Moneda destino) {
        // Validaciones
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las monedas no pueden ser nulas");
        }
        
        // Si es la misma moneda, retornar el mismo monto
        if (origen == destino) return monto;
        
        try {
            // Conversiones PEN
            if (origen == Moneda.PEN && destino == Moneda.USD) return monto * PEN_TO_USD;
            if (origen == Moneda.PEN && destino == Moneda.EUR) return monto * PEN_TO_EUR;
            
            // Conversiones USD
            if (origen == Moneda.USD && destino == Moneda.PEN) return monto * USD_TO_PEN;
            if (origen == Moneda.USD && destino == Moneda.EUR) return monto * USD_TO_EUR;
            
            // Conversiones EUR
            if (origen == Moneda.EUR && destino == Moneda.PEN) return monto * EUR_TO_PEN;
            if (origen == Moneda.EUR && destino == Moneda.USD) return monto * EUR_TO_USD;
            
            // Si no hay conversión directa, retornar el monto original
            return monto;
            
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Error en la conversión de moneda: " + ex.getMessage());
        }
    }
    
    public static String getSimboloMoneda(Moneda moneda) {
        if (moneda == null) return "";
        
        switch (moneda) {
            case PEN: return "S/";
            case USD: return "$";
            case EUR: return "€";
            default: return "";
        }
    }
}
