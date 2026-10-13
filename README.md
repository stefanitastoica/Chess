# Java Chess Application

A fully functional, desktop-based Chess application built from scratch in Java[cite: 3]. This project focuses on clean Object-Oriented Design, decoupling the complex game logic from the graphical user interface, and implementing data persistence.

## Features

*   **Complete Chess Logic:** Implements all standard chess movement rules, including specific piece behaviors (Pawns, Knights, Bishops, Rooks, Queens, Kings)[cite: 3].
*   **Graphical User Interface (GUI):** A custom, interactive chessboard built using Java Swing, featuring real-time move rendering[cite: 3].
*   **Data Persistence (JSON):** 
    *   **User Accounts:** System to register and manage user profiles (`accounts.json`)[cite: 3].
    *   **Save/Load Games:** Ability to serialize the current board state and save it to a JSON file (`games.json`), allowing players to resume matches later[cite: 3].
*   **Robust Error Handling:** Custom exceptions for invalid commands (`InvalidCommandException`) and illegal moves (`InvalidMoveException`) to ensure game integrity[cite: 3].

## Tech Stack

*   **Language:** Java (JDK 8+ recommended)[cite: 3]
*   **GUI Framework:** Java Swing[cite: 3]
*   **Data Format:** JSON[cite: 3]
*   **Dependencies:** `json-simple-1.1.1.jar` (included in the repository for parsing JSON files)[cite: 3]

## Architecture

The project is structured following Object-Oriented principles to ensure maintainability and separation of concerns:

*   `src/chess/board/`: Contains the `Board` and `Position` classes, managing the physical grid and coordinates[cite: 3].
*   `src/chess/pieces/`: Abstract `Piece` / `ChessPiece` classes and concrete implementations for every chess piece (e.g., `King.java`, `Knight.java`), each defining its own valid movement logic[cite: 3].
*   `src/chess/game/`: Manages the overall game state (`Game.java`), player turns (`Player.java`), user sessions (`User.java`), and move execution (`Move.java`)[cite: 3].
*   `src/chess/utils/`: Utility classes for reading JSON data (`JsonReader.java`), handling colors (`Colors.java`), and custom data structures (`ChessPair.java`)[cite: 3].
*   `src/chess/exception/`: Custom exception classes for robust error management[cite: 3].

## How to Run

Since this project does not use a build tool like Maven or Gradle, you can compile and run it directly using `javac` and `java`, ensuring you include the provided `.jar` dependency in the classpath.

### From the Command Line (Terminal)

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/stefanitastoica/Chess.git
    cd java-chess
    ```

2.  **Compile the source code:**
    *(Assuming you are in the root directory of the project)*
    *On Windows:*
    ```cmd
    javac -cp "json-simple-1.1.1.jar" src/Main.java src/chess/**/*.java
    ```
    *On Linux/Mac:*
    ```bash
    javac -cp "json-simple-1.1.1.jar" src/Main.java src/chess/**/*.java
    ```

3.  **Run the application:**
    *On Windows:*
    ```cmd
    java -cp "src;json-simple-1.1.1.jar" Main
    ```
    *On Linux/Mac:*
    ```bash
    java -cp "src:json-simple-1.1.1.jar" Main
    ```

*(Alternatively, you can open the project folder in an IDE like IntelliJ IDEA or Eclipse. Ensure you add `json-simple-1.1.1.jar` to your Project Structure / Build Path as a dependency library).*
