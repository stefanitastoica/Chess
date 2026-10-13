package chess.pieces;

import chess.board.Board;
import chess.board.Position;
import chess.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(Colors color, Position position) {
        super(color, position);
    }

    @Override
    public char type() {
        return 'N';
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[] dx = {1, 1, -1, -1, 2, 2, -2, -2};
        int[] dy = {2, -2, 2, -2, 1, -1, 1, -1};

        for (int i = 0; i < 8; i++) {
            int targetX = position.getX() + dx[i];
            int targetY = position.getY() + dy[i];

            if (targetX < 'A' || targetX > 'H' || targetY < 1 || targetY > 8) {
                continue;
            }

            Position targetPos = new Position((char) targetX, targetY);
            Piece pieceAtDestination = board.getPieceAt(targetPos);

            if (pieceAtDestination == null) {
                moves.add(targetPos);
            } else {
                if (pieceAtDestination.getColor() != this.color) {
                    moves.add(targetPos);
                }
            }
        }

        return moves;
    }
}