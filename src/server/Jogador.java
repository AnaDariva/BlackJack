package server;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private final String nome;
    private final List<Carta> cartasJogador;
    private final List<Carta> cartasDealer;
    private int vitorias;
    private int derrotas;
    private int empates;

    public Jogador(String nome) {
        this.nome = nome;
        this.cartasJogador = new ArrayList<>();
        this.cartasDealer = new ArrayList<>();
    }

    public void novaRodada() {
        cartasJogador.clear();
        cartasDealer.clear();
    }

    public void receberCarta(Carta carta) {
        cartasJogador.add(carta);
    }

    public void dealerRecebeCarta(Carta carta) {
        cartasDealer.add(carta);
    }

    public List<String> getCartasJogador() {
        return cartasJogador.stream().map(Carta::toString).toList();
    }

    public String getPrimeiraCartaDealer() {
        return cartasDealer.isEmpty() ? "?" : cartasDealer.get(0).toString();
    }

    public int getPontuacaoJogador() {
        return calcularPontuacao(cartasJogador);
    }

    public int getPontuacaoDealer() {
        return calcularPontuacao(cartasDealer);
    }

    private int calcularPontuacao(List<Carta> cartas) {
        int total = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            total += carta.getValorNumerico();
            if (carta.isAs()) ases++;
        }

        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }

    public void registrarVitoria() {
        vitorias++;
    }

    public void registrarDerrota() {
        derrotas++;
    }

    public void registrarEmpate() {
        empates++;
    }

    public String getHistorico() {
        return "Vitórias: " + vitorias + " | Derrotas: " + derrotas + " | Empates: " + empates;
    }
}
