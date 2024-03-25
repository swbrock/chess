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
            } else {
                if(client.server.authToken == null) {
                    this.state = State.SIGNEDOUT;
                }
                var message = client.evalSignedIn(input);
                System.out.println(message);
            }
        }
    }
}