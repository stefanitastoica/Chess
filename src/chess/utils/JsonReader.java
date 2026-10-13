package chess.utils;

import chess.board.Board;
import chess.board.Position;
import chess.game.Game;
import chess.game.Player;
import chess.game.User;
import chess.pieces.Piece;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonReader {

    private static final String ACCOUNTS_PATH = "accounts.json";
    private static final String GAMES_PATH = "games.json";

    public static List<User> loadData() {
        Map<Integer, Game> allGamesMap = readGames();
        return readAccounts(allGamesMap);
    }

    private static Map<Integer, Game> readGames() {
        Map<Integer, Game> gamesMap = new HashMap<>();
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(GAMES_PATH)) {
            Object obj = parser.parse(reader);
            JSONArray gamesList = (JSONArray) obj;

            for (Object gameObj : gamesList) {
                JSONObject gameJson = (JSONObject) gameObj;
                long idLong = (Long) gameJson.get("id");
                int gameId = (int) idLong;
                JSONArray playersArray = (JSONArray) gameJson.get("players");
                Player p1 = null;
                Player p2 = null;

                for (Object pObj : playersArray) {
                    JSONObject playerJson = (JSONObject) pObj;
                    String email = (String) playerJson.get("email");
                    String colorStr = (String) playerJson.get("color");
                    Colors color = Colors.valueOf(colorStr);

                    Player player = new Player(email, color);
                    if (color == Colors.WHITE) p1 = player;
                    else p2 = player;
                }

                Game game = new Game(gameId, p1, p2);
                String currentTurnColor = (String) gameJson.get("currentPlayerColor");

                if ("BLACK".equals(currentTurnColor)) {
                    game.switchPlayer();
                }

                Board board = game.getBoard();
                board.getAllPieces().clear();
                JSONArray boardArray = (JSONArray) gameJson.get("board");

                for (Object pieceObj : boardArray) {
                    JSONObject pieceJson = (JSONObject) pieceObj;
                    String typeStr = (String) pieceJson.get("type");
                    String colorStr = (String) pieceJson.get("color");
                    String posStr = (String) pieceJson.get("position");

                    char col = posStr.charAt(0);
                    int row = Integer.parseInt(posStr.substring(1));
                    Position position = new Position(col, row);

                    Colors color = Colors.valueOf(colorStr);
                    char typeChar = typeStr.charAt(0);

                    Piece piece = Piece.fromType(typeChar, color, position);
                    board.addPiece(piece);
                }

                gamesMap.put(gameId, game);
            }
            System.out.println("S-au încarcat " + gamesMap.size() + " jocuri din " + GAMES_PATH);

        } catch (IOException | ParseException e) {
            System.out.println("Nu s-au putut încarca jocurile (" + e.getMessage() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la datele din JSON" + e.getMessage());
        }

        return gamesMap;
    }

    private static List<User> readAccounts(Map<Integer, Game> loadedGames) {
        List<User> users = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(ACCOUNTS_PATH)) {
            Object obj = parser.parse(reader);
            JSONArray accountList = (JSONArray) obj;

            for (Object accountObj : accountList) {
                JSONObject accountJson = (JSONObject) accountObj;

                String email = (String) accountJson.get("email");
                String password = (String) accountJson.get("password");
                long points = (Long) accountJson.get("points");

                User user = new User(email, password, (int) points);

                JSONArray gamesIds = (JSONArray) accountJson.get("games");
                if (gamesIds != null) {
                    for (Object idObj : gamesIds) {
                        long gameIdLong = (Long) idObj;
                        int gameId = (int) gameIdLong;

                        Game game = loadedGames.get(gameId);
                        if (game != null) {
                            user.addGame(game);
                        }
                    }
                }

                users.add(user);
            }
            System.out.println("S-au încarcat " + users.size() + " conturi din " + ACCOUNTS_PATH);

        } catch (IOException | ParseException e) {
            System.out.println("Nu s-au putut incarca conturile (" + e.getMessage() + ")");
        }

        return users;
    }
}