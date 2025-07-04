/**
 * interface grafica do Blackjack usando Swing.
 * conecta ao servidor RMI e permite jogar via botoes.
 * Botbes para pedir carta, parar e iniciar nova rodada.
 */
package client;

import shared.GameService;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class BlackjackGUI extends JFrame {
    private GameService jogo;
    private String nome;

    private final JTextArea areaCartas = new JTextArea(3, 28); // Menor área!
    private final JLabel dealerLabel = new JLabel("Carta visível do Dealer: ?");
    private final JLabel resultadoLabel = new JLabel("");
    private final JButton hitButton = new JButton("Pedir Carta");
    private final JButton standButton = new JButton("Parar");

    public BlackjackGUI() {
        setTitle("Blackjack - Jogo 21");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(520, 360);
        setLocationRelativeTo(null);

        configurarInterface();
        setVisible(true);
        conectarAoServidor();
    }

    private void configurarInterface() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dealerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        areaCartas.setEditable(false);
        areaCartas.setFont(new Font("Monospaced", Font.PLAIN, 18));

        // dealer label (parte de cima)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        painel.add(dealerLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(new JScrollPane(areaCartas), gbc);

        //botoes rodapé
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        painel.add(hitButton, gbc);

        gbc.gridx = 1;
        painel.add(standButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(resultadoLabel, gbc);

        setContentPane(painel);

        hitButton.addActionListener(e -> pedirCarta());
        standButton.addActionListener(e -> pararRodada());
    }

    private void conectarAoServidor() {
        try {
            jogo = (GameService) Naming.lookup("rmi://localhost/BlackjackService");
            nome = JOptionPane.showInputDialog(this, "Digite seu nome:");
            if (nome == null || nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "Nome inválido.");
                System.exit(0);
            }
            jogo.connect(nome);
            iniciarRodada();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }
    }

    private void iniciarRodada() {
        try {
            List<String> cartas = jogo.startRound(nome);
            String dealerVisivel = jogo.getDealerVisibleCard(nome).get(0);

            dealerLabel.setText("Carta visível do Dealer: " + dealerVisivel);
            areaCartas.setText("Suas cartas: " + cartas);
            resultadoLabel.setText("");
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
        } catch (Exception e) {
            mostrarErro("Erro ao iniciar rodada: " + e.getMessage());
        }
    }

    private void pedirCarta() {
        try {
            List<String> cartas = jogo.hit(nome);
            areaCartas.setText("Suas cartas: " + cartas);
        } catch (Exception e) {

            String msg = e.getMessage();
            if (msg == null) msg = "Você estourou ou ocorreu um erro.";

            msg = msg.replaceFirst(".*RemoteException: ?", "");
            resultadoLabel.setText("<html>" + msg.replaceAll("\n", "<br>") + "</html>");
            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            int opcao = JOptionPane.showConfirmDialog(this, "Você estourou! Deseja jogar novamente?", "Nova Rodada", YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                iniciarRodada();
            } else {
                System.exit(0);
            }
        }
    }

    private void pararRodada() {
        try {
            String resultado = jogo.stand(nome);
            resultadoLabel.setText("<html>" + resultado.replaceAll("\n", "<br>") + "</html>");
            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja jogar novamente?", "Nova Rodada", YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                iniciarRodada();
            } else {
                System.exit(0);
            }
        } catch (Exception e) {
            mostrarErro("Erro ao finalizar rodada: " + e.getMessage());
        }
    }

    private int calcularPontos(List<String> cartas) {
        int total = 0, ases = 0;
        for (String carta : cartas) {
            String valor = carta.replaceAll("[^A-Z0-9]", "");
            if (valor.matches("\\d+")) total += Integer.parseInt(valor);
            else if ("JQK".contains(valor)) total += 10;
            else if ("A".equals(valor)) {
                total += 11;
                ases++;
            }
        }
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return total;
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }
}
