/**
 * implementa a lógica principal do jogo Blackjack.
 * mantém baralho, jogadores, dealer e métodos remotos do serviço.
 * cada jogador tem estado independente.
 */
package server;

import shared.GameService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameServiceImpl extends UnicastRemoteObject implements GameService {

    private final Map<String, Jogador> jogadores;
    private final List<Carta> baralho;

    public GameServiceImpl() throws RemoteException {
        jogadores = new HashMap<>();
        baralho = new ArrayList<>();
    }

    @Override
    public synchronized String connect(String nome) throws RemoteException {
        if (!jogadores.containsKey(nome)) {
            jogadores.put(nome, new Jogador(nome));
            return "Jogador " + nome + " conectado com sucesso.";
        }
        return "Jogador " + nome + " já está conectado.";
    }

    /**
     * inicia uma nova rodada para o jogador.
     * entrega 2 cartas ao jogador e 2 ao dealer (mas so uma fica visivel para o outro).
     * retorna as cartas do jogador.
     */
    @Override
    public synchronized List<String> startRound(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");

        reiniciarBaralho();
        jogador.novaRodada();

        jogador.receberCarta(comprarCarta());
        jogador.receberCarta(comprarCarta());
        jogador.dealerRecebeCarta(comprarCarta());
        jogador.dealerRecebeCarta(comprarCarta());

        return jogador.getCartasJogador();
    }

    /**
     * jogador pede uma nova carta.
     * retorna cartas atualizadas.
     * se ultrapassar 21, anota a derrota e lança exceçao para encerrar a rodada.
     */
    @Override
    public synchronized List<String> hit(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");

        jogador.receberCarta(comprarCarta());
        if (jogador.getPontuacaoJogador() > 21) {
            jogador.registrarDerrota();
            throw new RemoteException("Você estourou com " + jogador.getPontuacaoJogador() + " pontos!");
        }
        return jogador.getCartasJogador();
    }

    /**
     * jogador encerra a rodada e o dealer joga automaticamente.
     * calcula o resultado, atualiza historico e retorna mensagem de resultado.
     */
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

    /**
     * retorna a carta visivel do dealer para o jogador
     */
    @Override
    public synchronized List<String> getDealerVisibleCard(String nome) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        if (jogador == null) throw new RemoteException("Jogador não encontrado!");
        return Collections.singletonList(jogador.getPrimeiraCartaDealer());
    }

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

    /**
     * retira a primeira carta de cima do baralho
     */
    private Carta comprarCarta() {
        return baralho.remove(0);
    }
}
