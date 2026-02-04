/**
 * representa uma carta do baralho no jogo Blackjack.
 * guarda valor e naipe.
 * responsável por calcular o valor da carta no jogo.
 */
package server;

import java.io.Serializable;

public class Carta implements Serializable {
    private final String valor;
    private final String naipe;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    /** retorna valor numérico usado */
    public int getValorNumerico() {
        if (valor.matches("\\d+")) {
            return Integer.parseInt(valor);
        } else if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
            return 10;
        } else if (valor.equals("A")) {
            return 11;
        }
        return 0;
    }

    public boolean isAs() {
        return valor.equals("A");
    }

    @Override
    public String toString() {
        return valor + naipe;
    }
}
