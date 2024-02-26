package chess;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Pawn extends Piece {
    public Pawn (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        super(x, y, pieceName);
    }

    

    @Override
    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;

        if (file.equals(movedFile)) {        // if the piece is moving straight (no capture)
            if (isFirstMove(PieceType.WP, color) == true) {          // first move means it can move up 2 spaces
                if (color == Player.white) {
                    if (rank+1 == movedRank) {
                        if (pieceAhead(file, movedRank, board)) { return false; }
                        else { return true; }
                    }
                    else if (rank+2 == movedRank) {
                        if (pieceAhead(file, movedRank, board) || pieceAhead(file, movedRank-1, board)) { return false; }
                        else { 
                            Pawn newPawn = new Pawn(startingFile, startingRank+2, getPieceName());
                            movedPawns.add(newPawn);
                            //System.out.println(movedPawns.toString());
                            return true; 
                        }
                    }
                    else {
                        return false;
                    }
                }
                if (color == Player.black) {
                    if (rank-1 == movedRank) {
                        if (pieceAhead(file, movedRank, board)) { return false; }
                        else { return true; }
                    }
                    else if (rank-2 == movedRank) {
                        if (pieceAhead(file, movedRank, board) || pieceAhead(file, movedRank+1, board)) { return false; }
                        else {
                            Pawn newPawn = new Pawn(startingFile, startingRank-2, getPieceName());
                            movedPawns.add(newPawn);
                            //System.out.println(movedPawns.toString());
                            return true; 
                        }
                    }
                    else {
                        return false;
                    }
                }
                
            }
            else {
                if (color == Player.white) {
                    if (rank+1 == movedRank) {
                        if (pieceAhead(file, movedRank, board)) { return false; }
                        else { return true; }
                    }
                    else {
                        return false;
                    }
                }
                else if (color == Player.black) {
                    if (rank - 1 == movedRank) {
                        if (pieceAhead(file, movedRank, board)) { return false; }
                        else { return true; }
                    }
                    else return false;
                }
            }
        }

        if (file != movedFile) {        // moving diagonally (to capture)
            if (getNext(file) == movedFile || getPrev(file) == movedFile) {
                PieceFile capturingPieceFile;
                if (getNext(file) == movedFile) { capturingPieceFile = getNext(file); }
                else { capturingPieceFile = getPrev(file); }
                if (color == Player.white) {
                    if (rank + 1 == movedRank) {      // legal rank move
                        if (pieceAhead(capturingPieceFile, movedRank, board)) {    // if there is an opponent piece present in the space, capture it
                            Piece myPiece = identifyPieceType(file, rank, board);
                            Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                            return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                        }
                        else if (pieceAhead(capturingPieceFile, rank, board)) { // check for en passant capture
                            //System.out.println("testertester");
                            Piece myPiece = identifyPieceType(file, rank, board);
                            Piece capturedPiece = identifyPieceType(movedFile, rank, board);

                            if (validEnPassant(myPiece, capturedPiece)) {
                                return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                            }
                        }
                        else return false;
                    }
                } else if (color == Player.black) {
                    if (rank-1 == movedRank) {      // legal rank move
                        if (pieceAhead(capturingPieceFile, movedRank, board)) {    // if there is a piece present at the capture spot then capture it
                            Piece myPiece = identifyPieceType(file, rank, board);
                            Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                            return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                        }
                        else if (pieceAhead(capturingPieceFile, rank, board)) { // check for en passant capture
                            Piece myPiece = identifyPieceType(file, rank, board);
                            Piece capturedPiece = identifyPieceType(movedFile, rank, board);

                            if (validEnPassant(myPiece, capturedPiece)) {
                                return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                            }
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
            else return false;
        }
        return false;
    }
    
}


