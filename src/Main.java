package chess;

import chess.board.Position;
import chess.game.Game;
import chess.game.Player;
import chess.game.User;
import chess.pieces.Piece;
import chess.utils.Colors;
import chess.utils.JsonReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {


    private List<User> users;
    private Map<Integer, Game> games;
    private User currentUser;
    private Scanner scanner;

    public Main() {
        this.users = new ArrayList<>();
        this.games = new HashMap<>();
        this.currentUser = null;
        this.scanner = new Scanner(System.in);
    }


    public void read() {
        System.out.println("Se incarca datele...");

        List<User> Users = JsonReader.loadData();

        if (Users != null) {
            this.users = Users;
            for (User u : users) {
                for (Game g : u.getActiveGames()) {
                    this.games.put(g.getId(), g);
                }
            }
        }
    }

    public void write() {
        System.out.println("Se salveaza datele...");
        saveGames();
        saveAccounts();
    }

    private void saveGames() {
        JSONArray gamesList = new JSONArray();

        for (Game game : this.games.values()) {
            JSONObject gameJson = new JSONObject();
            gameJson.put("id", game.getId());
            JSONArray playersJson = new JSONArray();

            JSONObject p1 = new JSONObject();
            p1.put("email", game.getPlayerWhite().getName());
            p1.put("color", "WHITE");
            playersJson.add(p1);

            JSONObject p2 = new JSONObject();
            p2.put("email", game.getPlayerBlack().getName());
            p2.put("color", "BLACK");
            playersJson.add(p2);

            gameJson.put("players", playersJson);

            String turn = (game.getCurrentPlayer().getColor() == Colors.WHITE) ? "WHITE" : "BLACK";
            gameJson.put("currentPlayerColor", turn);

            JSONArray boardJson = new JSONArray();
            for (chess.utils.ChessPair<Position, Piece> pair : game.getBoard().getAllPieces()) {
                JSONObject pieceJson = new JSONObject();
                Piece piece = pair.getValue();
                Position pos = pair.getKey();

                pieceJson.put("type", String.valueOf(piece.type()));
                pieceJson.put("color", piece.getColor().toString());
                pieceJson.put("position", "" + pos.getX() + pos.getY());

                boardJson.add(pieceJson);
            }
            gameJson.put("board", boardJson);

            gamesList.add(gameJson);
        }

        try (FileWriter file = new FileWriter("games.json")) {
            file.write(gamesList.toJSONString());
            file.flush();
            System.out.println(" -> games.json actualizat.");
        } catch (IOException e) {
            System.out.println("Eroare la scrierea games.json: " + e.getMessage());
        }
    }

    private void saveAccounts() {
        JSONArray accountsList = new JSONArray();

        for (User user : this.users) {
            JSONObject userJson = new JSONObject();
            userJson.put("email", user.getEmail());
            userJson.put("password", user.getPassword());
            userJson.put("points", user.getPoints());

            JSONArray gamesIds = new JSONArray();
            for (Game g : user.getActiveGames()) {
                gamesIds.add(g.getId());
            }
            userJson.put("games", gamesIds);
            accountsList.add(userJson);
        }

        try (FileWriter file = new FileWriter("accounts.json")) {
            file.write(accountsList.toJSONString());
            file.flush();
            System.out.println(" -> accounts.json actualizat.");
        } catch (IOException e) {
            System.out.println("Eroare la scrierea accounts.json: " + e.getMessage());
        }
    }

    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                this.currentUser = u;
                return u;
            }
        }
        return null;
    }

    public User newAccount(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                System.out.println("Exista deja un cont cu acest email.");
                return null;
            }
        }

        User newUser = new User(email, password);
        this.users.add(newUser);
        this.currentUser = newUser;

        return newUser;
    }

    public void run() {
        boolean running = true;
        System.out.println("--- CHESS GAME ---");

        while (running) {
            if (currentUser == null) {
                System.out.println("\n1. Login");
                System.out.println("2. Register (New Account)");
                System.out.println("3. Exit");
                System.out.print("Alege: ");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Parola: ");
                    String pass = scanner.nextLine();

                    User u = login(email, pass);
                    if (u != null) {
                        System.out.println("Bine ai venit, " + u.getEmail());
                    } else {
                        System.out.println("Parola/email gresite.");
                    }
                }
                else if (choice.equals("2")) {
                    System.out.print("Email nou: ");
                    String email = scanner.nextLine();
                    System.out.print("Parola noua: ");
                    String pass = scanner.nextLine();

                    User u = newAccount(email, pass);
                    if (u != null) {
                        System.out.println("Cont creat!");
                    }
                }
                else if (choice.equals("3")) {
                    running = false;
                }
                else {
                    System.out.println("Comanda invalida.");
                }
            } else {
                System.out.println("\n--- Meniu Utilizator: " + currentUser.getEmail() + " ---");
                System.out.println("Total puncte: " + currentUser.getPoints());
                System.out.println("1. Joc Nou");
                System.out.println("2. Continua Joc");
                System.out.println("3. Delogare");
                System.out.print("Alege: ");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    NewGame();
                }
                else if (choice.equals("2")) {
                    ResumeGame();
                }
                else if (choice.equals("3")) {
                    currentUser = null;
                    System.out.println("Te-ai delogat.");
                }
                else {
                    System.out.println("Comanda invalida.");
                }
            }
        }
    }

    private void NewGame() {
        Player p1 = new Player(currentUser.getEmail(), Colors.WHITE);
        Player p2 = new Player("Computer", Colors.BLACK);

        int newId = games.size() + 1;
        while (games.containsKey(newId)) newId++;

        Game game = new Game(newId, p1, p2);
        game.start();

        currentUser.addGame(game);
        games.put(newId, game);

        playGameLoop(game);
    }

    private void ResumeGame() {
        List<Game> myGames = currentUser.getActiveGames();
        if (myGames.isEmpty()) {
            System.out.println("Nu ai jocuri active.");
            return;
        }

        System.out.println("Jocurile tale:");
        for (int i = 0; i < myGames.size(); i++) {
            System.out.println((i + 1) + ". ID " + myGames.get(i).getId());
        }

        System.out.print("Alege numarul din lista: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < myGames.size()) {
                Game game = myGames.get(idx);
                game.setGameActive(true);
                playGameLoop(game);
            } else {
                System.out.println("Numar invalid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input invalid.");
        }
    }

    private void playGameLoop(Game game) {
        System.out.println("--- MECI IN DESFASURARE ---");
        System.out.println("Comenzi: 'E2-E4', 'E2', 'renunt' (te predai), 'leave' (pauza)");

        while (game.isActive()) {
            System.out.println("\n" + game.getBoard());
            System.out.print("Comanda ta (" + currentUser.getEmail() + "): ");

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("LEAVE") || input.equals("EXIT")) {
                System.out.println("Joc pus pe pauza. Progresul (Y=" + game.getMatchPoints() + ") este salvat.");
                return;
            }

            if (input.equals("RENUNT")) {
                int X = currentUser.getPoints();
                int Y = game.getMatchPoints();
                int X_nou = X + Y - 150;

                currentUser.setPoints(X_nou);
                System.out.println("Ai renuntat.");
                System.out.println("Calcul: " + X + " + " + Y + " - 150 = " + X_nou);

                game.setGameActive(false);
                currentUser.removeGame(game);
                games.remove(game.getId());
                return;
            }

            try {
                if (input.length() == 2) {
                    Position pos = new Position(input.charAt(0), input.charAt(1) - '0');
                    Piece p = game.getBoard().getPieceAt(pos);
                    if (p != null && p.getColor() == Colors.WHITE) {
                        System.out.print("Mutari posibile: ");
                        for(Position move : p.getPossibleMoves(game.getBoard())) {
                            System.out.print(move + " ");
                        }
                        System.out.println();
                    } else {
                        System.out.println("Nu ai o piesa acolo.");
                    }
                }
                else if (input.length() == 5 && input.charAt(2) == '-') {
                    Position from = new Position(input.charAt(0), input.charAt(1) - '0');
                    Position to = new Position(input.charAt(3), input.charAt(4) - '0');

                    game.processTurn(from, to);
                    if (game.isActive()) {
                        game.playComputerMove();
                    }
                } else {
                    System.out.println("Format invalid.");
                }
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        int X = currentUser.getPoints();
        int Y = game.getMatchPoints();
        int X_nou = X;

        if (game.userWon) {
            if (game.isInCheck(Colors.BLACK)) {
                System.out.println("\nSAH MAT! AI CASTIGAT!");
                X_nou = X + Y + 300;
                System.out.println("Total nou: " + X + " + " + Y + " + 300 = " + X_nou);
            } else {

                System.out.println("\nJoc terminat: Computerul renunta (Remiza).");
                X_nou = X + Y + 150;
                System.out.println("Total nou (Remiza): " + X + " + " + Y + " + 150 = " + X_nou);
            }
        }
        else {
            if (game.isInCheck(Colors.WHITE)) {
                System.out.println("\nSAH MAT! AI PIERDUT!");
                X_nou = X + Y - 300;
                System.out.println("Total nou: " + X + " + " + Y + " - 300 = " + X_nou);
            } else {

                System.out.println("\nJoc terminat: Ai pierdut.");
                X_nou = X + Y - 150;
                System.out.println("Total nou: " + X + " + " + Y + " - 150 = " + X_nou);
            }
        }

        currentUser.setPoints(X_nou);
        currentUser.removeGame(game);
        games.remove(game.getId());
    }
    public static void main(String[] args) {
        Main app = new Main();
        app.read();
        app.run();
        app.write();
    }
}