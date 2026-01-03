package modelo;

public class Metrics {
    public long comparisons = 0;
    public long assignments = 0;
    public long calls = 0;

    public void reset() {
        comparisons = 0;
        assignments = 0;
        calls = 0;
    }

    public Metrics snapshot() {
        Metrics s = new Metrics();
        s.comparisons = this.comparisons;
        s.assignments = this.assignments;
        s.calls = this.calls;
        return s;
    }
}
