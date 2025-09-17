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
        repo.save(m);
    }

    public ObservableList<Movimiento> listarMovimientos() {
        return repo.findAll(); // 👈 devolvemos observable compartido
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
