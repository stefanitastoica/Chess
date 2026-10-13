package chess.game;

import chess.board.Board;
import chess.board.Position;
import chess.exception.InvalidMoveException;
import chess.pieces.Piece;
import chess.utils.ChessPair;
import chess.utils.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    private String name;
    private Colors color;
    private List<Piece> capturedPieces;
    private int points;

    public Player(String name, Colors color) {
        this.name = name;
        this.color = color;
        this.capturedPieces = new ArrayList<>();
        this.points = 0;
    }

    public void makeMove(Position from, Position to, Board board) throws InvalidMoveException {
        Piece pieceToMove = board.getPieceAt(from);

        if (pieceToMove == null) {
            throw new InvalidMoveException("Nu există piesă la poziția " + from);
        }

        if (pieceToMove.getColor() != this.color) {
            throw new InvalidMoveException("Nu poți muta piesele adversarului!");
        }

        Piece targetPiece = board.getPieceAt(to);
        board.movePiece(from, to);

        if (targetPiece != null) {
            capturedPieces.add(targetPiece);
            addPointsForCapture(targetPiece);
            System.out.println(name + " a capturat " + targetPiece.type() + "! (+ " + getPieceValue(targetPiece) + " pct)");
        }
    }

    private void addPointsForCapture(Piece captured) {
        points += getPieceValue(captured);
    }

    private int getPieceValue(Piece p) {
        switch (p.type()) {
            case 'Q': return 90;
            case 'R': return 50;
            case 'B': return 30;
            case 'N': return 30;
            case 'P': return 10;
            default: return 0;
        }
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public String getName() { return name; }
    public Colors getColor() { return color; }
    public int getPoints() { return points; }
    public List<Piece> getCapturedPieces() { return capturedPieces; }
    public TreeSet<ChessPair<Position, Piece>> getOwnedPieces(Board board) {
        TreeSet<ChessPair<Position, Piece>> owned = new TreeSet<>();
        return owned;
    }
}