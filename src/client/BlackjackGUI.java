package client;

import shared.GameService;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class BlackjackGUI extends JFrame {
    private GameService jogo;
    private String nome;

    private final JTextArea areaCartas = new JTextArea(5, 30);
    private final JLabel dealerLabel = new JLabel("Dealer: ?");
    private final JLabel resultadoLabel = new JLabel("");
    private final JButton hitButton = new JButton("Pedir Carta");
    private final JButton standButton = new JButton("Parar");

    public BlackjackGUI() {
        setTitle("Blackjack - Jogo 21");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        configurarInterface();
        conectarAoServidor();
    }

    private void configurarInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));

        areaCartas.setEditable(false);
        areaCartas.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaCartas);

        JPanel painelTopo = new JPanel(new BorderLayout());
        dealerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelTopo.add(dealerLabel, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());
        painelBotoes.add(hitButton);
        painelBotoes.add(standButton);

        JPanel painelRodape = new JPanel(new BorderLayout());
        resultadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        painelRodape.add(resultadoLabel, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelRodape, BorderLayout.PAGE_END);

        setContentPane(painelPrincipal);
        pack(); // Redimensiona corretamente
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
            int pontos = calcularPontos(cartas);

            if (pontos > 21) {
                resultadoLabel.setText("Você estourou! Pontuação: " + pontos);
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
            }
        } catch (Exception e) {
            mostrarErro("Erro ao pedir carta: " + e.getMessage());
        }
    }

    private void pararRodada() {
        try {
            String resultado = jogo.stand(nome);
            resultadoLabel.setText("<html>" + resultado.replaceAll("\n", "<br>") + "</html>");
            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja jogar novamente?", "Nova Rodada", JOptionPane.YES_NO_OPTION);
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
        int total = 0;
        int ases = 0;
        for (String carta : cartas) {
            String valor = carta.replaceAll("[^A-Z0-9]", "");
            if (valor.matches("\\d+")) {
                total += Integer.parseInt(valor);
            } else if ("JQK".contains(valor)) {
                total += 10;
            } else if ("A".equals(valor)) {
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
