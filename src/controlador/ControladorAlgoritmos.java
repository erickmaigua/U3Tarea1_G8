package controlador;

import modelo.Metrics;
import modelo.ModeloAlgoritmos;
import modelo.ResultadoEjecucion;
import vista.VistaConsola;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControladorAlgoritmos {

    private final ModeloAlgoritmos modelo;
    private final VistaConsola vista;
    private final Metrics metrics;

    public ControladorAlgoritmos(ModeloAlgoritmos modelo, VistaConsola vista, Metrics metrics) {
        this.modelo = modelo;
        this.vista = vista;
        this.metrics = metrics;
    }

    public void iniciar() {
        while (true) {
            vista.mostrarMenu();
            String op = vista.leerTexto("Elige una opción: ").trim();

            switch (op) {
                case "1" -> { demoCompleta(); vista.pausa(); }
                case "2" -> { demoBusquedas(); vista.pausa(); }
                case "3" -> { demoOrdenamiento(); vista.pausa(); }
                case "4" -> { demoFibonacci(); vista.pausa(); }
                case "5" -> { demoGreedy(); vista.pausa(); }
                case "6" -> { demoMochila(); vista.pausa(); }
                case "7" -> { demoNReinas(); vista.pausa(); }
                case "0" -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // tiempo + métricas
    private ResultadoEjecucion ejecutar(
            String nombre,
            String metodologia,
            String complejidad,
            String tamanioEntrada,
            Supplier<Object> funcion,
            Function<Object, String> preview
    ) {
        metrics.reset();
        long ini = System.nanoTime();
        Object salida = funcion.get();
        long fin = System.nanoTime();
        double ms = (fin - ini) / 1_000_000.0;

        return new ResultadoEjecucion(
                nombre, metodologia, complejidad, tamanioEntrada,
                preview.apply(salida), ms, metrics.snapshot()
        );
    }

    // ==========================
    // DEMO COMPLETA (dinámica)
    // ==========================
    private void demoCompleta() {
        vista.titulo("DEMO COMPLETA");

        demoBusquedasInterno(false);
        demoOrdenamientoInterno(false);
        demoFibonacciInterno(false);
        demoGreedyInterno(false);
        demoMochilaInterno(false);
        demoNReinasInterno(false);

        vista.titulo("FIN DEMO");
    }

    // ==========================
    // 2) Búsquedas
    // ==========================
    private void demoBusquedas() {
        demoBusquedasInterno(true);
    }

    private void demoBusquedasInterno(boolean pedirDatos) {
        vista.titulo("BÚSQUEDAS (lineal vs binaria)");

        int n = pedirDatos ? vista.leerEnteroDefault("Tamaño del arreglo n (Enter=20000): ", 20000) : 20000;
        int bound = pedirDatos ? vista.leerEnteroDefault("Rango de valores (Enter=100000): ", 100000) : 100000;

        int[] arr = modelo.arregloAleatorio(n, bound);
        int target = arr[new Random().nextInt(n)];

        int[] ordenado = Arrays.copyOf(arr, arr.length);
        Arrays.sort(ordenado);

        ResultadoEjecucion r1 = ejecutar(
                "Búsqueda lineal",
                "Fuerza bruta",
                "O(n)",
                "n=" + n,
                () -> modelo.busquedaLineal(arr, target),
                out -> "índice=" + out + ", target=" + target
        );
        vista.imprimirResultado(r1);

        ResultadoEjecucion r2 = ejecutar(
                "Búsqueda binaria",
                "Divide y vencerás",
                "O(log n)",
                "n=" + n + " (ordenado)",
                () -> modelo.busquedaBinaria(ordenado, target),
                out -> "índice=" + out + ", target=" + target
        );
        vista.imprimirResultado(r2);
    }

    // ==========================
    // 3) Ordenamiento
    // ==========================
    private void demoOrdenamiento() {
        demoOrdenamientoInterno(true);
    }

    private void demoOrdenamientoInterno(boolean pedirDatos) {
        vista.titulo("ORDENAMIENTO (bubble vs merge)");

        int nBubble = pedirDatos ? vista.leerEnteroDefault("n para Bubble (Enter=1200): ", 1200) : 1200;
        int nMerge  = pedirDatos ? vista.leerEnteroDefault("n para Merge (Enter=30000): ", 30000) : 30000;
        int bound   = pedirDatos ? vista.leerEnteroDefault("Rango valores (Enter=100000): ", 100000) : 100000;

        int[] a1 = modelo.arregloAleatorio(nBubble, bound);
        int[] a2 = modelo.arregloAleatorio(nMerge, bound);

        ResultadoEjecucion r1 = ejecutar(
                "Bubble sort",
                "Fuerza bruta",
                "O(n^2)",
                "n=" + nBubble,
                () -> modelo.bubbleSort(a1),
                out -> "primeros10=" + Arrays.toString(Arrays.copyOf((int[]) out, Math.min(10, ((int[]) out).length)))
        );
        vista.imprimirResultado(r1);

        ResultadoEjecucion r2 = ejecutar(
                "Merge sort",
                "Divide y vencerás",
                "O(n log n)",
                "n=" + nMerge,
                () -> modelo.mergeSort(a2),
                out -> "primeros10=" + Arrays.toString(Arrays.copyOf((int[]) out, Math.min(10, ((int[]) out).length)))
        );
        vista.imprimirResultado(r2);
    }

    // ==========================
    // 4) Fibonacci
    // ==========================
    private void demoFibonacci() {
        demoFibonacciInterno(true);
    }

    private void demoFibonacciInterno(boolean pedirDatos) {
        vista.titulo("FIBONACCI (recursivo vs DP)");

        int nRec = pedirDatos ? vista.leerEnteroDefault("n recursivo (Enter=32): ", 32) : 32;
        int nDP  = pedirDatos ? vista.leerEnteroDefault("n DP (Enter=50000): ", 50000) : 50000;

        ResultadoEjecucion r1 = ejecutar(
                "Fibonacci recursivo",
                "Recursión",
                "O(2^n) aprox",
                "n=" + nRec,
                () -> modelo.fibRecursivo(nRec),
                out -> "fib(" + nRec + ")=" + out
        );
        vista.imprimirResultado(r1);

        ResultadoEjecucion r2 = ejecutar(
                "Fibonacci DP",
                "Programación dinámica",
                "O(n)",
                "n=" + nDP,
                () -> modelo.fibDP(nDP),
                out -> "calculado (n grande)"
        );
        vista.imprimirResultado(r2);
    }

    // ==========================
    // 5) Greedy
    // ==========================
    private void demoGreedy() {
        demoGreedyInterno(true);
    }

    private void demoGreedyInterno(boolean pedirDatos) {
        vista.titulo("GREEDY (cambio de monedas)");

        int amount = pedirDatos ? vista.leerEnteroDefault("Monto (Enter=289): ", 289) : 289;

        int[] coins;
        if (pedirDatos) {
            String txt = vista.leerTexto("Monedas separadas por coma (Enter=100,50,20,10,5,1): ").trim();
            coins = txt.isEmpty() ? new int[]{100, 50, 20, 10, 5, 1} : vista.parsearArregloEnteros(txt);
        } else {
            coins = new int[]{100, 50, 20, 10, 5, 1};
        }

        ResultadoEjecucion r = ejecutar(
                "Cambio de monedas (greedy)",
                "Greedy (voraz)",
                "O(m)",
                "amount=" + amount + ", coins=" + Arrays.toString(coins),
                () -> modelo.cambioMonedasGreedy(amount, coins),
                out -> {
                    @SuppressWarnings("unchecked")
                    List<Integer> lst = (List<Integer>) out;
                    int total = lst.stream().mapToInt(Integer::intValue).sum();
                    return "monedas=" + lst + ", total=" + total;
                }
        );
        vista.imprimirResultado(r);
    }

    // ==========================
    // 6) Mochila DP
    // ==========================
    private void demoMochila() {
        demoMochilaInterno(true);
    }

    private void demoMochilaInterno(boolean pedirDatos) {
        vista.titulo("DP (mochila 0/1)");

        int n = pedirDatos ? vista.leerEnteroDefault("Cantidad de items n (Enter=10): ", 10) : 10;
        int cap = pedirDatos ? vista.leerEnteroDefault("Capacidad W (Enter=30): ", 30) : 30;

        boolean aleatorio = !pedirDatos || vista.leerSiNo("¿Generar pesos/valores aleatorios? (s/n, Enter=s): ", true);

        int[] w;
        int[] v;

        if (aleatorio) {
            int wMax = pedirDatos ? vista.leerEnteroDefault("Peso máximo (Enter=10): ", 10) : 10;
            int vMax = pedirDatos ? vista.leerEnteroDefault("Valor máximo (Enter=20): ", 20) : 20;
            w = modelo.arregloAleatorio(n, wMax + 1); // incluye wMax
            v = modelo.arregloAleatorio(n, vMax + 1);
        } else {
            String tw = vista.leerTexto("Pesos (coma) ej: 2,3,4 (obligatorio): ").trim();
            String tv = vista.leerTexto("Valores (coma) ej: 3,4,5 (obligatorio): ").trim();
            w = vista.parsearArregloEnteros(tw);
            v = vista.parsearArregloEnteros(tv);
            n = Math.min(w.length, v.length);
            if (w.length != n || v.length != n) {
                w = Arrays.copyOf(w, n);
                v = Arrays.copyOf(v, n);
            }
        }

        final int[] wf = w;
        final int[] vf = v;
        final int capF = cap;

        ResultadoEjecucion r = ejecutar(
                "Mochila 0/1 (DP)",
                "Programación dinámica",
                "O(n*W)",
                "n=" + wf.length + ", W=" + capF,
                () -> modelo.mochila01DP(wf, vf, capF),
                out -> {
                    ModeloAlgoritmos.ResultadoMochila rm = (ModeloAlgoritmos.ResultadoMochila) out;
                    return "maxValor=" + rm.maxValor + ", items=" + rm.itemsElegidos;
                }
        );
        vista.imprimirResultado(r);
    }

    // ==========================
    // 7) N-Reinas
    // ==========================
    private void demoNReinas() {
        demoNReinasInterno(true);
    }

    private void demoNReinasInterno(boolean pedirDatos) {
        vista.titulo("BACKTRACKING (N-Reinas)");

        int n = pedirDatos ? vista.leerEnteroDefault("n (Enter=8): ", 8) : 8;

        ResultadoEjecucion r = ejecutar(
                "N-Reinas",
                "Backtracking",
                "Exponencial (depende de poda)",
                "n=" + n,
                () -> modelo.nReinas(n),
                out -> {
                    @SuppressWarnings("unchecked")
                    List<int[]> sols = (List<int[]>) out;
                    String first = sols.isEmpty() ? "null" : Arrays.toString(sols.get(0));
                    return "soluciones=" + sols.size() + ", primera=" + first;
                }
        );
        vista.imprimirResultado(r);
    }
}
