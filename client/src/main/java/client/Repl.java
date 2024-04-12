package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    private final ChessClient client;

    private final Scanner scanner;

    private State state = State.SIGNEDOUT;


    public Repl(int urlPort) {
        client = new ChessClient(urlPort, state);
        scanner = new Scanner(System.in);
    }

    public void run() {
        client.printSignedOutMenu();
        while (true) {
            this.state = client.state;
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (state == State.SIGNEDOUT) {
                var message = client.evalSignedOut(input);
                System.out.println(message);
                this.state = State.SIGNEDIN;
            } else if (state == State.SIGNEDIN) {
                var message = client.evalSignedIn(input);
                System.out.println(message);

            } else if (state == State.INGAME) {
                var message = client.evalInGame(input);
                System.out.println(message);
            } else {
                System.out.println("Invalid state");
            }
        }
    }
}