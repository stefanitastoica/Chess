package chess.board;
import java.util.Objects;

public class Position implements Comparable<Position>{
    private char x;
    private int y;

    public Position(char x, int y) {
        this.y = y;
        this.x = x;
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                x + y +
                '}';
    }
    @Override
    public int compareTo(Position other) {
        if (this.y != other.y)
            return Integer.compare(this.y, other.y);
        return Character.compare(this.x, other.x);
    }
}