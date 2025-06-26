package server;

import shared.GameService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameServiceImpl extends UnicastRemoteObject implements GameService {

    private static final long serialVersionUID = 1L;

    // Armazena o estado dos jogadores
    private final Map<String, Jogador> jogadores;

    // Baralho (reiniciado por rodada)
    private final List<Carta> baralho;

    public GameServiceImpl() throws RemoteException {
        super();
        jogadores = new HashMap<>();
        baralho = new ArrayList<>();
    }

    // Conecta jogador pelo nome
    @Override
    public synchronized String connect(String nome) throws RemoteException {
        if (!jogadores.containsKey(nome)) {
            jogadores.put(nome, new Jogador(nome));
            return "Jogador " + nome + " conectado com sucesso.";
        }
        return "Jogador " + nome + " já está conectado.";
    }

    @Override
    public synchronized List<String> startRound(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");

        reiniciarBaralho();
        jogador.novaRodada();

        // Duas cartas para o jogador
        jogador.receberCarta(comprarCarta());
        jogador.receberCarta(comprarCarta());

        // Duas cartas para o dealer
        jogador.dealerRecebeCarta(comprarCarta()); // visível
        jogador.dealerRecebeCarta(comprarCarta()); // oculta

        return jogador.getCartasJogador();
    }

    @Override
    public synchronized List<String> hit(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");
        jogador.receberCarta(comprarCarta());
        return jogador.getCartasJogador();
    }

    @Override
    public synchronized String stand(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");

        while (jogador.getPontuacaoDealer() < 17) {
            jogador.dealerRecebeCarta(comprarCarta());
        }

        int pJogador = jogador.getPontuacaoJogador();
        int pDealer = jogador.getPontuacaoDealer();

        String resultado;

        if (pJogador > 21) {
            jogador.registrarDerrota();
            resultado = "Você estourou com " + pJogador + ". Derrota.";
        } else if (pDealer > 21) {
            jogador.registrarVitoria();
            resultado = "Dealer estourou com " + pDealer + ". Você venceu!";
        } else if (pJogador > pDealer) {
            jogador.registrarVitoria();
            resultado = "Você venceu! (" + pJogador + " x " + pDealer + ")";
        } else if (pJogador == pDealer) {
            jogador.registrarEmpate();
            resultado = "Empate. Dealer vence. (" + pJogador + " x " + pDealer + ")";
        } else {
            jogador.registrarDerrota();
            resultado = "Você perdeu. (" + pJogador + " x " + pDealer + ")";
        }

        resultado += "\nHistórico: " + jogador.getHistorico();
        return resultado;
    }

    @Override
    public synchronized List<String> getDealerVisibleCard(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");
        return Collections.singletonList(jogador.getPrimeiraCartaDealer());
    }

    // Método auxiliar: reinicia e embaralha baralho
    private void reiniciarBaralho() {
        baralho.clear();
        String[] naipes = {"♠", "♥", "♦", "♣"};
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String naipe : naipes) {
            for (String valor : valores) {
                baralho.add(new Carta(valor, naipe));
            }
        }

        Collections.shuffle(baralho);
    }

    private Carta comprarCarta() {
        return baralho.remove(0);
    }
}
