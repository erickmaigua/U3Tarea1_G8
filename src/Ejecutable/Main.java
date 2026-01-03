package Ejecutable;

import controlador.ControladorAlgoritmos;
import modelo.Metrics;
import modelo.ModeloAlgoritmos;
import vista.VistaConsola;

public class Main {

	public static void main(String[] args) {
		Metrics metrics = new Metrics();
        ModeloAlgoritmos modelo = new ModeloAlgoritmos(metrics);
        VistaConsola vista = new VistaConsola();
        ControladorAlgoritmos controlador = new ControladorAlgoritmos(modelo, vista, metrics);
        controlador.iniciar();
	}

}
