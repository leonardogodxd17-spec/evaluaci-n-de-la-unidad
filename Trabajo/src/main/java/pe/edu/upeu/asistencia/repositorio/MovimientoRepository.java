package pe.edu.upeu.asistencia.repositorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pe.edu.upeu.asistencia.modelo.Movimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientoRepository {
    // Usar ObservableList compartida
    private static final ObservableList<Movimiento> DATA = FXCollections.observableArrayList();

    public void save(Movimiento m) {
        DATA.add(m);
    }

    public ObservableList<Movimiento> findAll() {
        return DATA; // 👈 devolvemos la misma lista, no una copia
    }

    public void delete(Movimiento m) {
        DATA.remove(m);
    }

    public void clear() {
        DATA.clear();
    }
}
