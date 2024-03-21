package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    
    private final ChessClient client;
    
    private final Scanner scanner;

    private State state = State.SIGNEDOUT;



    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
        scanner = new Scanner(System.in);
        state = State.SIGNEDOUT;
    }

    public void start() {

        while (true) {
            if (state == State.SIGNEDOUT) {
                printSignedOutMenu();
            } else {
                printSignedInMenu();
            }
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (state == State.SIGNEDOUT) {
                handleSignedOutInput(input);
            } else {
                //handleSignedInInput(input);
            }
        }
    }

    private void printSignedOutMenu() {
        System.out.println("1. Sign in");
        System.out.println("2. Sign up");
        System.out.println("exit");
    }

    private void printSignedInMenu() {
        System.out.println("1. List games");
        System.out.println("2. Create game");
        System.out.println("3. Join game");
        System.out.println("4. Sign out");
        System.out.println("exit");
    }

    private void handleSignedOutInput(String input) {
        switch (input) {
            case "1":
                System.out.println("Enter username:");
                String username = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();
                client.signIn(username, password);
                break;
            case "2":
                System.out.println("Enter username:");
                String newUsername = scanner.nextLine();
                System.out.println("Enter password:");
                String newPassword = scanner.nextLine();
                System.out.println("Enter email:");
                String newEmail = scanner.nextLine();
                client.register(newUsername, newPassword, newEmail);
                System.out.println("Succesfully added! Welcome " + newUsername);
                break;
            default:
                System.out.println("Invalid input");
        }
    }

//    private void handleSignedInInput(String input) {
//        switch (input) {
//            case "1":
//                client.listGames();
//                break;
//            case "2":
//                System.out.println("Enter game name:");
//                String gameName = scanner.nextLine();
//                client.createGame(gameName);
//                break;
//            case "3":
//                System.out.println("Enter game ID:");
//                int gameID = Integer.parseInt(scanner.nextLine());
//                client.joinGame(gameID);
//                break;
//            case "4":
//                client.signOut();
//                break;
//            default:
//                System.out.println("Invalid input");
//        }
    }
