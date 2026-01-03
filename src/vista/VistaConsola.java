package vista;

import modelo.ResultadoEjecucion;

import java.util.Scanner;

public class VistaConsola {
    private final Scanner sc = new Scanner(System.in);

    public void titulo(String t) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(t);
        System.out.println("=".repeat(70));
    }

    public void mostrarMenu() {
        titulo("MENÚ");
        System.out.println("1) Demo completa");
        System.out.println("2) Búsquedas (lineal vs binaria)");
        System.out.println("3) Ordenamiento (bubble vs merge)");
        System.out.println("4) Fibonacci (recursivo vs DP)");
        System.out.println("5) Greedy (cambio de monedas)");
        System.out.println("6) DP (mochila 0/1)");
        System.out.println("7) Backtracking (N-reinas)");
        System.out.println("0) Salir");
    }

    public String leerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    public int leerEnteroDefault(String msg, int def) {
        System.out.print(msg);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        return Integer.parseInt(s);
    }

    public boolean leerSiNo(String msg, boolean def) {
        System.out.print(msg);
        String s = sc.nextLine().trim().toLowerCase();
        if (s.isEmpty()) return def;
        return s.startsWith("s");
    }

    public int[] parsearArregloEnteros(String texto) {
        String[] parts = texto.split(",");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            out[i] = Integer.parseInt(parts[i].trim());
        }
        return out;
    }

    public void imprimirResultado(ResultadoEjecucion r) {
        System.out.println("\nAlgoritmo: " + r.nombre);
        System.out.println("Metodología: " + r.metodologia);
        System.out.println("Complejidad: " + r.complejidadTeorica);
        System.out.println("Entrada: " + r.tamanioEntrada);
        System.out.printf("Tiempo: %.3f ms%n", r.tiempoMs);

        System.out.println("Métricas: comparaciones=" + r.metrics.comparisons
                + ", asignaciones=" + r.metrics.assignments
                + ", llamadas=" + r.metrics.calls);

        System.out.println("Salida: " + r.previewSalida);
    }

    public void pausa() {
        System.out.print("\nEnter para volver...");
        sc.nextLine();
    }
}
