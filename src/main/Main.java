package main;

import vista.VentanaPrincipal;

import javax.swing.*;


/**
 * Clase Principal del programa que hace visible la ventana principal.
 */
    public class Main {
        public static void main(String[] args) {

         SwingUtilities.invokeLater(() -> {
               new VentanaPrincipal().setVisible(true);
            });
        }
   }



