package server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("BlackjackService", new GameServiceImpl());
            System.out.println("Servidor Blackjack pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
