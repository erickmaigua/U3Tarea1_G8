package Ejecutable;

import javax.swing.SwingUtilities;

import controlador.ControladorAlgoritmos;
import controlador.ControladorComplejidad;
import modelo.Metrics;
import modelo.ModeloAlgoritmos;
import modelo.ModeloComplejidad;
import vista.VistaComplejidad;
import vista.VistaConsola;

public class Main {

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Metrics metrics = new Metrics();
            ModeloComplejidad modelo = new ModeloComplejidad(metrics);
            VistaComplejidad vista = new VistaComplejidad();
            new ControladorComplejidad(modelo, vista).iniciar();

        });
    }

}
