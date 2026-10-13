package chess.board;

import chess.pieces.*;
import chess.utils.ChessPair;
import chess.utils.Colors;
import chess.exception.InvalidMoveException;

import java.util.TreeSet;

public class Board {
    private TreeSet<ChessPair<Position, Piece>> pieces;

    public Board() {
        this.pieces = new TreeSet<>();
    }
    public TreeSet<ChessPair<Position, Piece>> getAllPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        this.pieces.add(new ChessPair<>(piece.getPosition(), piece));
    }

    public Piece getPieceAt(Position position) {
        for (ChessPair<Position, Piece> pair : pieces) {
            if (pair.getKey().equals(position)) {
                return pair.getValue();
            }
        }
        return null;
    }

    public void initialize() {
        pieces.clear();

        for (char c = 'A'; c <= 'H'; c++) {
            addPiece(new Pawn(Colors.WHITE, new Position(c, 2)));
            addPiece(new Pawn(Colors.BLACK, new Position(c, 7)));
        }


        addPiece(new Rook(Colors.WHITE,   new Position('A', 1)));
        addPiece(new Knight(Colors.WHITE, new Position('B', 1)));
        addPiece(new Bishop(Colors.WHITE, new Position('C', 1)));
        addPiece(new Queen(Colors.WHITE,  new Position('D', 1)));
        addPiece(new King(Colors.WHITE,   new Position('E', 1)));
        addPiece(new Bishop(Colors.WHITE, new Position('F', 1)));
        addPiece(new Knight(Colors.WHITE, new Position('G', 1)));
        addPiece(new Rook(Colors.WHITE,   new Position('H', 1)));

        addPiece(new Rook(Colors.BLACK,   new Position('A', 8)));
        addPiece(new Knight(Colors.BLACK, new Position('B', 8)));
        addPiece(new Bishop(Colors.BLACK, new Position('C', 8)));
        addPiece(new Queen(Colors.BLACK,  new Position('D', 8)));
        addPiece(new King(Colors.BLACK,   new Position('E', 8)));
        addPiece(new Bishop(Colors.BLACK, new Position('F', 8)));
        addPiece(new Knight(Colors.BLACK, new Position('G', 8)));
        addPiece(new Rook(Colors.BLACK,   new Position('H', 8)));
    }

    public void removePieceAt(Position position) {
        Piece p = getPieceAt(position);
        if (p != null) {
            pieces.remove(new ChessPair<>(position, p));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("    -------------------------------\n");
        for (int row = 8; row >= 1; row--) {
            sb.append(row).append(" | ");

            for (char col = 'A'; col <= 'H'; col++) {
                Piece p = getPieceAt(new Position(col, row));
                if (p == null) {
                    sb.append(" .  ");
                } else {
                    sb.append(p.type()).append("-");
                    sb.append(p.getColor() == Colors.WHITE ? "W" : "B"); // Ex: W
                    sb.append(" ");
                }
            }
            sb.append("|\n");
        }
        sb.append("    -------------------------------\n");
        sb.append("     A   B   C   D   E   F   G   H \n");

        return sb.toString();
    }

    public void movePiece(Position from, Position to) throws InvalidMoveException {
        if (!isValidMove(from, to)) {
            throw new InvalidMoveException("Mutare invalidă sau regele este în șah!");
        }
        Piece pieceToMove = getPieceAt(from);
        if (pieceToMove == null) {
            throw new InvalidMoveException("Nu există piesă la poziția de start: " + from);
        }
        Piece pieceAtDestination = getPieceAt(to);
        removePieceAt(from);

        if (pieceAtDestination != null) {
            removePieceAt(to);
        }
        pieceToMove.setPosition(to);
        addPiece(pieceToMove);
        if (pieceToMove.type() == 'P') {
            promotePawn(pieceToMove);
        }
    }

    private void promotePawn(Piece pawn) {
        int y = pawn.getPosition().getY();
        if ((pawn.getColor() == Colors.WHITE && y == 8) ||
                (pawn.getColor() == Colors.BLACK && y == 1)) {
            removePieceAt(pawn.getPosition());
            Piece queen = new Queen(pawn.getColor(), pawn.getPosition());
            addPiece(queen);
            System.out.println("Pion promovat in regina la " + pawn.getPosition());
        }
    }
    public Position getKingPosition(Colors color) {
        for (ChessPair<Position, Piece> pair : pieces) {
            Piece p = pair.getValue();
            if (p.type() == 'K' && p.getColor() == color) {
                return p.getPosition();
            }
        }
        return null;
    }

    public boolean isInCheck(Colors kingColor) {
        Position kingPos = getKingPosition(kingColor);
        if (kingPos == null) return false;

        for (ChessPair<Position, Piece> pair : pieces) {
            Piece p = pair.getValue();

            if (p.getColor() != kingColor) {
                if (p.getPossibleMoves(this).contains(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isValidMove(Position from, Position to) {
            Piece pieceToMove = getPieceAt(from);

            if (pieceToMove == null) return false;
            if (from.equals(to)) return false;
            if (!pieceToMove.getPossibleMoves(this).contains(to)) {
                return false;
            }

            Piece capturedPiece = getPieceAt(to);
            Position originalPos = from;
            removePieceAt(from);

            if (capturedPiece != null) {
                removePieceAt(to);
            }
            pieceToMove.setPosition(to);
            addPiece(pieceToMove);

            boolean kingIsSafe = !isInCheck(pieceToMove.getColor());
            removePieceAt(to);

            pieceToMove.setPosition(originalPos);
            addPiece(pieceToMove);

            if (capturedPiece != null) {
                addPiece(capturedPiece);
            }

            return kingIsSafe;
    }
}