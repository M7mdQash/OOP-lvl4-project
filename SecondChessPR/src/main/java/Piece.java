public abstract class Piece {
    private final Color color;

    private final String ID;

    private int x, y;

    public boolean isFirstMove;
//a piece has all the above
    public Piece(Color color, String ID, int startX, int startY) {//basically constructor
        this.color = color;
        this.ID = ID;
        this.x = startX;
        this.y = startY;

        if (this.getColor() == Color.WHITE) {//constructs on the board
            Board.white.add(this);
        } else if (this.getColor() == Color.BLACK) {
            Board.black.add(this);
        }
        Board.setPiece(x, y, this);
    }

    //we use this for when user inputs the piece's name in game
    public String getID() {
        return this.ID;
    }

    public boolean matchID(String ID) {
        return this.ID.equals(ID);
    }

    public Color getColor() {
        return this.color;
    }

    public boolean sameColor(Piece otherPiece) {//we use this in the pieces classes to scan if the destination
        //is of the same colors so it doenst capture its own piece
        if (otherPiece == null) {
            return false;
        }
        return (this.color == otherPiece.getColor());
    }

    public int getX() {
        return this.x;
    }

    void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    void setY(int newY) {
        this.y = newY;
    }

    public abstract boolean possibleMove(int x, int y);//used in piece classes for finding possible moves

    public int move(int x, int y, Piece selectedPiece) {
        if (this.possibleMove(x, y) != true) {
            return -1;
        }

        Color color = this.getColor();
        int originX = this.getX();
        int originY = this.getY();

        if (this.getColor() == Color.WHITE) {//removes/captures
            Board.black.remove(selectedPiece);
        } else {
            Board.white.remove(selectedPiece);
        }

        Board.setPiece(originX, originY, null);
        Board.setPiece(x, y, this);

        boolean isFirstMoveOG = this.isFirstMove;
        this.isFirstMove = false;

        if (Board.checkForCheck(color) == true) {//this is the code that moves the piece to desired location n
            if (selectedPiece != null) {
                if (this.getColor() == Color.WHITE) {
                    Board.black.add(selectedPiece);
                } else {
                    Board.white.add(selectedPiece);
                }
            }
            Board.setPiece(originX, originY, this);//sets piece from this
            Board.setPiece(x, y, selectedPiece);//into this
            this.isFirstMove = isFirstMoveOG;

            return -1;
        }
        //this is the pawn promotion code
        if (this instanceof Pawn) {
            char file = this.getID().charAt(4);
            if (this.getColor() == Color.WHITE && y == 0) {
                Board.setPiece(x, y, null);
                Board.white.remove(this);
                Queen promQueen = new Queen(Color.WHITE, "queen" + file, x, y);
                System.out.println("Pawn promoted!");
            } else if (this.getColor() == Color.BLACK && y == 7) {
                Board.setPiece(x, y, null);
                Board.black.remove(this);
                Queen promQueen = new Queen(Color.BLACK, "queen" + file, x, y);
                System.out.println("Pawn promoted!");
            }
        }

        return 0;
    }

    public boolean testMove(int x, int y) {//this code is used in every piece class to
        // test if the piece can move to the desired location
        int originX = this.getX();
        int originY = this.getY();
        Piece other;
        boolean isFirst = this.isFirstMove;

        if (x >= 0 && y >= 0 && x <= 7 && y <= 7) {
            other = Board.getPiece(x, y);
            if (this.move(x, y, other) == 0) {
                // captured piece set to original position
                Board.setPiece(x, y, other);
                // selected piece set to original position
                Board.setPiece(originX, originY, this);
                isFirstMove = isFirst;
                if (other != null) {
                    if (other.getColor() == Color.WHITE) {
                        Board.white.add(other);
                    } else
                        Board.black.add(other);
                }
                return true;
            }
        }
        return false;
    }

    public String nullToString() {
        return "   ";
    }
//this will be used to display pieces
    public abstract String toString();
//this method will be used to tell if a piece can move to its desired location
    public abstract boolean canMove();
}
