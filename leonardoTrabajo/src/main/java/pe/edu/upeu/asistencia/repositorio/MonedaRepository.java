package pe.edu.upeu.asistencia.repositorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pe.edu.upeu.asistencia.modelo.Movimiento;

public class MonedaRepository {
    // aca se usa encapsulamiento
    private static final ObservableList<Movimiento> DATA = FXCollections.observableArrayList();

    public void save(Movimiento m) {
        DATA.add(m);
    }

    public ObservableList<Movimiento> findAll() {
        return DATA; //  devolvemos la misma lista,
    }

    public void delete(Movimiento m) {
        DATA.remove(m);
    }

    public void clear() {
        DATA.clear();
    }
}
