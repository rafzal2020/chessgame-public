package chess;

import java.util.EnumSet;

import chess.ReturnPiece.PieceFile;

public class Rook extends Piece {
    public Rook (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        super(x, y, pieceName);
    }


    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
        // moving straight forward
        if (file.equals(movedFile)) {
            // if there is a piece to capture
            int dir = direction(1, movedFile, movedRank);
            if(pieceAhead(file, movedRank, board)) {  
                // check first if there is a piece blocking the capture piece.
                boolean noPieceFound = true;
                if (dir == 1) {
                    for (int i = rank+1; i < movedRank; i++) {
                        if (pieceAhead(file, i, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                }
                else if (dir == -1) {
                    for (int i = rank-1; i > movedRank; i--) {
                        if (pieceAhead(file, i, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                }
                if (noPieceFound) { // the capture is valid
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                }
            }
                
            // no piece to capture
            else {
                // there is a piece ahead, but not capturing (i.e. cannot move forward past a piece that is in the way)
                boolean noPieceFound = true;
                if (dir == 1) {
                    for (int i = rank+1; i < movedRank; i++) {
                        if (pieceAhead(file, i, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                }
                else if (dir == -1) {
                    for (int i = rank-1; i > movedRank; i--) {
                        if (pieceAhead(file, i, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                }
                // if there is nothing ahead, the queen can move forward
                if (noPieceFound) {
                    return true;
                }

            }
        }
        // moving horizonally
        if (rank == movedRank) {
            int dir = direction(0, movedFile, movedRank);
            // there is a piece to capture
            if (pieceAhead(movedFile, movedRank, board)) {
                boolean noPieceFound = true;
                if (dir == 1) {     // moving right
                    if (movedFile == getNext(file)) {   // moving only 1 space 
                        noPieceFound = true;
                    }
                    else {
                    for (PieceFile f : EnumSet.range(getNext(file), getPrev(movedFile))) {  // exclusive
                        if (pieceAhead(f, rank, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                    }
                }
                else if (dir == -1) {   // moving left
                    if (movedFile == getPrev(file)) {
                        noPieceFound = true;
                    }
                    else {
                        for (PieceFile f : EnumSet.range(getNext(movedFile), getPrev(file))) {  // exclusive
                            if (pieceAhead(f, rank, board)) {
                                noPieceFound = false;
                                return false;
                            }
                        }
                    }
                }
                if (noPieceFound) { // the capture is valid
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                }
            }
            // no capture
            else {
                // there is a piece ahead, but not capturing (i.e. cannot move forward past a piece that is in the way)
                boolean noPieceFound = true;
                if (dir == 1) {
                    if (movedFile == getNext(file)) {   // moving only 1 space 
                        noPieceFound = true;
                    }
                    else {
                    for (PieceFile f : EnumSet.range(getNext(file), getPrev(movedFile))) {  // exclusive
                        if (pieceAhead(f, rank, board)) {
                            noPieceFound = false;
                            return false;
                        }
                    }
                    }
                }
                else if (dir == -1) {
                    if (movedFile == getPrev(file)) {
                        noPieceFound = true;
                    }
                    else {
                        for (PieceFile f : EnumSet.range(getNext(movedFile), getPrev(file))) {  // exclusive
                            if (pieceAhead(f, rank, board)) {
                                noPieceFound = false;
                                return false;
                            }
                        }
                    }
                }
                // if there is nothing ahead, the queen can move forward
                if (noPieceFound) {
                    return true;
                }

            }
        }
        return false;

    }

}

