package modelo;

public class ResultadoFila {
    public final int n;
    public final double tiempoMsProm;
    public final long comparisonsProm;
    public final long assignmentsProm;

    public ResultadoFila(int n, double tiempoMsProm, long comparisonsProm, long assignmentsProm) {
        this.n = n;
        this.tiempoMsProm = tiempoMsProm;
        this.comparisonsProm = comparisonsProm;
        this.assignmentsProm = assignmentsProm;
    }
}
