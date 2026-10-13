package chess.pieces;

import chess.board.Position;
import chess.utils.Colors;

import java.util.Objects;

public abstract class Piece implements ChessPiece{
    protected final Colors color;
    protected Position position;

    public Piece(Colors color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Colors getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return type() + "-" + (color == Colors.WHITE ? "W" : "B"); // Ex: K-W, P-B [cite: 265]
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color && Objects.equals(position, piece.position);
    }


    public static Piece fromType(char type, Colors color, Position position) {
        switch (type) {
            case 'R': return new Rook(color, position);
            case 'N': return new Knight(color, position);
            case 'B': return new Bishop(color, position);
            case 'Q': return new Queen(color, position);
            case 'K': return new King(color, position);
            case 'P': return new Pawn(color, position);
            default: throw new IllegalArgumentException("Tip piesă necunoscut: " + type);
        }
    }
    public char type() {
        return this.getClass().getSimpleName().charAt(0);
    }
}