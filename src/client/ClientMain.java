/**
 * cliente de Blackjack com interface grafica (Swing) utilizando RMI.
 * ao rodar este cliente, a janela grafica sera aberta automaticamente.
 * jogador pode jogar varias rodadas via GUI.
 */
package client;

import javax.swing.SwingUtilities;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackjackGUI().setVisible(true));
    }
}
