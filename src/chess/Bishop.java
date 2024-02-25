package chess;

import java.util.EnumSet;

import chess.ReturnPiece.PieceFile;

public class Bishop extends Piece {
    public Bishop (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        super(x, y, pieceName);
    }

    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
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
        return false;
    }
}
