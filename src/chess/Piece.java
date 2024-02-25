package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;


public abstract class Piece extends Chess {
    protected ReturnPiece.PieceFile x, newX;
    protected int y, newY;
    ReturnPiece.PieceType pieceName;
    public static ReturnPlay demoBoard;

    // keep track of the pawns that have moved two spaces for en passant
    public static ArrayList<Pawn> movedPawns = new ArrayList<>();

    public Piece (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        this.x = x;
        this.y = y;
        this.pieceName = pieceName;
    }

    public ReturnPiece.PieceFile getFile() {
        return x;
    }

    public int getRank() {
        return y;
    }

    public ReturnPiece.PieceType getPieceName() { return pieceName; }

    // determines whos turn it is to prevent illegal moves
    public boolean myTurn() {
        Character c = getColor(getPieceName());
        if (playColor.name().charAt(0) == Character.toLowerCase(c)) {
            return true;
        }
        return false;
    }

    public char getColor(ReturnPiece.PieceType type) {
        return type.name().charAt(0);
    }

    public abstract boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board);

    public void makeMove(PieceFile startingFile, int startingRank, PieceFile newX, int newY, ReturnPlay board) {
        for (int i = 0; i < board.piecesOnBoard.size(); i++) {
			if (board.piecesOnBoard.get(i).pieceFile.equals(startingFile) && 
				board.piecesOnBoard.get(i).pieceRank == startingRank) {

				    board.piecesOnBoard.get(i).pieceFile = newX;
				    board.piecesOnBoard.get(i).pieceRank = newY;

			}
		}
    }

    public void makeCastlingMove(Piece kingPiece, PieceFile rookStartingFile, int rookStartingRank, ReturnPlay board) {
            for (int i = 0; i < board.piecesOnBoard.size(); i++) {
                if (board.piecesOnBoard.get(i).pieceFile.equals(rookStartingFile) && 
                    board.piecesOnBoard.get(i).pieceRank == rookStartingRank) {

                        if (globalIsRightRook) {
                            board.piecesOnBoard.get(i).pieceFile = PieceFile.f;
                            board.piecesOnBoard.get(i).pieceRank = rookStartingRank;
                        }
                        else {
                            board.piecesOnBoard.get(i).pieceFile = PieceFile.d;
                            board.piecesOnBoard.get(i).pieceRank = rookStartingRank;
                        }
                }
            }
    }

    // used for pawn, also for rook and king to determine if castling is legal
    public boolean isFirstMove(ReturnPiece.PieceType type, Player color) {
        ReturnPiece.PieceFile file = getFile();
        int rank = getRank();
        if (color == Player.white) {
            if (type == ReturnPiece.PieceType.WP) {
                if (getRank() == 2) return true;
            }
            else if (type == ReturnPiece.PieceType.WR || type == ReturnPiece.PieceType.WK) {
                if (type == ReturnPiece.PieceType.WR) {
                    if (file == ReturnPiece.PieceFile.a && rank == 1) { // left rook
                        if (leftWRookFirstMove) {
                            leftWRookFirstMove = false;
                            return true;
                        }
                    }
                    else if (file == ReturnPiece.PieceFile.h && rank == 1) { // right rook
                        if (rightWRookFirstMove) {
                            rightWRookFirstMove = false;
                            return true;
                        }
                    }
                }
                else if (type == ReturnPiece.PieceType.WK) {
                    if (file == PieceFile.e && rank == 1) {    // king
                        if (wKingFirstMove) {
                            wKingFirstMove = false;
                            return true;
                        }
                    }
                }
                /*if (file == ReturnPiece.PieceFile.a && rank == 1) { // left rook
                    if (leftWRookFirstMove) {
                        leftWRookFirstMove = false;
                        return true;
                    }
                }
                else if (file == ReturnPiece.PieceFile.h && rank == 1) { // right rook
                    if (rightWRookFirstMove) {
                        rightWRookFirstMove = false;
                        return true;
                    }
                }
                else if (file == ReturnPiece.PieceFile.e && rank == 1) {    // king
                    if (wKingFirstMove) {
                        wKingFirstMove = false;
                        return true;
                    }
                }*/
            }
        }
        else if (color == Player.black) {
            if (type == PieceType.WP) {
                if (getRank() == 7) return true;
            }
            else if (type == ReturnPiece.PieceType.WR || type == ReturnPiece.PieceType.WK || type == ReturnPiece.PieceType.BR || type == ReturnPiece.PieceType.BK) {
                if (file == ReturnPiece.PieceFile.a && rank == 8) { // left rook
                    if (leftBRookFirstMove) {
                        leftBRookFirstMove = false;
                        return true;
                    }
                }
                else if (file == ReturnPiece.PieceFile.h && rank == 8) { // right rook
                    if (rightBRookFirstMove) {
                        rightBRookFirstMove = false;
                        return true;
                    }
                }
                else if (file == ReturnPiece.PieceFile.e && rank == 8) {    // king
                    if (bKingFirstMove) {
                        bKingFirstMove = false;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // prevents capturing pieces of the same color. the color param is the color you do NOT want to capture
    // NOTE this method is case sensitive, use the capital letter for the character param (B and W)
    public boolean isSameColor(Piece startingPiece, Piece captPiece, boolean forIllegalCheck, ReturnPlay board) {
        //System.out.println(captPiece.getColor(captPiece.getPieceName()));
        //if (col != playColor) return false;
        char startingColor = startingPiece.getColor(startingPiece.getPieceName());
        char capturedColor = captPiece.getColor(captPiece.getPieceName());
        if (startingColor == capturedColor) { // cannot capture same color
            return false;
        }
        else {
            if (!forIllegalCheck) {
                ReturnPiece rp = new ReturnPiece();
                rp.pieceFile = captPiece.getFile();
                rp.pieceRank = captPiece.getRank();
                rp.pieceType = captPiece.getPieceName();
                board.piecesOnBoard.remove(rp);
            } 

            return true;
        }
    }

    public boolean validEnPassant(Piece capturingPiece, Piece capturedPiece) {
        PieceFile capturedPieceFile = capturedPiece.getFile();
        int capturedPieceRank = capturedPiece.getRank();
        System.out.println(movedPawns.size() + "\n" + capturedPiece.getPieceName() + ": " + capturedPieceFile +""+capturedPieceRank);
        for (int i = 0; i < movedPawns.size(); i++) {
            Pawn p = movedPawns.get(i);
            System.out.println(p.getPieceName() + ": " + p.getFile() + "" + p.getRank());
            if (p.getPieceName() == capturedPiece.getPieceName() && p.getFile() == capturedPieceFile && p.getRank() == capturedPieceRank) {
                System.out.println(p.getPieceName() + " : test2.");
                return true;
            }
        }
        return false;
    }

    // determines if there is a piece at the given file and rank location. This can be used for captures, or preventing illegal capture.
    public static boolean pieceAhead(PieceFile f, int movedRank, ReturnPlay board) {
        if (playColor == Player.white) {
            if (identifyPieceType(f, movedRank, board) != null) {
                return true;
            }
            return false;
        }
        else if (playColor == Player.black) {
            if (identifyPieceType(f, movedRank, board) != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    // needed for the isIllegalCheck method. This will locate the current position of the king so it can be determined the
    // move jeopardizes the king.
    public Piece findKing(Player color, ReturnPlay board) {
        for (PieceFile f : PieceFile.values()) {
            for (int i = 1; i <= 8; i++) {
                Piece p = identifyPieceType(f, i, board);
                if (p != null) {
                    if (color == Player.white && p.getPieceName().equals(ReturnPiece.PieceType.WK)) {
                        return p;
                    }
                    else if (color == Player.black && p.getPieceName().equals(ReturnPiece.PieceType.BK)) {
                        return p;
                    }
                }
            }
        }
        
        return null;
    }

    public ReturnPlay cloneBoard(ReturnPlay board) {
        ReturnPlay clone = new ReturnPlay();
        clone.piecesOnBoard = new ArrayList<>();

        // Copy each piece individually to create a deep copy
        for (ReturnPiece piece : board.piecesOnBoard) {
            ReturnPiece pieceClone = new ReturnPiece();
            pieceClone.pieceFile = piece.pieceFile;
            pieceClone.pieceRank = piece.pieceRank;
            pieceClone.pieceType = piece.pieceType;
            clone.piecesOnBoard.add(pieceClone);
        }

        return clone;
    }

    public Player switchTurn(Player curr) {
        if (curr == Player.white) {
            return Player.black;
        }
        else if (curr == Player.black) {
            return Player.white;
        }
        return null;
    }

    // determines if the player's king is in check, this will take into account every piece at its position on the board
    // relative to the king's position, determining if the current status is in check. 
    public boolean isIllegalCheck(Player currentPlayer, PieceFile movedFile, int movedRank, Piece king, ReturnPlay board) {
        boolean checked = false;
        int kingRank = king.getRank();
        PieceFile kingFile = king.getFile();
        PieceFile startingFile = getFile();
        int startingRank = getRank();
        if (startingFile == kingFile && startingRank == kingRank) {
            kingFile = movedFile;
            kingRank = movedRank;
        }
        
        if (board == null) {
            board = cloneBoard(startingBoard);
        }
        ReturnPlay tempBoard = cloneBoard(board);
        
        makeMove(startingFile, startingRank, movedFile, movedRank, board); // temporarily move the piece
        
        // now we need to see if theres any potential openings for the king to be attacked,
        // if there are, return true and return an illegal_move message in the class this is being called in.
        for (PieceFile f : PieceFile.values()) {
            for (int i = 1; i <= 8; i++) {
                board = cloneBoard(tempBoard);
                Piece p = identifyPieceType(f, i, board);
                if (p != null && p.getColor(p.getPieceName()) != king.getColor(king.getPieceName())) {
                    if (p.isValid(p.getFile(), p.getRank(), kingFile, kingRank, switchTurn(currentPlayer), true, board)) {
                        checked = true;
                        break;
                    }
                    else {
                        checked = false;                    
                    }
                }
            }
            if(checked) break;
        }
        board = cloneBoard(tempBoard);
        
        makeMove(movedFile, movedRank, startingFile, startingRank, board); // move the piece back in original position
        if (checked) {
            return true;
        }
        else if (!checked) {
            return false;
        }
        return false;
    }

    public boolean isCheck(Player currentPlayer, PieceFile movedFile, int movedRank) {
        Piece king = null;
        ReturnPlay tempBoard = cloneBoard(startingBoard);
        if (playColor == Player.white) {
            king =  findKing(Player.black, tempBoard);
            currentPlayer = Player.black;
        }
        else if (playColor == Player.black) {
            king = findKing(Player.white, tempBoard);
            currentPlayer = Player.white;
        }
        //System.out.println(king.getPieceName() + ": " + king.getFile() + "" + king.getRank());
        PieceFile kingFile = king.getFile();
        int kingRank = king.getRank();
        //Piece p = identifyPieceType(movedFile, movedRank);
        //System.out.println(getPieceName() + ": " + movedFile +""+ movedRank + " - " + playColor);
        for (PieceFile f : PieceFile.values()) {
            for (int i = 1; i <= 8; i++) {
                tempBoard = cloneBoard(startingBoard);
                Piece p = identifyPieceType(f,i, tempBoard);
                if (p != null && p.isValid(p.getFile(), p.getRank(), kingFile, kingRank, playColor, true, tempBoard)) {
                    //System.out.println("test333");
                    return true;
                }
            }
        }
        tempBoard = cloneBoard(startingBoard);
        return false;
    }
  

    public boolean isCheckmate(Player currentPlayer, ReturnPlay board) {
        board = cloneBoard(startingBoard);
        for (PieceFile f : PieceFile.values()) {
            for (int i = 1; i <= 8; i++) {
                
                Piece p = identifyPieceType(f, i, board);
                
                for (PieceFile fMove : PieceFile.values()) {
                    for (int j = 1; j <= 8; j++) {
                        board = cloneBoard(startingBoard);
                        if (p != null) {
                            char c = p.getColor(p.getPieceName());
                            if (Character.toLowerCase(c) == currentPlayer.name().charAt(0)) {
                                if (p.isValid(p.getFile(), p.getRank(), fMove, j, currentPlayer, false, board)) {
                                    System.out.println(p.getPieceName() + ": " + f +""+i+ " -> " + fMove+""+j + " is valid.");
                                    if (!isIllegalCheck(currentPlayer, fMove, j, findKing(currentPlayer, board), board)) {
                                        board = cloneBoard(startingBoard);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        board.piecesOnBoard = startingBoard.piecesOnBoard;
        return true;
    }

    // returns 1 if moving forward or right, -1 if moving backward or left, return 0 if the rank is the same
    // forward means going up along the rank (1 to 8), (a to g), backward is going down along the rank (8 to 1) (g to a)
    // type parameter: put 0 if you want the direction along the file, 1 for the direction along the rank
    public int direction(int type, PieceFile movedFile, int movedRank) {
        /*if (rank > movedRank) {
            return -1;
        } else if (rank < movedRank) {
            return 1;
        }*/
        if (type == 1) {
            if (getRank() > movedRank) {
                return -1;
            } else if (getRank() < movedRank) {
                return 1;
            }
        }
        if (type == 0) {
            int leftOrRight = Integer.compare(getFile().ordinal(), movedFile.ordinal());
            if (leftOrRight < 0) {
                return 1;
            } else if (leftOrRight > 0) {
                return -1;
            } 
            /*for (PieceFile file : EnumSet.range(getFile(), movedFile)) {
                if (getNext(file).equals(movedFile)) {
                    return 1;
                }
            }
            for (PieceFile file : EnumSet.range(getFile(), movedFile)) {
                if(getPrev(file).equals(movedFile)) {
                    return -1;
                }
            }*/
        }
        return 0;
    }

    public static int[] getCoord(String move) {
        int[] dissectedMove = new int[5]; // Extend array to hold a promotion piece
        
        dissectedMove[0] = move.charAt(0); // Convert from char to 0-based column index
        int row;
        if (isWhite = true) {
            row = (Character.getNumericValue(move.charAt(1)));//  2
        } else {
            row = (8 - Character.getNumericValue(move.charAt(1)));
        }
        dissectedMove[1] = row;
    
        dissectedMove[2] = move.charAt(3); // Convert from char to 0-based column index
        int row2;
        if (isWhite = true) {
            row2 = Character.getNumericValue(move.charAt(4));// 3 
        } else {
            row2 = 8 - Character.getNumericValue(move.charAt(4));
        }
        dissectedMove[3] = row2;
        
        // Handle pawn promotion
        if (move.length() == 6) { // Check if promotion is specified
            char promotion = move.charAt(5);
            switch (promotion) {
                case 'Q': dissectedMove[4] = 1; break; // Queen
                case 'R': dissectedMove[4] = 2; break; // Rook
                case 'B': dissectedMove[4] = 3; break; // Bishop
                case 'N': dissectedMove[4] = 4; break; // Knight
                default: dissectedMove[4] = 0; // No promotion or unrecognized
            }
        } else {
            dissectedMove[4] = 0; // No promotion
        }
    
        return dissectedMove;
    }

}



