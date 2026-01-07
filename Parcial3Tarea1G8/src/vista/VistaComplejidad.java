package vista;

import modelo.ResultadoFila;
import modelo.ResultadoTabla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaComplejidad extends JFrame {

    private final JComboBox<String> cbAlgoritmo;
    private final JTextField txtNs;
    private final JTextField txtBound;
    private final JTextField txtReps;

    private final JButton btnEjecutar;
    private final JButton btnLimpiar;

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextArea txtRatios;
    private final JLabel lblEstado;

    public VistaComplejidad() {
        setTitle("Complejidad algorítmica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        cbAlgoritmo = new JComboBox<>(new String[]{
                "Búsqueda lineal (O(n))|LINEAL",
                "Búsqueda binaria (O(log n))|BINARIA",
                "Bubble sort (O(n^2))|BUBBLE",
                "Merge sort (O(n log n))|MERGE",
                "Fibonacci recursivo (O(2^n))|FIB_REC",
                "Fibonacci DP (O(n))|FIB_DP"
        });

        txtNs = new JTextField("1000,2000,4000,8000", 22);
        txtBound = new JTextField("100000", 10);
        txtReps = new JTextField("3", 6);

        btnEjecutar = new JButton("Ejecutar");
        btnLimpiar = new JButton("Limpiar");

        int row = 0;

        gc.gridx = 0; gc.gridy = row;
        top.add(new JLabel("Algoritmo:"), gc);
        gc.gridx = 1;
        top.add(cbAlgoritmo, gc);

        row++;
        gc.gridx = 0; gc.gridy = row;
        top.add(new JLabel("Lista de n:"), gc);
        gc.gridx = 1;
        top.add(txtNs, gc);
        gc.gridx = 2;
        top.add(new JLabel("Ej: 1000,2000,4000"), gc);

        row++;
        gc.gridx = 0; gc.gridy = row;
        top.add(new JLabel("Rango valores:"), gc);
        gc.gridx = 1;
        top.add(txtBound, gc);

        row++;
        gc.gridx = 0; gc.gridy = row;
        top.add(new JLabel("Repeticiones:"), gc);
        gc.gridx = 1;
        top.add(txtReps, gc);

        row++;
        gc.gridx = 0; gc.gridy = row;
        top.add(btnEjecutar, gc);
        gc.gridx = 1;
        top.add(btnLimpiar, gc);

        tableModel = new DefaultTableModel(new Object[]{"n", "tiempo(ms)", "comparaciones", "asignaciones"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        txtRatios = new JTextArea();
        txtRatios.setEditable(false);
        txtRatios.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane ratiosScroll = new JScrollPane(txtRatios);

        lblEstado = new JLabel("Listo.");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, ratiosScroll);
        split.setResizeWeight(0.55);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(lblEstado, BorderLayout.WEST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    // datos
    public String getTipoAlgoritmo() {
        String item = (String) cbAlgoritmo.getSelectedItem();
        if (item == null) return "LINEAL";
        String[] parts = item.split("\\|");
        return parts[parts.length - 1].trim();
    }

    public int[] parseNs() {
        String s = txtNs.getText().trim();
        if (s.isEmpty()) return new int[0];

        String[] parts = s.split(",");
        int[] ns = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            ns[i] = Integer.parseInt(parts[i].trim());
        }
        return ns;
    }

    public int getBound() {
        return Integer.parseInt(txtBound.getText().trim());
    }

    public int getReps() {
        return Integer.parseInt(txtReps.getText().trim());
    }

    // eventos
    public void onEjecutar(ActionListener al) { btnEjecutar.addActionListener(al); }
    public void onLimpiar(ActionListener al) { btnLimpiar.addActionListener(al); }

    public void setBotonesEnabled(boolean enabled) {
        btnEjecutar.setEnabled(enabled);
        btnLimpiar.setEnabled(enabled);
    }

    // UI
    public void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setEstado(String s) {
        lblEstado.setText(s);
    }

    public void setRatiosTexto(String t) {
        txtRatios.setText(t);
        txtRatios.setCaretPosition(0);
    }

    public void limpiarTabla() {
        tableModel.setRowCount(0);
    }

    public void cargarTabla(ResultadoTabla tabla) {
        limpiarTabla();
        for (ResultadoFila f : tabla.filas) {
            tableModel.addRow(new Object[]{
                    f.n,
                    String.format("%.3f", f.tiempoMsProm),
                    f.comparisonsProm,
                    f.assignmentsProm
            });
        }
    }
}
