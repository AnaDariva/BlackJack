package client;

import shared.GameService;

import javax.swing.*;
import java.rmi.Naming;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackjackGUI().setVisible(true));
        try {
            Scanner scanner = new Scanner(System.in);
            GameService game = (GameService) Naming.lookup("rmi://localhost/BlackjackService");

            System.out.print("Digite seu nome: ");
            String name = scanner.nextLine();
            game.connect(name);

            System.out.println("Iniciando rodada...");
            var playerCards = game.startRound(name);
            System.out.println("Suas cartas: " + playerCards);
            System.out.println("Carta visível do dealer: " + game.getDealerVisibleCard(name));

            while (true) {
                System.out.print("Deseja [1] Pedir +1 carta ou [2] Parar? ");
                String option = scanner.nextLine();
                if (option.equals("1")) {
                    var cards = game.hit(name);
                    System.out.println("Suas cartas: " + cards);
                    // verificar se estourou...
                } else {
                    String result = game.stand(name);
                    System.out.println("Resultado: " + result);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
