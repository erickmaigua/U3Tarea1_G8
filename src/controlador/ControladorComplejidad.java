package controlador;

import modelo.ModeloComplejidad;
import modelo.ResultadoFila;
import modelo.ResultadoTabla;
import vista.VistaComplejidad;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorComplejidad {

    private final ModeloComplejidad modelo;
    private final VistaComplejidad vista;

    private SwingWorker<ResultadoTabla, Void> workerActivo;

    public ControladorComplejidad(ModeloComplejidad modelo, VistaComplejidad vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        vista.setVisible(true);

        vista.onEjecutar(e -> ejecutar());
        vista.onLimpiar(e -> limpiar());

        // por si cierran la ventana mientras corre
        vista.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelarWorkerSiExiste();
            }
        });
    }

    private void ejecutar() {
        // si ya está corriendo algo, no lanzar otro
        if (workerActivo != null && !workerActivo.isDone()) {
            vista.mostrarError("Ya hay una ejecución en curso. Espera a que termine.");
            return;
        }

        // 1) Leer y validar entradas
        String tipo = vista.getTipoAlgoritmo();

        int[] ns;
        int bound;
        int reps;

        try {
            ns = vista.parseNs();
        } catch (Exception ex) {
            vista.mostrarError("Lista de n inválida. Ejemplo: 1000,2000,4000");
            return;
        }

        if (ns.length == 0) {
            vista.mostrarError("La lista de n está vacía.");
            return;
        }

        try {
            bound = vista.getBound();
            reps = vista.getReps();
        } catch (Exception ex) {
            vista.mostrarError("Rango o repeticiones inválidos (usa números).");
            return;
        }

        if (bound <= 0) {
            vista.mostrarError("El rango de valores debe ser > 0.");
            return;
        }
        if (reps <= 0) {
            vista.mostrarError("Las repeticiones deben ser > 0.");
            return;
        }

        // 2) Normalizar n: quitar negativos/ceros, quitar repetidos, ordenar
        ns = Arrays.stream(ns)
                .filter(n -> n > 0)
                .distinct()
                .sorted()
                .toArray();

        if (ns.length == 0) {
            vista.mostrarError("Todos los n son inválidos. Usa n > 0.");
            return;
        }

        // 3) Reglas especiales para Fibonacci recursivo
        if (tipo.equals("FIB_REC")) {
            int max = Arrays.stream(ns).max().orElse(0);
            if (max > 45) {
                vista.mostrarError("Fibonacci recursivo es muy lento. Usa n <= 45.");
                return;
            }
        }

        // 4) Preparar UI
        vista.setEstado("Ejecutando...");
        vista.setBotonesEnabled(false);
        vista.limpiarTabla();
        vista.setRatiosTexto("");

        final int[] nsFinal = ns;
        final int boundFinal = bound;
        final int repsFinal = reps;
        final String tipoFinal = tipo;

        // 5) Ejecutar en background
        workerActivo = new SwingWorker<>() {
            @Override
            protected ResultadoTabla doInBackground() {
                // corre el benchmark del modelo
                return modelo.benchmark(tipoFinal, nsFinal, boundFinal, repsFinal);
            }

            @Override
            protected void done() {
                try {
                    ResultadoTabla tabla = get();
                    vista.cargarTabla(tabla);
                    vista.setRatiosTexto(generarTextoRatios(tabla));
                    vista.setEstado("Terminado.");
                } catch (Exception ex) {
                    vista.mostrarError("Error: " + ex.getMessage());
                    vista.setEstado("Error.");
                } finally {
                    vista.setBotonesEnabled(true);
                }
            }
        };

        workerActivo.execute();
    }

    private void limpiar() {
        cancelarWorkerSiExiste();
        vista.limpiarTabla();
        vista.setRatiosTexto("");
        vista.setEstado("Listo.");
        vista.setBotonesEnabled(true);
    }

    private void cancelarWorkerSiExiste() {
        if (workerActivo != null && !workerActivo.isDone()) {
            workerActivo.cancel(true);
        }
    }

    private String generarTextoRatios(ResultadoTabla tabla) {
        Map<Integer, ResultadoFila> map = new HashMap<>();
        for (ResultadoFila f : tabla.filas) map.put(f.n, f);

        StringBuilder sb = new StringBuilder();

        // texto estilo Joyanes: "Orden de complejidad"
        sb.append("Orden de complejidad (O grande): ").append(tabla.complejidadTeorica).append("\n");
        sb.append("Algoritmo: ").append(tabla.nombreAlgoritmo).append("\n");
        sb.append("Repeticiones: ").append(tabla.repeticiones).append("\n\n");

        sb.append("Ratios T(2n)/T(n) cuando n se duplica:\n\n");

        boolean found = false;
        for (ResultadoFila f : tabla.filas) {
            int n = f.n;
            int d = n * 2;
            if (map.containsKey(d)) {
                found = true;
                double t1 = Math.max(0.000001, f.tiempoMsProm);
                double t2 = map.get(d).tiempoMsProm;
                double ratio = t2 / t1;
                sb.append("n=").append(n)
                  .append(" -> 2n=").append(d)
                  .append(" | ratio=").append(String.format("%.3f", ratio))
                  .append("\n");
            }
        }

        if (!found) {
            sb.append("No hay pares n y 2n en la lista.\n");
        }

        sb.append("\nGuía rápida (aprox):\n");
        sb.append("O(n): ~2\n");
        sb.append("O(n log n): un poco > 2\n");
        sb.append("O(n^2): ~4\n");
        sb.append("O(log n): ~1\n");
        sb.append("O(2^n): crece muchísimo\n");

        sb.append("\nNota: los tiempos dependen del PC y del sistema.\n");

        return sb.toString();
    }
}
