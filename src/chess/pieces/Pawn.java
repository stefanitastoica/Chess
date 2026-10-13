package chess.pieces;

import chess.board.Board;
import chess.board.Position;
import chess.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Colors color, Position position) {
        super(color, position);
    }

    @Override
    public char type() {
        return 'P';
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int direction = (this.color == Colors.WHITE) ? 1 : -1;

        int currentX = position.getX();
        int currentY = position.getY();

        int nextY = currentY + direction;
        if (nextY >= 1 && nextY <= 8) {
            Position forwardPos = new Position((char) currentX, nextY);
            if (board.getPieceAt(forwardPos) == null) {
                moves.add(forwardPos);

                boolean isStartPos = (this.color == Colors.WHITE && currentY == 2) ||
                        (this.color == Colors.BLACK && currentY == 7);

                if (isStartPos) {
                    int doubleMoveY = currentY + (2 * direction);
                    Position doublePos = new Position((char) currentX, doubleMoveY);

                    if (board.getPieceAt(doublePos) == null) {
                        moves.add(doublePos);
                    }
                }
            }
        }

        int[] captureDx = {-1, 1};

        for (int dx : captureDx) {
            int captureX = currentX + dx;

            if (captureX >= 'A' && captureX <= 'H' && nextY >= 1 && nextY <= 8) {
                Position capturePos = new Position((char) captureX, nextY);
                Piece pieceToCapture = board.getPieceAt(capturePos);

                if (pieceToCapture != null && pieceToCapture.getColor() != this.color) {
                    moves.add(capturePos);
                }
            }
        }

        return moves;
    }
}