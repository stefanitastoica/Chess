package chess.game;

import chess.board.Board;
import chess.board.Position;
import chess.pieces.Piece;
import chess.utils.ChessPair;
import chess.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int id;
    private Player player1;
    private Player player2;
    private Board board;
    private boolean isWhiteTurn;
    private boolean isGameActive;
    public boolean userWon = false;

    private List<String> history;

    public Game(int id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board();
        this.board.initialize();
        this.isWhiteTurn = true;
        this.isGameActive = true;
        this.history = new ArrayList<>();
        saveStateToHistory();
    }

    private int currentMatchPoints = 0;
    public void processTurn(Position from, Position to) throws Exception {
        if (!isGameActive) {
            throw new Exception("Jocul s-a terminat!");
        }

        if (!isWhiteTurn) {
            throw new Exception("Nu este randul tau!");
        }

        Piece targetPiece = board.getPieceAt(to);
        board.movePiece(from, to);

        if (targetPiece != null) {
            int points = getPieceValue(targetPiece);
            this.currentMatchPoints += points;

            if (points > 0) {
                System.out.println("(Ai capturat " + targetPiece.type() + "! +" + points + " puncte)");
            }
        }

        switchPlayer();
        saveStateToHistory();
        checkRepetition();

        if (isInCheck(Colors.BLACK)) {
            System.out.println(">>> AI DAT SAH! <<<");
        }
    }
    public void playComputerMove() {
        if (!isGameActive) return;

        List<ChessPair<Position, Piece>> blackPieces = getPlayerPieces(Colors.BLACK);

        boolean moved = false;

        for (ChessPair<Position, Piece> pair : blackPieces) {
            Piece piece = pair.getValue();
            Position startPos = pair.getKey();
            List<Position> moves = piece.getPossibleMoves(board);

            for (Position endPos : moves) {
                try {
                    board.movePiece(startPos, endPos);
                    moved = true;
                    System.out.println("[Computer] A mutat " + startPos + " -> " + endPos);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (moved) break;
        }

        if (!moved) {
            this.userWon = true;
            System.out.println("Computerul nu mai are mutari legale!");
            isGameActive = false;
        } else {
            switchPlayer();
            saveStateToHistory();
            checkRepetition();
        }
    }

    private void saveStateToHistory() {
        history.add(board.toString());
    }

    private void checkRepetition() {
        if (!isGameActive) return;

        String currentState = board.toString();
        int count = 0;
        for (String state : history) {
            if (state.equals(currentState)) {
                count++;
            }
        }
        if (count >= 3) {
            System.out.println("\n[!] DETECTIE EGALITATE (Repetitie de 3 ori) [!]");
            System.out.println("Computerul considera ca partida nu mai progreseaza si RENUNTA.");
            System.out.println("Partida s-a incheiat la egalitate (REMIZA).");
            isGameActive = false;
        }
    }
    public int getMatchPoints() {
        return currentMatchPoints;
    }

    public boolean isInCheck(Colors kingColor) {
        Position kingPos = null;
        for (chess.utils.ChessPair<Position, Piece> pair : board.getAllPieces()) {
            Piece p = pair.getValue();
            if (p.type() == 'K' && p.getColor() == kingColor) {
                kingPos = pair.getKey();
                break;
            }
        }
        if (kingPos == null)
            return false;

        Colors enemyColor = (kingColor == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;

        for (chess.utils.ChessPair<Position, Piece> pair : board.getAllPieces()) {
            Piece enemyPiece = pair.getValue();

            if (enemyPiece.getColor() == enemyColor) {
                java.util.List<Position> attacks = enemyPiece.getPossibleMoves(board);
                if (attacks.contains(kingPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<ChessPair<Position, Piece>> getPlayerPieces(Colors color) {
        List<ChessPair<Position, Piece>> result = new ArrayList<>();
        for (ChessPair<Position, Piece> pair : board.getAllPieces()) {
            if (pair.getValue().getColor() == color) {
                result.add(pair);
            }
        }
        return result;
    }

    public void switchPlayer() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isActive() {
        return isGameActive;
    }

    public void setGameActive(boolean active) {
        this.isGameActive = active;
    }

    public Board getBoard() {
        return board;
    }

    private int getPieceValue(Piece p) {
        if (p == null) return 0;
        char type = p.type(); // Presupunem că returnează 'P', 'R', etc.
        switch (type) {
            case 'Q': return 90;
            case 'R': return 50;
            case 'B': return 30;
            case 'N': return 30; // Knight (Cal)
            case 'P': return 10;
            default: return 0; // Rege sau eroare
        }
    }

    public Player getCurrentPlayer() {
        return isWhiteTurn ? player1 : player2;
    }

    public int getId() { return id; }
    public Player getPlayerWhite() { return player1; }
    public Player getPlayerBlack() { return player2; }

    public void start() {
        System.out.println("Jocul #" + id + " a inceput!");
    }
}