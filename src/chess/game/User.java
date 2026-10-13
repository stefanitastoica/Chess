package chess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String email;
    private String password;
    private int points;
    private List<Game> activeGames;

    public User(String email, String password, int points) {
        this.email = email;
        this.password = password;
        this.points = points;
        this.activeGames = new ArrayList<>();
    }

    public User(String email, String password) {
        this(email, password, 0);
    }

    public void addGame(Game game) {
        this.activeGames.add(game);
    }

    public void removeGame(Game game) {
        this.activeGames.remove(game);
    }

    public List<Game> getActiveGames() {
        return activeGames;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", points=" + points +
                ", activeGamesCount=" + activeGames.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }


}