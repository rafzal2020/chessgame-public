package chess;

import java.util.EnumSet;

import chess.ReturnPiece.*;

public class Queen extends Piece {
    public Queen(PieceFile x, int y, PieceType pieceName) {
        super(x,y,pieceName);
    }


    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
        // moving straight forward or backward
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
                    if (movedFile == getNext(file)) {
                        noPieceFound = true;
                    }
                    else {
                        for (PieceFile f : EnumSet.range(getNext(file), getPrev(movedFile))) {
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
                        for (PieceFile f : EnumSet.range(getNext(movedFile), getPrev(file))) {
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
        // moving diagonally
        if (!file.equals(movedFile) && rank != movedRank) {
            if (Math.abs(movedFile.ordinal() - file.ordinal()) == 1 && Math.abs(movedRank - rank) == 1) {
                // This is a valid one-space diagonal move, assuming no piece is blocking
                // Optionally, add any additional checks needed for capturing or other rules here
                if (pieceAhead(movedFile, movedRank, board)) {
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                } else return true;
                //return true; // Directly return true for a valid one-space diagonal move
            }

            int dirRank = direction(1, movedFile, movedRank);
            int dirFile = direction(0, movedFile, movedRank);
            int loop = 0;
            // check if the move is a valid diagonal move
            if (dirRank == 1) {
                loop = movedRank - rank;
            } else if (dirRank == -1) loop = rank - movedRank;
            int index = 0;
            PieceFile testFile = file;
            if (dirFile == 1) {
                while (index < loop) {
                    testFile = getNext(testFile);
                    index++;
                }
            } else {
                while (index < loop) {
                    testFile = getPrev(testFile);
                    index++;
                }
            }
            if (!testFile.equals(movedFile)) { 
                //System.out.println(testFile + ", " + movedFile);
                return false; 
            }
            // capturing a piece
            if (pieceAhead(movedFile, movedRank, board)) {
                boolean noPieceFound = true;
                if (dirRank == 1 && dirFile == 1) { // moving up-right
                    int rankIndex = rank+1;
                    PieceFile startFile = getNext(file);
                    PieceFile endFile = getPrev(movedFile);
                    if (startFile.ordinal() > endFile.ordinal()) {
                        // Swap them if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex++;
                    }
                }
                else if (dirRank == 1 && dirFile == -1) {   // moving up-left
                    int rankIndex = movedRank-1;
                    PieceFile startFile = getNext(movedFile); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(file); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                     // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex--;
                    }
                }
                else if (dirRank == -1 && dirFile == 1) {   // moving down-right
                    int rankIndex = rank-1;
                    PieceFile startFile = getNext(file); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(movedFile); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                        // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex--;
                    }
                }
                else if (dirRank == -1 && dirFile == -1) {  // moving down-left
                    int rankIndex = movedRank+1;
                    for (PieceFile f : EnumSet.range(getNext(movedFile), getPrev(file))) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            //System.out.println("test");
                            return false;
                        }
                        rankIndex++;
                    }
                }

                if (noPieceFound) {  // no pieces blocking, can capture
                    Piece myPiece = identifyPieceType(file, rank, board);
                    Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                    return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
                }
            }
            else {  // no capture
                // still test for any pieces blocking path
                boolean noPieceFound = true;
                if (dirRank == 1 && dirFile == 1) { // moving up-right
                    int rankIndex = rank+1;
                    PieceFile startFile = getNext(file); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(movedFile); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                    // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex++;
                    }
                }
                else if (dirRank == 1 && dirFile == -1) {   // moving up-left
                    int rankIndex = movedRank-1;
                    PieceFile startFile = getNext(movedFile); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(file); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                        // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex--;
                    }
                }
                else if (dirRank == -1 && dirFile == 1) {   // moving down-right
                    int rankIndex = rank-1;
                    PieceFile startFile = getNext(file); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(movedFile); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                        // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex--;
                    }
                }
                else if (dirRank == -1 && dirFile == -1) {  // moving down-left
                    int rankIndex = movedRank+1;
                    PieceFile startFile = getNext(movedFile); // Initially assumed to be the start of the range
                    PieceFile endFile = getPrev(file); // Initially assumed to be the end of the range

                    // Ensure that the startFile has a lower ordinal than the endFile
                    if (startFile.ordinal() > endFile.ordinal()) {
                        // Swap startFile and endFile if they are in the wrong order
                        PieceFile temp = startFile;
                        startFile = endFile;
                        endFile = temp;
                    }
                    for (PieceFile f : EnumSet.range(startFile, endFile)) {
                        if (pieceAhead(f, rankIndex, board)) {
                            noPieceFound = false;
                            
                            return false;
                        }
                        rankIndex++;
                    }
                }

                if (noPieceFound) {
                    return true;
                }
            }
        }
        //System.out.println("test5");
        return false;
    }
}
