package modelo;

import java.util.List;

public class ResultadoTabla {
    public final String nombreAlgoritmo;
    public final String complejidadTeorica;
    public final int repeticiones;
    public final List<ResultadoFila> filas;

    public ResultadoTabla(String nombreAlgoritmo, String complejidadTeorica, int repeticiones, List<ResultadoFila> filas) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.complejidadTeorica = complejidadTeorica;
        this.repeticiones = repeticiones;
        this.filas = filas;
    }
}
