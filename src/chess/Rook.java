package chess;

import java.util.EnumSet;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Rook extends Piece {
    public Rook (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        super(x, y, pieceName);
    }


    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
        //System.out.println("testing123");
        // moving straight forward
        //isFirstMove(PieceType.WR,color);
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

    //     PieceFile file = getFile();
    //     int rank = getRank();

    //     // Check for horizontal or vertical movement only.
    //     if (file == movedFile || rank == movedRank) {
    //         int spacesMoved = file == movedFile ? Math.abs(rank - movedRank) : Math.abs(file.ordinal() - movedFile.ordinal());

    //         // Check if there's any piece in the path of movement.
    //         if (color == Player.white) {
    //             for (int i = 1; i <= spacesMoved; i++) {
    //                 int nextRank = rank + (file == movedFile ? i : 0);
    //                 PieceFile nextFile = file != movedFile ? PieceFile.values()[file.ordinal() + i] : file;
    //                 if (identifyPieceType(nextFile, nextRank) != null) {
    //                     return false; // Path is not clear.
    //                 }
    //             }
    //         } else if (color == Player.black) {
    //             for (int i = 1; i <= spacesMoved; i++) {
    //                 int nextRank = rank - (file == movedFile ? i : 0);
    //                 PieceFile nextFile = file != movedFile ? PieceFile.values()[file.ordinal() - i] : file;
    //                 if (identifyPieceType(nextFile, nextRank) != null) {
    //                     return false; // Path is not clear.
    //                 }
    //             }
    //         }

    //         // Assuming direct movement to an empty space or capturing an opponent's piece is valid.
    //         return true;
    //     }

    //     // If it's neither vertical nor horizontal movement, it's invalid for a rook.
    //     return false;
    // }

    
//     @Override
//     public boolean isValid(PieceFile movedFile, int movedRank) {
//     PieceFile file = this.getFile();
//     int rank = this.getRank();
    
//     // Check for horizontal or vertical movement
//     if (file == movedFile || rank == movedRank) {
//         if (!isPathClear(file, rank, movedFile, movedRank)) {
//             // Path is not clear, move is not valid
//             return false;
//         }
        
//         // Check for capturing
//         if (isOpponentPieceAt(movedFile, movedRank)) {
//             // Identify the piece object at the destination
//             Piece rook = identifyPieceType(movedFile, movedRank);
//             // Assuming identifyPieceType correctly identifies and returns the piece object, including its color
            
//             // Create a ReturnPiece object for the piece to be captured
//             ReturnPiece pieceToCapture = new ReturnPiece();
//             pieceToCapture.setPieceFile(rook.getFile());
//             pieceToCapture.setPieceRank(rook.getRank());
//             pieceToCapture.setPieceType(rook.getPieceName());
            
//             // Remove the captured piece from the board
//             startingBoard.piecesOnBoard.remove(pieceToCapture);
            
//             // Capture logic completed
//         }
        
//         return true;
//     }
    
//     return false; // Not a valid rook move
// }

// private boolean isPathClear(PieceFile startFile, int startRank, PieceFile endFile, int endRank) {
//     int startFileIndex = startFile.ordinal();
//     int endFileIndex = endFile.ordinal();
//     int fileDirection = Integer.compare(endFileIndex, startFileIndex);
//     int rankDirection = Integer.compare(endRank, startRank);

//     int currentFileIndex = startFileIndex + fileDirection;
//     int currentRank = startRank + rankDirection;

//     while (currentFileIndex != endFileIndex || currentRank != endRank) {
//         Piece pieceAtCurrentLocation = Chess.getPiece(PieceFile.values()[currentFileIndex], currentRank);
//         if (pieceAtCurrentLocation != null) {
//             return false; // Found a piece in the path
//         }
//         currentFileIndex += (currentFileIndex != endFileIndex) ? fileDirection : 0;
//         currentRank += (currentRank != endRank) ? rankDirection : 0;
//     }

//     return true; //
// }

// private boolean isOpponentPieceAt(PieceFile file, int rank) {
//     Piece pieceAtLocation = Chess.getPiece(file, rank);
//     if (pieceAtLocation != null && pieceAtLocation.isWhite() != this.isWhite()) {
//         return true; // There is an opponent's piece at the given location
//     }
//     return false; // No opponent piece at the location
// }
//}
