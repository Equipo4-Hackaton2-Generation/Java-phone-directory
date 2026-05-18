package org.generation.main;

import org.generation.view.AgendaView;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación Agenda Telefónica.
 * Lanza la vista Swing en el Event Dispatch Thread (EDT).
 */
public class AgendaMain {
    public static void main(String[] args) {
        // Usar look & feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Lanzar la UI en el EDT de Swing
        SwingUtilities.invokeLater(AgendaView::new);
    }
}
