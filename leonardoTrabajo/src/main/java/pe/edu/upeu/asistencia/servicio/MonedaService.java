package pe.edu.upeu.asistencia.servicio;

import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.enums.Moneda;
import pe.edu.upeu.asistencia.enums.TipoMovimiento;
import pe.edu.upeu.asistencia.modelo.Movimiento;
import pe.edu.upeu.asistencia.repositorio.MonedaRepository;
import pe.edu.upeu.asistencia.util.MonedaUtil;

@Service
public class MonedaService {
    private final MonedaRepository repo = new MonedaRepository();

    public void registrarMovimiento(Movimiento m) {
        if (m == null) {
            throw new IllegalArgumentException("El movimiento no puede ser nulo");
        }
        if (m.getDescripcion() == null || m.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (m.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (m.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio");
        }
        if (m.getMoneda() == null) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }
        
        repo.save(m);
    }

    public ObservableList<Movimiento> listarMovimientos() {
        return repo.findAll(); //  devolvemos observable compartido
    }

    public double calcularSaldo() {
        return listarMovimientos().stream()
                .mapToDouble(m -> m.getTipo() == TipoMovimiento.INGRESO ? m.getMonto() : -m.getMonto())
                .sum();
    }

    public double calcularBalance(Moneda monedaVista) {
        double ingresos = 0.0;
        double gastos = 0.0;

        for (Movimiento m : repo.findAll()) {
            double montoConvertido = MonedaUtil.convertir(m.getMonto(), m.getMoneda(), monedaVista);
            if (m.getTipo() == TipoMovimiento.INGRESO) ingresos += montoConvertido;
            else gastos += montoConvertido;
        }
        return ingresos - gastos;
    }
}
