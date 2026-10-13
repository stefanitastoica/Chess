package chess.pieces;

import chess.board.Board;
import chess.board.Position;
import chess.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(Colors color, Position position) {
        super(color, position);
    }

    @Override
    public char type() {
        return 'R';
    }
    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int currentX = position.getX();
            int currentY = position.getY();

            while (true) {
                currentX += dx[i];
                currentY += dy[i];

                if (currentX < 'A' || currentX > 'H' || currentY < 1 || currentY > 8) {
                    break;
                }

                Position nextPosition = new Position((char) currentX, currentY);
                Piece pieceAtPosition = board.getPieceAt(nextPosition);

                if (pieceAtPosition == null) {
                    moves.add(nextPosition);
                }
                else {
                    if (pieceAtPosition.getColor() != this.color) {
                        moves.add(nextPosition);
                    }
                    break;
                }
            }
        }
        return moves;
    }
}