package chess;

import chess.ReturnPiece.PieceFile;

public class Knight extends Piece {

    public Knight (ReturnPiece.PieceFile x, int y, ReturnPiece.PieceType pieceName) {
        super(x, y, pieceName);

    }

    public boolean isValid(ReturnPiece.PieceFile startingFile, int startingRank, ReturnPiece.PieceFile movedFile, int movedRank, Player color, boolean forIllegalCheck, ReturnPlay board) {
        PieceFile file = startingFile;
        int rank = startingRank;
        // Calculate file and rank differences to check for L-shaped movement
        int fileDifference = Math.abs(movedFile.ordinal() - file.ordinal());
        int rankDifference = Math.abs(movedRank - rank);

        // Check for L-shaped move: 2 by 1 or 1 by 2
        if ((fileDifference == 2 && rankDifference == 1) || (fileDifference == 1 && rankDifference == 2)) {
            // Since the Knight jumps over pieces, we don't need to check the path between the start and end positions
            // However, we need to check the legality of the destination square
            Piece targetPiece = identifyPieceType(movedFile, movedRank, board); // Assuming this method identifies the piece at the target location

            if (targetPiece == null) {
                // The destination square is empty; move is valid
                return true;
            } else {
                // There is a piece at the destination square; check if it's capturable (opponent's piece)
                Piece myPiece = identifyPieceType(file, rank, board);
                Piece capturedPiece = identifyPieceType(movedFile, movedRank, board);
                    
                return isSameColor(myPiece, capturedPiece, forIllegalCheck, board);
            }
        }
        return false;
    }

}
