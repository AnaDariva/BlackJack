/**
 * inicializa o servidor do Blackjack via RMI
 * cria registro RMI na porta 1099 e publica o serviço com o nome "BlackjackService"
 */
package server;

import shared.GameService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            GameService jogo = new GameServiceImpl();
            Naming.rebind("BlackjackService", jogo);
            System.out.println("Servidor Blackjack iniciado com sucesso na porta 1099.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
