package modelo;

import java.util.*;

public class ModeloAlgoritmos {

    // Algoritmos de la tarea
    private final Metrics m;
    private final Random rnd = new Random();

    public ModeloAlgoritmos(Metrics metrics) {
        this.m = metrics;
    }

    // datos dinámicos (arreglos aleatorios)
    public int[] arregloAleatorio(int n, int bound) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(bound);
            m.assignments++;
        }
        return a;
    }

    // Fuerza bruta
    public int busquedaLineal(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            m.comparisons++;
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // Divide y vencerás
    public int busquedaBinaria(int[] sorted, int target) {
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

    // Fuerza bruta
    public int[] bubbleSort(int[] arr) {
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

    // Divide y vencerás
    public int[] mergeSort(int[] arr) {
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

    // Recursión
    public long fibRecursivo(int n) {
        m.calls++;
        m.comparisons++;
        if (n <= 1) return n;
        return fibRecursivo(n - 1) + fibRecursivo(n - 2);
    }

    // Programación dinámica
    public long fibDP(int n) {
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

    // Greedy
    public List<Integer> cambioMonedasGreedy(int amount, int[] coins) {
        int[] c = Arrays.copyOf(coins, coins.length);
        Arrays.sort(c);
        m.assignments += c.length;

        List<Integer> result = new ArrayList<>();
        for (int i = c.length - 1; i >= 0; i--) {
            int coin = c[i];
            while (amount >= coin) {
                m.comparisons++;
                result.add(coin);
                amount -= coin;
                m.assignments += 2;
            }
            m.comparisons++;
        }
        return result;
    }

    // DP mochila 0/1
    public static class ResultadoMochila {
        public final int maxValor;
        public final List<Integer> itemsElegidos;
        public ResultadoMochila(int maxValor, List<Integer> itemsElegidos) {
            this.maxValor = maxValor;
            this.itemsElegidos = itemsElegidos;
        }
    }

    public ResultadoMochila mochila01DP(int[] weights, int[] values, int capacity) {
        int n = Math.min(weights.length, values.length);
        int[][] dp = new int[n + 1][capacity + 1];
        m.assignments += (long) (n + 1) * (capacity + 1);

        for (int i = 1; i <= n; i++) {
            int w = weights[i - 1];
            int v = values[i - 1];
            for (int cap = 0; cap <= capacity; cap++) {
                m.comparisons++;
                if (w <= cap) {
                    int take = dp[i - 1][cap - w] + v;
                    int skip = dp[i - 1][cap];
                    m.assignments += 2;

                    dp[i][cap] = (take > skip) ? take : skip;
                    m.comparisons++;
                    m.assignments++;
                } else {
                    dp[i][cap] = dp[i - 1][cap];
                    m.assignments++;
                }
            }
        }

        List<Integer> chosen = new ArrayList<>();
        int cap = capacity;
        for (int i = n; i >= 1; i--) {
            m.comparisons++;
            if (dp[i][cap] != dp[i - 1][cap]) {
                chosen.add(i - 1);
                cap -= weights[i - 1];
                m.assignments += 2;
            }
        }
        Collections.reverse(chosen);
        return new ResultadoMochila(dp[n][capacity], chosen);
    }

    // Backtracking
    public List<int[]> nReinas(int n) {
        int[] cols = new int[n];
        Arrays.fill(cols, -1);
        List<int[]> sols = new ArrayList<>();
        backtrackReinas(0, n, cols, sols);
        return sols;
    }

    private void backtrackReinas(int row, int n, int[] cols, List<int[]> sols) {
        m.calls++;
        if (row == n) {
            sols.add(Arrays.copyOf(cols, cols.length));
            m.assignments += cols.length;
            return;
        }
        for (int col = 0; col < n; col++) {
            m.comparisons++;
            if (seguro(row, col, cols)) {
                cols[row] = col;
                m.assignments++;
                backtrackReinas(row + 1, n, cols, sols);
                cols[row] = -1;
                m.assignments++;
            }
        }
    }

    private boolean seguro(int row, int col, int[] cols) {
        for (int r = 0; r < row; r++) {
            int c = cols[r];
            m.comparisons++;
            if (c == col) return false;
            m.comparisons++;
            if (Math.abs(c - col) == Math.abs(r - row)) return false;
        }
        return true;
    }
}
