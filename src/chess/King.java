package chess;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class King extends Piece {

    public King(PieceFile x, int y, PieceType pieceName) {
        super(x, y, pieceName);
    }

    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
        int dirRank = direction(1, movedFile, movedRank);
        int dirFile = direction(0, movedFile, movedRank);
        
        
        // moving forward and backward
        if (file.equals(movedFile) && rank != movedRank) {
            boolean validVertical = false;
            // king can only move 1 space in any direction, determine if the move is legal first
            if (dirRank == 1) {
                if (movedRank - 1 == rank) { validVertical = true; }
            }
            else if (dirRank == -1) {
                if (rank - 1 == movedRank) { validVertical = true; }
            }
            // case for capture 
            if (validVertical) {
                if (pieceAhead(file, movedRank, board)) {
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                }
                else {  // no piece to capture
                    return true;
                }
            } else {
                return false;
            }
        }

        // CASTLING 
        if (movedFile == PieceFile.g || movedFile == PieceFile.c) {
        King kingPiece = new King(startingFile, startingRank, getPieceName());
        Rook rookPiece = null;
        boolean isRightRook = false;
        if (movedFile == PieceFile.g) {
            System.out.println("line 52");
            isRightRook = true;
            if (pieceAhead(getNext(movedFile), startingRank, board)) {
                System.out.println("line 55");
                if (identifyPieceType(getNext(movedFile), startingRank, board).getPieceName() == PieceType.WR || identifyPieceType(getNext(movedFile), startingRank, board).getPieceName() == PieceType.BR) {
                    System.out.println("line 57");
                    rookPiece = new Rook(getNext(movedFile), startingRank, identifyPieceType(getNext(movedFile), startingRank, board).getPieceName());
                    System.out.println(rookPiece.getPieceName() + ": " + rookPiece.getFile() +""+rookPiece.getRank());
                }
            }
        }
        else if (movedFile == PieceFile.c) {
            isRightRook = false;
            if (pieceAhead(getPrev(getPrev(movedFile)), startingRank, board)) {
                if (identifyPieceType(getPrev(getPrev((movedFile))), startingRank, board).getPieceName() == PieceType.WR || identifyPieceType(getPrev(getPrev((movedFile))), startingRank, board).getPieceName() == PieceType.BR) {
                    rookPiece = new Rook(getPrev(getPrev((movedFile))), startingRank, identifyPieceType(getPrev(getPrev((movedFile))), startingRank, board).getPieceName());
                }
            }
        }
        if (rookPiece != null) {
            System.out.println("line 71");
            System.out.println(kingPiece.getPieceName() + ": " + kingPiece.getFile() +""+kingPiece.getRank());
            if (validCastling(kingPiece, rookPiece, startingFile, movedFile, isRightRook, board, color)) {
                System.out.println("line 73");
                validCastlingMove = true;
                if (isRightRook) globalIsRightRook = true;
                else globalIsRightRook = false;
                return true;
            }
        }
    }

        // moving horizontal 
        if (!file.equals(movedFile) && rank == movedRank) {
            System.out.println("line 85");
            boolean validHorizontal = false;
            
            // determine legality of move
            if (dirFile == 1) {
                System.out.println("line 90");
                if (getNext(file).equals(movedFile)) { 
                    System.out.println(file + " : " + getNext(file) + " : " + movedFile);
                    System.out.println("line 92");
                    validHorizontal = true; 
                }
            }
            else if (dirFile == -1) {
                if (getNext(movedFile) == file) { validHorizontal = true; }
            }
            // case for capture
            System.out.println("ValidHorizontal = " + validHorizontal);
            if (validHorizontal) {
                if (pieceAhead(movedFile, rank, board)) {
                    System.out.println("line104");
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        // moving diagonally
        if (!file.equals(movedFile) && rank != movedRank) {
            // determine legality of move
            boolean validDiagonal = false;
            if (dirRank == 1 && dirFile == 1) { // up right
                if (getNext(file) == movedFile && rank + 1 == movedRank) {
                    validDiagonal = true;
                    
                }
            }
            else if (dirRank == 1 && dirFile == -1) {   // up left
                if (getNext(movedFile) == file && rank + 1 == movedRank) {
                    validDiagonal = true;
                   
                }
            }
            else if (dirRank == -1 && dirFile == 1) {   // down right
                if (getNext(file) == movedFile && movedRank + 1 == rank) {
                    validDiagonal = true;
                    
                }
            }
            else if (dirRank == -1 && dirFile == -1) {  // down left
                if (getNext(movedFile) == file && movedRank + 1 == rank) {
                    validDiagonal = true;
                    
                }
            }

            // case for capture
            if (validDiagonal) {
                if (pieceAhead(movedFile, movedRank, board)) {
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                } else {
                    return true;
                }
            }
            else {
                return false;
            }
        }
        return false;
    }
    public boolean validCastling(Piece kingPiece, Piece rookPiece, PieceFile startingFile, PieceFile movedFile, boolean isRightRook, ReturnPlay board, Player color) {
        // first case: neither the king nor the rook has moved
        
        System.out.println("line 149");
        if (rookPiece.isFirstMove(PieceType.WR, color) && kingPiece.isFirstMove(PieceType.WK, color)) {

            // second case: there are no pieces between the king and the rook
            if (isRightRook) {
                if (pieceAhead(getNext(kingPiece.getFile()), kingPiece.getRank(), board)
                    || pieceAhead(getNext(getNext((kingPiece.getFile()))), kingPiece.getRank(), board)) {
                        System.out.println("line 151");
                        return false;
                    }
            }
            else {
                if (pieceAhead(getPrev(kingPiece.getFile()), kingPiece.getRank(), board)
                    || pieceAhead(getPrev(getPrev((kingPiece.getFile()))), kingPiece.getRank(), board)
                    || pieceAhead(getNext(rookPiece.getFile()), kingPiece.getRank(), board)) {
                        System.out.println("line 159");
                        return false;
                    }
            }

            // third case: the king is not in check
            if (isRightRook) {
                if (kingPiece.isIllegalCheck(color, startingFile, kingPiece.getRank(), kingPiece, null)) {
                    System.out.println("line 167");
                    return false;
                }
            }
            else {
                if (kingPiece.isIllegalCheck(color, startingFile, kingPiece.getRank(), kingPiece, null)) {
                    System.out.println("line 173");
                    return false;
                }
            }

            // fourth case: the king does not pass through or finish on a square that is attacked by an enemy piece (inCheck)
            if (isRightRook) {
                
                if (kingPiece.isIllegalCheck(color, PieceFile.f, kingPiece.getRank(), kingPiece, null)
                    /*|| kingPiece.isIllegalCheck(color, movedFile, kingPiece.getRank(), kingPiece, cloneBoard(board))*/) {
                        System.out.println("line 194: " + kingPiece.getFile() +""+kingPiece.getRank());
                        System.out.println(getNext(startingFile) + "" + kingPiece.getRank() + " and " + movedFile + "" + kingPiece.getRank());
                        System.out.println("line 182");
                        return false;
                }
            }
            else {
                if (kingPiece.isIllegalCheck(color, getPrev(startingFile), kingPiece.getRank(), kingPiece, null)
                /*|| kingPiece.isIllegalCheck(color, getPrev(getPrev((kingPiece.getFile()))), kingPiece.getRank(), kingPiece, board)*/) {
                    System.out.println("line 189");
                    return false;
            }
            
        }
        return true;
    }
    return false;
}
    
}
