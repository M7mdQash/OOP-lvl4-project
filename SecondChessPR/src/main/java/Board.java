/*made by kaskari,
* bissmillah */
import java.util.ArrayList;
public class Board {
    public static ArrayList<Piece> black = new ArrayList<Piece>();
    public static ArrayList<Piece> white = new ArrayList<Piece>();
//list for white and black for less confusion
    static Piece board[][] = new Piece[8][8];
    //two dimensianal array because chess is only 8x8

    static void printBoard() {
        System.out.println("   a   b   c   d   e   f   g   h");

        System.out.println("  ---------------------------------");
        int count = 8;
        for (int i = 0; i < 8; i++) {
            System.out.print(count + " ");
            System.out.print(" ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(" -  ");
                } else {
                    System.out.print(board[i][j] + "  ");
                }
            }
            System.out.print(count);
            count--;
            
            System.out.println("\n  ---------------------------------");
        }
        System.out.println("    a   b   c   d   e   f   g   h");
        System.out.println();
    }

    static void startGame() {

        System.out.println("***" +
                "\nthis is the how to play:" +
                "\nto use pawns: pawn[x file in capial] ex: pawnA" +
                "\nfor pieces: input nameQ/K for queen side or king side ex: bishopQ" +
                "\nto move: input a space then file character+row number, ex: a6" +
                "\nto castle: type castle then K/Q for king or queen side, ex: castleK" +
                "\npawns promote into queens automatically and will be refered to as their original file character" +
                "***");

        //all this code is to set the pieces
        // black
        new Rook(Color.BLACK, "rookQ", 0, 0);
        new Knight(Color.BLACK, "knightQ", 1, 0);
        new Bishop(Color.BLACK, "bishopQ", 2, 0);
        new Queen(Color.BLACK, "queen", 3, 0);
        new King(Color.BLACK, "king", 4, 0);
        new Bishop(Color.BLACK, "bishopK", 5, 0);
        new Knight(Color.BLACK, "knightK", 6, 0);
        new Rook(Color.BLACK, "rookK", 7, 0);

        new Pawn(Color.BLACK, "pawnA", 0, 1);
        new Pawn(Color.BLACK, "pawnB", 1, 1);
        new Pawn(Color.BLACK, "pawnC", 2, 1);
        new Pawn(Color.BLACK, "pawnD", 3, 1);
        new Pawn(Color.BLACK, "pawnE", 4, 1);
        new Pawn(Color.BLACK, "pawnF", 5, 1);
        new Pawn(Color.BLACK, "pawnG", 6, 1);
        new Pawn(Color.BLACK, "pawnH", 7, 1);

        // white
        new Rook(Color.WHITE, "rookQ", 0, 7);
        new Knight(Color.WHITE, "knightQ", 1, 7);
        new Bishop(Color.WHITE, "bishopQ", 2, 7);
        new Queen(Color.WHITE, "queen", 3, 7);
        new King(Color.WHITE, "king", 4, 7);
        new Bishop(Color.WHITE, "bishopK", 5, 7);
        new Knight(Color.WHITE, "knightK", 6, 7);
        new Rook(Color.WHITE, "rookK", 7, 7);

        new Pawn(Color.WHITE, "pawnA", 0, 6);
        new Pawn(Color.WHITE, "pawnB", 1, 6);
        new Pawn(Color.WHITE, "pawnC", 2, 6);
        new Pawn(Color.WHITE, "pawnD", 3, 6);
        new Pawn(Color.WHITE, "pawnE", 4, 6);
        new Pawn(Color.WHITE, "pawnF", 5, 6);
        new Pawn(Color.WHITE, "pawnG", 6, 6);
        new Pawn(Color.WHITE, "pawnH", 7, 6);
    }

    // set piece to provided coordinates
    public static void setPiece(int x, int y, Piece piece) {
        if (piece != null) {
            piece.setX(x);
            piece.setY(y);
        }
        board[y][x] = piece;
    }

    // check spot on board
    public static Piece getPiece(int x, int y) {
        return board[y][x];
    }

    // match String piece from user with Piece on board
    public static Piece getPiece(String piece, Color color) {

        if (color == Color.WHITE) {

            for (int i = 0; i < white.size(); i++) {
                Piece p = white.get(i);
                if (p.matchID(piece)) {
                    return p;
                }
            }
        }

        else if (color == Color.BLACK) {

            for (int i = 0; i < black.size(); i++) {
                Piece p = black.get(i);
                if (p.matchID(piece)) {
                    return p;
                }
            }
        }

        return null;

    }

    public static boolean isPathClear(int x1, int y1, int x2, int y2) {//used in moves see if the selected location is clear
        //as is the path to the desired location is clear
        //knight dont need this becs it jumps over
        int xDistance = x2 - x1;//sub heading from origin
        int yDistance = y2 - y1;
        int xDir = 0;
        int yDir = 0;
        int clock = 0;
        //this will be like a clock to check hroug the lines
        if (xDistance < 0) {
            xDir = -1;
        } else if (xDistance > 0) {
            xDir = 1;
        }

        if (yDistance < 0) {
            yDir = -1;
        } else if (yDistance > 0) {
            yDir = 1;
        }

        if (xDistance != 0) {//this runs the clock wither it be on the X axis or Y axis
            clock = Math.abs(xDistance) - 1;
        } else {
            clock = Math.abs(yDistance) - 1;//subtract 1 from the given number to get a clock
        }
        // change on x and y

        for (int i = 0; i < clock; i++) {
            x1 += xDir;
            y1 += yDir;

            if (getPiece(x1, y1) != null) {
                return false;
            }
        }
        return true;

    }

    static int processMove(String move, Color color) {//whole method is for move legality
        //splits the chosen piece and its destination index
        //it splits at thespapce creating two array indexes
        String[] splitStr = move.split(" ");
        String piece = splitStr[0];
        //this one check if the given piece choice is a castle move
        if (piece.equals("castle")) {
            King king = (King) getPiece("king", color);
            return king.castle(splitStr[1]);
        }

        // piece selected to move
        Piece p = getPiece(piece, color);
        if (p == null) {
            System.out.println("invalid piece, please type in piece to move it.");
            return -1;
        }

        String coordinates = splitStr[1];
        //if the coordinates are less than the length of 2(for y and x)
        if (coordinates.length() != 2) {
            System.out.println("Invalid Tile please try again");
            return -1;
        }

        int file = coordinates.charAt(0) - 'a'; // y
        int rank = 7 - (coordinates.charAt(1) - '1'); // x
        //making sure the coords dont extend the board amount less it is invalid

        if (rank < 0 || rank > 7 || file < 0 || file > 7) {
            System.out.println("Invalid Tile please try again");
            return -1;
        }

        // piece at destination
        //get its coords
        Piece other = getPiece(file, rank);

        return p.move(file, rank, other);//then use the method from Piece class to move

    }

    public static boolean checkForCheck(Color color) {

        Piece king = getPiece("king", color);

        if (color == Color.WHITE) {
            for (int i = 0; i < black.size(); i++) {//will take eeach black picce and see if they capture king
                Piece p = black.get(i);
                if (p.possibleMove(king.getX(), king.getY())) {
                    return true;
                }
            }
        }

        else if (color == Color.BLACK) {
            for (int i = 0; i < white.size(); i++) {
                Piece p = white.get(i);
                if (p.possibleMove(king.getX(), king.getY())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean mate(Color color) {

        if (color == Color.WHITE) {
            for (int i = 0; i < white.size(); i++) {
                Piece p = white.get(i);
                if (p.canMove()) {
                    return false;
                }
            }
        } else if (color == Color.BLACK) {
            for (int i = 0; i < black.size(); i++) {
                Piece p = black.get(i);
                if (p.canMove()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean staleMate(Color color) {

        // insufficient material stalemate
        Piece knightK = getPiece("knightK", color);
        Piece knightQ = getPiece("knightQ", color);
        Piece bishopK = getPiece("bishopK", color);
        Piece bishopQ = getPiece("bishopQ", color);

        if (white.size() == 2 && black.size() == 2) {
            if (white.contains(bishopK) || white.contains(bishopQ) || white.contains(knightK)
                    || white.contains(knightQ)) {
                return true;
            }
            if (black.contains(bishopK) || black.contains(bishopQ) || black.contains(knightK)
                    || white.contains(knightQ)) {
                return true;
            }

        }
        if (white.size() == 1 && white.get(0) instanceof King && black.size() == 1 && black.get(0) instanceof King) {
            return true;
        }//if king and king left its stale

        // no legal moves stalemate
        if (mate(color) == true && checkForCheck(color) == false) {//king not inn check plus mate its stale
            return true;
        }

        return false;

    }

}

