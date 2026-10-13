package chess.game;

import chess.board.Position;
import chess.pieces.Piece;
import chess.utils.Colors;

public class Move {
    private final Colors playerColor;
    private final Position from;
    private final Position to;
    private final Piece capturedPiece;

    public Move(Colors playerColor, Position from, Position to, Piece capturedPiece) {
        this.playerColor = playerColor;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
    }

    public Colors getPlayerColor() { return playerColor; }
    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public Piece getCapturedPiece() { return capturedPiece; }

    @Override
    public String toString() {
        return "Move{" +
                "color=" + playerColor +
                ", from=" + from +
                ", to=" + to +
                ", captured=" + (capturedPiece != null ? capturedPiece.type() : "None") +
                '}';
    }
}