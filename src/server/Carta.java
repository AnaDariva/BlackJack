package server;

public class Carta {
    private final String valor;
    private final String naipe;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public int getValorNumerico() {
        if (valor.matches("\\d+")) {
            return Integer.parseInt(valor);
        } else if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
            return 10;
        } else if (valor.equals("A")) {
            return 11; // Ás vale 11 por padrão, será ajustado depois
        }
        return 0;
    }

    public String getValor() {
        return valor;
    }

    public boolean isAs() {
        return valor.equals("A");
    }

    @Override
    public String toString() {
        return valor + naipe;
    }

}
