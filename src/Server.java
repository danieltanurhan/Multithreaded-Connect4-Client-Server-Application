import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(59001)) {
            var pool = Executors.newFixedThreadPool(200);
            Game game = new Game();
            while (true) {
                pool.execute(game.new Player(listener.accept()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Game {
    private final Set<String> connectedNames = new HashSet<>();
    private final Set<PrintWriter> writers = new HashSet<>();
    Player currentPlayer;
    Boolean inProgress = false;
    private Boolean soloPlay = false;
    final private int WIDTH = 6;
    private final Integer[][] board = new Integer[WIDTH][WIDTH];
    public Game() {

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public synchronized int move(int loc, Player player){
        int row = 0;

        if(player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        } else if (!soloPlay && player.opponent == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        }else if (board[row+1][loc] != null) {
            throw new IllegalStateException("Grid filled up");
        }

        for(int i = WIDTH - 1; i > 0; i--) {
            if(board[i][loc] == null) {
                board[i][loc] = currentPlayer.playerNum;
                return (loc+(i*WIDTH));
            }
        }
        return -1;
    }

    public int soloMove(int loc){
        int row = 0;

        if (board[row+1][loc] != null) {
            throw new IllegalStateException("Grid filled up");
        }

        for(int i = 5; i > 0; i--) {
            if(board[i][loc] == null) {
                board[i][loc] = 2;

                return (loc+(i*6));
            }
        }

        return -1;
    }

    public int soloWinner() {
        if(verticalCheck(currentPlayer.playerNum, 2)
                || horizontalCheck(currentPlayer.playerNum, 2)
                || diagonalDownRight(currentPlayer.playerNum, 2)
                || diagonalUpRight(currentPlayer.playerNum, 2)) {
            return 1;
        } else if (verticalCheck(2, currentPlayer.playerNum)
                || horizontalCheck(2, currentPlayer.playerNum)
                || diagonalDownRight(2, currentPlayer.playerNum)
                || diagonalUpRight(2, currentPlayer.playerNum)) {
            return 2;
        }

        return -1;
    }

    public synchronized boolean hasWinner(){
        if (currentPlayer.opponent != null) {
            return verticalCheck(currentPlayer.playerNum, currentPlayer.opponent.playerNum)
                    || horizontalCheck(currentPlayer.playerNum, currentPlayer.opponent.playerNum)
                    || diagonalDownRight(currentPlayer.playerNum, currentPlayer.opponent.playerNum)
                    || diagonalUpRight(currentPlayer.playerNum, currentPlayer.opponent.playerNum);
        }

        return false;

    }

    public boolean verticalCheck(int currentPlayerNum, int oppNum) {
        int fourCount = 0;
        for(int col = 0; col < 6; col++) {
            for(int i = 1; i < 6; i++) {
                if(board[i][col] == null || board[i][col] == oppNum) {
                    fourCount = 0;
                } else if (board[i][col] == currentPlayerNum) {
                    fourCount++;
                }

                if(fourCount == 4){
                    return true;
                }
            }
            fourCount = 0;
        }
        return false;
    }

    public boolean horizontalCheck(int currentPlayerNum, int oppNum) {
        int fourCount = 0;
        for(int row = 1; row < 6; row++) {
            for(int i = 0; i < 6; i++) {
                if (board[row][i] == null || board[row][i] == oppNum) {
                    fourCount = 0;
                } else if(board[row][i] == currentPlayerNum) {
                    fourCount++;
                }

                if(fourCount == 4){
                    return true;
                }
            }
            fourCount = 0;
        }
        return false;
    }

    public boolean diagonalDownRight(int currentPlayerNum, int oppNum){
        int row = 2;
        int col = 0;
        int fourCount = 0;

        for(; row < WIDTH; row++, col++){
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;
        for(row = 1, col = 0; row < WIDTH; row++, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;
        for(row = 1, col = 1; col < WIDTH; row++, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;
        for(row = 1, col = 2; col < WIDTH; row++, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        return false;
    }

    public boolean diagonalUpRight(int currentPlayerNum, int oppNum) {
        int row = 4;
        int col = 0;
        int fourCount = 0;

        for(; row > 0; row--, col++){
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;

        for(row = 5, col = 0; row > 0; row--, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;
        for(row = 5, col = 1; col < WIDTH; row--, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if(board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if(fourCount == 4){
                return true;
            }
        }
        fourCount = 0;
        for(row = 5, col = 2; col < WIDTH; row--, col++) {
            if (board[row][col] == null || board[row][col] == oppNum) {
                fourCount = 0;
            } else if (board[row][col] == currentPlayerNum) {
                fourCount++;
            }
            if (fourCount == 4) {
                return true;
            }
        }
        return false;
    }

    public boolean boardFilledUp() {
        for(int i = 1; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if(board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetBoard() {
        for(int i = 1; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                board[i][j] = null;
            }
        }
    }

    class Player implements Runnable {
        Socket socket;
        Scanner input;
        PrintWriter output;
        String playerName;
        Player opponent;
        Integer playerNum;


        public Player(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(output != null) {
                    writers.remove(output);
                }
                if(playerName != null) {
                    connectedNames.remove(playerName);
                    if(!inProgress || inProgress == null) {
                        for (PrintWriter writer : writers) {
                            writer.println("MESSAGE " + playerName + " has left");
                        }
                    } else {

                        if(soloPlay) {
                            inProgress = false;
                            playerNum = null;
                            currentPlayer = null;
                            resetBoard();
                            for (PrintWriter writer : writers) {
                                writer.println("RESET");
                                writer.println("MESSAGE Game has ended because " + playerName + " has left");
                            }
                        } else if(Objects.equals(playerName, currentPlayer.playerName) || Objects.equals(playerName, currentPlayer.opponent.playerName)) {
                            for (PrintWriter writer : writers) {
                                writer.println("RESET");
                                writer.println("MESSAGE Game has ended because " + playerName + " has left");
                            }
                            if(opponent != null) {
                                opponent.opponent = null;
                                opponent.playerNum = null;
                            }
                            inProgress = false;
                            currentPlayer = null;
                            resetBoard();
                        }
                    }
                }

                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }

        //Setting up inout output streams and filling up player data
        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);

            while(true){
                output.println("SUBMIT-NAME");
                playerName = input.nextLine();
                if(playerName == null) {
                    return;
                }
                synchronized (connectedNames) {
                    if(!playerName.isBlank() && !connectedNames.contains(playerName)) {
                        connectedNames.add(playerName);
                        System.out.println("Added name: " + playerName);
                        break;
                    }
                }
            }

            output.println("NAME-ACCEPTED" + playerName);
            for(PrintWriter writer : writers) {
                writer.println("MESSAGE " + playerName + " has joined");
            }
            writers.add(output);
        }

        //processCommands listens to the input to get instructions from Client
        private void processCommands(){
            while(input.hasNextLine()) {
                String command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    processMoveCommand(Integer.parseInt(command.substring(4)));
                }
                else if (command.startsWith("MESSAGE/multiplayerPlay")) {
                    if(soloPlay && inProgress || inProgress && currentPlayer.opponent != null) {
                        output.println("MESSAGE Game in progress!");
                    } else if(getCurrentPlayer() == null) {
                        this.playerNum = 1;
                        setCurrentPlayer(this);
                        resetBoard();
                        inProgress = true;
                        for(PrintWriter writer : writers) {
                            writer.println("RESET");
                            writer.println("MESSAGE " + playerName + " has connected as Player " + playerNum);
                        }
                    } else if (!playerName.equals(currentPlayer.playerName)) {
                        this.playerNum = 2;
                        opponent = currentPlayer;
                        opponent.opponent = this;


                        for(PrintWriter writer : writers) {
                            writer.println("MESSAGE " + playerName + " has connected as Player " + playerNum);
                            writer.println("MESSAGE Start!");
                        }
                        opponent.output.println("MESSAGE Your move");
                    }

                }
                else if (command.startsWith("MESSAGE/leave")) {

                    if(inProgress && !soloPlay) {
                        if(opponent == null && playerName.equals(currentPlayer.playerName)) {
                            playerNum = null;
                            inProgress = false;
                            resetBoard();
                            currentPlayer = null;
                            for(PrintWriter writer : writers) {
                                writer.println("RESET");
                                writer.println("MESSAGE " + playerName + " has left the game, the game has ended.");
                            }
                        } else if(!soloPlay
                                && playerName.equals(currentPlayer.playerName)
                                || playerName.equals(currentPlayer.opponent.playerName)) {
                            playerNum = null;
                            opponent.playerNum = null;
                            opponent.opponent = null;
                            inProgress = false;
                            resetBoard();
                            currentPlayer = null;
                            opponent = null;
                            for(PrintWriter writer : writers) {
                                writer.println("RESET");
                                writer.println("MESSAGE " + playerName + " has left the game, the game has ended.");
                            }
                        }

                    } else if (inProgress && soloPlay) {
                        if(currentPlayer != null && Objects.equals(playerName, currentPlayer.playerName)) {
                            playerNum = null;
                            inProgress = false;
                            soloPlay = false;
                            resetBoard();
                            currentPlayer = null;
                            for(PrintWriter writer : writers) {
                                writer.println("RESET");
                                writer.println("MESSAGE " + playerName + " has left the game, the game has ended.");
                            }
                        }
                    }

                }
                else if (command.startsWith("MESSAGE/soloPlay")) {
                    if(inProgress) {
                        output.println("MESSAGE Game in progress!");
                    } else if(getCurrentPlayer() == null) {
                        this.playerNum = 1;
                        setCurrentPlayer(this);
                        resetBoard();
                        soloPlay = true;
                        inProgress = true;
                        for(PrintWriter writer : writers) {
                            writer.println("RESET");
                            writer.println("MESSAGE " + playerName + " has started a solo game ");
                        }
                    }
                }
                else if (command.startsWith("MESSAGE/help")){
                    output.println("MESSAGE /multiplayerPlay, /soloPlay, /reset, /quit, /help");
                }
                else if (command.startsWith("MESSAGE")){
                    for(PrintWriter writer : writers) {
                        writer.println("MESSAGE " + playerName + ": " + command.substring(7));
                    }
                }
            }
        }

        //Function to handle player move
        private synchronized void processMoveCommand(int loc) {
            Random rnd = new Random();
            int brainyAIBotMove;
            int tileLoc;
            try {
                if(playerNum != null) {
                    tileLoc = move(loc, this);
                    if(tileLoc > 5 && tileLoc < 36) { //tileLoc != -1 &&
                        for(PrintWriter writer : writers) {
                            writer.println("VALID_MOVE" + playerNum + "" + tileLoc);
                        }
                        if(soloPlay && soloWinner() != 1) {
                            Thread.sleep(250);
                            do {
                                brainyAIBotMove = rnd.nextInt(6);
                                if(boardFilledUp()){
                                    break;
                                }
                            }
                            while(board[1][brainyAIBotMove] != null);
                            tileLoc = soloMove(brainyAIBotMove);

                            for(PrintWriter writer : writers) {
                                writer.println("VALID_MOVE" + 2 + "" + tileLoc);
                            }
                        }

                        if(!soloPlay && hasWinner()) {
                            output.println("WINNER");
                            opponent.output.println("LOSER");
                            playerNum = null;
                            opponent.playerNum = null;
                            opponent.opponent = null;
                            currentPlayer = null;
                            inProgress = false;
                            for(PrintWriter writer : writers) {
                                writer.println("MESSAGE " + playerName + " is the Winner, " + opponent.playerName + " is the loser of the game");
                            }
                            opponent = null;
                        } else if (!soloPlay && boardFilledUp()){
                            output.println("DRAW");
                            opponent.output.println("DRAW");
                            playerNum = null;
                            opponent.playerNum = null;
                            opponent = null;
                            currentPlayer = null;
                            inProgress = false;
                            for(PrintWriter writer : writers) {
                                writer.println("MESSAGE Game ended in a draw");
                            }
                        } else if (!soloPlay && currentPlayer.opponent != null){
                            setCurrentPlayer(currentPlayer.opponent);
                        } else if (soloWinner() == 1) {
                            output.println("WINNER");
                            playerNum = null;
                            currentPlayer = null;
                            inProgress = false;
                            soloPlay = false;

                            for(PrintWriter writer : writers) {
                                writer.println("MESSAGE " + playerName + " is the Winner");
                            }

                        } else if (soloWinner() == 2) {
                            output.println("LOSER");
                            playerNum = null;
                            currentPlayer = null;
                            inProgress = false;
                            soloPlay = false;

                            for(PrintWriter writer : writers) {
                                writer.println("MESSAGE " + playerName + " is the Loser, ABBOT Wins");
                            }
                        } else if (soloPlay && boardFilledUp()) {
                            output.println("DRAW");
                            playerNum = null;
                            currentPlayer = null;
                            inProgress = false;
                            soloPlay = false;
                            for(PrintWriter writer : writers) {
                                writer.println("MESSAGE Game ended in a draw");
                            }
                        }
                    }
                }

            } catch (IllegalStateException e) {
                output.println("MESSAGE " + e.getMessage());
            } catch ( InterruptedException ie) {
                System.out.println(ie.getMessage());
            }

        }
    }
}

