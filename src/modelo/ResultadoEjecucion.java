package modelo;

public class ResultadoEjecucion {
    public final String nombre;
    public final String metodologia;
    public final String complejidadTeorica;
    public final String tamanioEntrada;
    public final String previewSalida;
    public final double tiempoMs;
    public final Metrics metrics;

    public ResultadoEjecucion(String nombre, String metodologia, String complejidadTeorica,
                             String tamanioEntrada, String previewSalida, double tiempoMs, Metrics metrics) {
        this.nombre = nombre;
        this.metodologia = metodologia;
        this.complejidadTeorica = complejidadTeorica;
        this.tamanioEntrada = tamanioEntrada;
        this.previewSalida = previewSalida;
        this.tiempoMs = tiempoMs;
        this.metrics = metrics;
    }
}
