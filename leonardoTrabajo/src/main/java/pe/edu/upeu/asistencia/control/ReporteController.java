package pe.edu.upeu.asistencia.control;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.Moneda;
import pe.edu.upeu.asistencia.modelo.Movimiento;
import pe.edu.upeu.asistencia.servicio.MonedaService;

@Controller
public class ReporteController {
    @FXML
    private TableView<Movimiento> tablaReporte;
    @FXML
    private TableColumn<Movimiento, String> colDescripcion;
    @FXML
    private TableColumn<Movimiento, Double> colMonto;
    @FXML
    private TableColumn<Movimiento, String> colTipo;
    @FXML
    private Label saldoLabel;
    @FXML
    private ComboBox<Moneda> monedaCombo;


    @Autowired
    private MonedaService service;

    @FXML
    public void initialize() {
        try {
            // Configurar columnas de la tabla
            colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            
            // Configurar combo de monedas
            monedaCombo.setItems(FXCollections.observableArrayList(Moneda.values()));
            monedaCombo.getSelectionModel().selectFirst();
            monedaCombo.setOnAction(e -> actualizarSaldo());

            // Conectar tabla al observable compartido
            tablaReporte.setItems(service.listarMovimientos());

            // Listener para recalcular saldo cuando cambie la lista
            service.listarMovimientos().addListener((javafx.collections.ListChangeListener<Movimiento>) c -> {
                actualizarSaldo();
            });

            // Mostrar saldo inicial
            actualizarSaldo();
            
        } catch (Exception ex) {
            saldoLabel.setText("Error al inicializar reporte");
            ex.printStackTrace();
        }
    }
    
    private void actualizarSaldo() {
        try {
            Moneda monedaSeleccionada = monedaCombo.getValue();
            if (monedaSeleccionada == null) {
                saldoLabel.setText("Saldo: 0.00");
                return;
            }
            
            double saldo = service.calcularBalance(monedaSeleccionada);
            String simbolo = getSimboloMoneda(monedaSeleccionada);
            saldoLabel.setText(String.format("Saldo: %s %.2f", simbolo, saldo));
        } catch (Exception ex) {
            saldoLabel.setText("Error al calcular saldo");
            ex.printStackTrace();
        }
    }
    
    private String getSimboloMoneda(Moneda moneda) {
        switch (moneda) {
            case PEN: return "S/";
            case USD: return "$";
            case EUR: return "€";
            default: return "";
        }
    }

    public void cargarDatos() {
        saldoLabel.setText(String.format("%.2f", service.calcularSaldo()));
    }
}
