package chess.pieces;

import chess.board.Board;
import chess.board.Position;
import java.util.List;

public interface ChessPiece {
    List<Position> getPossibleMoves(Board board);
    default boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }
    char type();
}