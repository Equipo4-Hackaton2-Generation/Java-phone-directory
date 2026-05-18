
package org.generation.view;

import org.generation.exceptions.CampoInvalidoException;
import org.generation.exceptions.ContactoNotFoundException;
import org.generation.model.Contacto;
import org.generation.service.AgendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class AgendaView extends JFrame {

    private final AgendaService service = new AgendaService();

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;

    private JTextField txtBuscarNombre;

    private JTextField txtModBuscarNombre;
    private JTextField txtModBuscarApellido;
    private JTextField txtModNombre;
    private JTextField txtModApellido;
    private JTextField txtModTelefono;

    private JTextField txtElimNombre;
    private JTextField txtElimApellido;

    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JLabel lblContador;

    public AgendaView() {
        setTitle("Agenda Telefonica");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    public void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Agregar",   crearTabAgregar());
        pestanias.addTab("Buscar",    crearTabBuscar());
        pestanias.addTab("Modificar", crearTabModificar());
        pestanias.addTab("Eliminar",  crearTabEliminar());
        pestanias.setPreferredSize(new Dimension(270, 0));

        String[] columnas = {"#", "Nombre", "Apellido", "Telefono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaContactos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaContactos);

        lblContador = new JLabel("0 / 10 contactos");
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(lblContador);

        panelPrincipal.add(pestanias,   BorderLayout.WEST);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelSur,    BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearTabAgregar() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo Contacto"));

        txtNombre   = new JTextField();
        txtApellido = new JTextField();
        txtTelefono = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Telefono (10 dig.):"));
        panel.add(txtTelefono);

        JButton btnAgregar = new JButton("Agregar Contacto");


        btnAgregar.addActionListener(e -> accionAgregar());


        panel.add(btnAgregar);


        return panel;
    }

    private JPanel crearTabBuscar() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar por Nombre"));

        txtBuscarNombre = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtBuscarNombre);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> accionBuscar());

        panel.add(new JLabel());
        panel.add(btnBuscar);

        return panel;
    }

    private JPanel crearTabModificar() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Modificar Contacto"));

        txtModBuscarNombre   = new JTextField();
        txtModBuscarApellido = new JTextField();
        txtModNombre         = new JTextField();
        txtModApellido       = new JTextField();
        txtModTelefono       = new JTextField();

        panel.add(new JLabel("Nombre actual:"));
        panel.add(txtModBuscarNombre);
        panel.add(new JLabel("Apellido actual:"));
        panel.add(txtModBuscarApellido);
        panel.add(new JLabel("Nuevo nombre:"));
        panel.add(txtModNombre);
        panel.add(new JLabel("Nuevo apellido:"));
        panel.add(txtModApellido);
        panel.add(new JLabel("Nuevo telefono:"));
        panel.add(txtModTelefono);

        JButton btnModificar = new JButton("Guardar Cambios");
        btnModificar.addActionListener(e -> accionModificar());

        panel.add(new JLabel());
        panel.add(btnModificar);

        return panel;
    }

    private JPanel crearTabEliminar() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Eliminar Contacto"));

        txtElimNombre   = new JTextField();
        txtElimApellido = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtElimNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtElimApellido);

        JButton btnEliminar = new JButton("Eliminar Contacto");
        btnEliminar.addActionListener(e -> accionEliminar());

        panel.add(new JLabel());
        panel.add(btnEliminar);

        return panel;
    }

    private void accionAgregar() {
        try {
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String telStr   = txtTelefono.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || telStr.isEmpty()) {
                throw new CampoInvalidoException("Todos los campos son obligatorios.");
            }

            int tel;
            try {
                tel = Integer.parseInt(telStr);
            } catch (NumberFormatException ex) {
                throw new CampoInvalidoException("El telefono solo acepta digitos numericos.");
            }

            service.aniadirContacto(new Contacto(nombre, apellido, tel));
            limpiar(txtNombre, txtApellido, txtTelefono);
            actualizarTabla();

        } catch (CampoInvalidoException | IllegalStateException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionBuscar() {
        try {
            String nombre = txtBuscarNombre.getText().trim();

            if (nombre.isEmpty()) {
                throw new CampoInvalidoException("Ingresa un nombre para buscar.");
            }

            StringBuilder sb = new StringBuilder();
            int count = 0;

            for (Contacto c : service.listarContactos()) {
                if (c.getNombre().equalsIgnoreCase(nombre)) {
                    sb.append(c.getNombre()).append(" ")
                      .append(c.getApellido()).append(" - Tel: ")
                      .append(c.getTelefono()).append("\n");
                    count++;
                }
            }

            if (count == 0) {
                throw new ContactoNotFoundException("No se encontro ningun contacto con nombre '" + nombre + "'.");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Resultados de busqueda", JOptionPane.INFORMATION_MESSAGE);

        } catch (CampoInvalidoException | ContactoNotFoundException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionModificar() {
        try {
            String nomAct = txtModBuscarNombre.getText().trim();
            String apAct  = txtModBuscarApellido.getText().trim();
            String nomNew = txtModNombre.getText().trim();
            String apNew  = txtModApellido.getText().trim();
            String telStr = txtModTelefono.getText().trim();

            if (nomAct.isEmpty() || apAct.isEmpty() || nomNew.isEmpty() || apNew.isEmpty() || telStr.isEmpty()) {
                throw new CampoInvalidoException("Todos los campos son obligatorios.");
            }

            int tel;
            try {
                tel = Integer.parseInt(telStr);
            } catch (NumberFormatException ex) {
                throw new CampoInvalidoException("El telefono solo acepta digitos numericos.");
            }

            service.modificarContacto(nomAct, apAct, nomNew, apNew, tel);
            limpiar(txtModBuscarNombre, txtModBuscarApellido, txtModNombre, txtModApellido, txtModTelefono);
            actualizarTabla();

        } catch (CampoInvalidoException | ContactoNotFoundException | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionEliminar() {
        try {
            String nombre   = txtElimNombre.getText().trim();
            String apellido = txtElimApellido.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                throw new CampoInvalidoException("Debes ingresar nombre y apellido del contacto a eliminar.");
            }

            Contacto buscado = null;
            for (Contacto c : service.listarContactos()) {
                if (c.getNombre().equalsIgnoreCase(nombre) && c.getApellido().equalsIgnoreCase(apellido)) {
                    buscado = c;
                    break;
                }
            }

            if (buscado == null) {
                throw new ContactoNotFoundException("No existe el contacto '" + nombre + " " + apellido + "'.");
            }

            int conf = JOptionPane.showConfirmDialog(
                    this,
                    "Eliminar a '" + buscado.getNombre() + " " + buscado.getApellido() + "'?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (conf == JOptionPane.YES_OPTION) {
                service.eliminarContacto(buscado);
                limpiar(txtElimNombre, txtElimApellido);
                actualizarTabla();
            }

        } catch (CampoInvalidoException | ContactoNotFoundException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        int idx = 1;
        for (Contacto c : service.listarContactos()) {
            modeloTabla.addRow(new Object[]{idx++, c.getNombre(), c.getApellido(), c.getTelefono()});
        }
        lblContador.setText(service.getTotalContactos() + " / 10 contactos");
    }

    private void limpiar(JTextField... campos) {
        for (JTextField tf : campos) {
            tf.setText("");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
