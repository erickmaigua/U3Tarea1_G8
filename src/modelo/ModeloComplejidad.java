package modelo;

import java.util.*;

public class ModeloComplejidad {

    private final Metrics m;
    private final Random rnd = new Random();

    public ModeloComplejidad(Metrics metrics) {
        this.m = metrics;
    }

    public ResultadoTabla benchmark(String tipo, int[] ns, int bound, int reps) {
        String nombre = nombreAlgoritmo(tipo);
        String comp = complejidadTeorica(tipo);

        List<ResultadoFila> filas = new ArrayList<>();

        for (int n : ns) {
            double sumMs = 0.0;
            long sumComp = 0;
            long sumAsig = 0;

            for (int r = 0; r < reps; r++) {
                m.reset();

                long ini = System.nanoTime();
                ejecutarCaso(tipo, n, bound);
                long fin = System.nanoTime();

                double ms = (fin - ini) / 1_000_000.0;

                sumMs += ms;
                sumComp += m.comparisons;
                sumAsig += m.assignments;
            }

            filas.add(new ResultadoFila(
                    n,
                    sumMs / reps,
                    sumComp / reps,
                    sumAsig / reps
            ));
        }

        return new ResultadoTabla(nombre, comp, reps, filas);
    }

    private void ejecutarCaso(String tipo, int n, int bound) {
        switch (tipo) {
            case "LINEAL" -> {
                int[] arr = arregloAleatorio(n, bound);
                int target = arr[rnd.nextInt(n)];
                busquedaLineal(arr, target);
            }
            case "BINARIA" -> {
                int[] arr = arregloAleatorio(n, bound);
                Arrays.sort(arr);
                int target = arr[rnd.nextInt(n)];
                busquedaBinaria(arr, target);
            }
            case "BUBBLE" -> {
                int[] arr = arregloAleatorio(n, bound);
                bubbleSort(arr);
            }
            case "MERGE" -> {
                int[] arr = arregloAleatorio(n, bound);
                mergeSort(arr);
            }
            case "FIB_REC" -> fibRecursivo(n);
            case "FIB_DP" -> fibDP(n);
            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        }
    }

    private int[] arregloAleatorio(int n, int bound) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(bound);
            m.assignments++;
        }
        return a;
    }

    // O(n)
    private int busquedaLineal(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            m.comparisons++;
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // O(log n)
    private int busquedaBinaria(int[] sorted, int target) {
        int lo = 0, hi = sorted.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            m.assignments++;

            m.comparisons++;
            if (sorted[mid] == target) return mid;

            m.comparisons++;
            if (sorted[mid] < target) {
                lo = mid + 1;
                m.assignments++;
            } else {
                hi = mid - 1;
                m.assignments++;
            }
        }
        return -1;
    }

    // O(n^2)
    private int[] bubbleSort(int[] arr) {
        int[] a = Arrays.copyOf(arr, arr.length);
        m.assignments += a.length;

        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                m.comparisons++;
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    m.assignments += 3;
                }
            }
        }
        return a;
    }

    // O(n log n)
    private int[] mergeSort(int[] arr) {
        m.calls++;
        if (arr.length <= 1) return Arrays.copyOf(arr, arr.length);

        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        m.assignments += left.length + right.length;

        int[] sLeft = mergeSort(left);
        int[] sRight = mergeSort(right);
        return merge(sLeft, sRight);
    }

    private int[] merge(int[] left, int[] right) {
        int[] out = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            m.comparisons++;
            if (left[i] <= right[j]) {
                out[k++] = left[i++];
                m.assignments++;
            } else {
                out[k++] = right[j++];
                m.assignments++;
            }
        }
        while (i < left.length) {
            out[k++] = left[i++];
            m.assignments++;
        }
        while (j < right.length) {
            out[k++] = right[j++];
            m.assignments++;
        }
        return out;
    }

    // O(2^n) aprox
    private long fibRecursivo(int n) {
        m.calls++;
        m.comparisons++;
        if (n <= 1) return n;
        return fibRecursivo(n - 1) + fibRecursivo(n - 2);
    }

    // O(n)
    private long fibDP(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1;
        m.assignments += 2;

        for (int i = 2; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
            m.assignments += 3;
        }
        return b;
    }

    private String nombreAlgoritmo(String tipo) {
        return switch (tipo) {
            case "LINEAL" -> "Búsqueda lineal";
            case "BINARIA" -> "Búsqueda binaria";
            case "BUBBLE" -> "Bubble sort";
            case "MERGE" -> "Merge sort";
            case "FIB_REC" -> "Fibonacci recursivo";
            case "FIB_DP" -> "Fibonacci DP";
            default -> "Algoritmo";
        };
    }

    private String complejidadTeorica(String tipo) {
        return switch (tipo) {
            case "LINEAL" -> "O(n)";
            case "BINARIA" -> "O(log n)";
            case "BUBBLE" -> "O(n^2)";
            case "MERGE" -> "O(n log n)";
            case "FIB_REC" -> "O(2^n) aprox";
            case "FIB_DP" -> "O(n)";
            default -> "—";
        };
    }
}
