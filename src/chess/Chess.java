// RAYAAN AFZAL, JUNAID
package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;



class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}




public class Chess{
	public static boolean isWhite;
	public static ReturnPlay startingBoard;
	public static Player playColor;
	public static boolean forCheckmateCheck = false;
	public static boolean bKingFirstMove = true;
    public static boolean wKingFirstMove = true;
    public static boolean rightBRookFirstMove = true;
    public static boolean leftBRookFirstMove = true;
    public static boolean rightWRookFirstMove = true;
    public static boolean leftWRookFirstMove = true;
	public static boolean validCastlingMove = false;
	public static boolean globalIsRightRook = false;
	public static ReturnPlay simBoard;
	
	enum Player { white, black }
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {
		/* FILL IN THIS METHOD */
		ReturnPlay result = new ReturnPlay();
		result.piecesOnBoard = new ArrayList<>();

		int[] moveset = Piece.getCoord(move);
		char firstFile = (char)moveset[0];
		int firstRank = moveset[1];

		ReturnPiece movedPiece = new ReturnPiece();
		movedPiece.pieceFile = convertToPieceFile(firstFile);
		movedPiece.pieceRank = firstRank;

		char secondFile = (char)moveset[2];
		ReturnPiece.PieceFile sF = convertToPieceFile(secondFile);
		int secondRank = moveset[3];

		ReturnPlay testBoard = startingBoard;
		Piece p = identifyPieceType(movedPiece.pieceFile, firstRank, startingBoard);
		Piece temp = identifyPieceType(movedPiece.pieceFile, firstRank, testBoard);
		if (p != null && !p.myTurn()) {
			p = null;
		}

		// Check if the move includes a draw offer
		boolean drawOffer = move.endsWith("draw?");
		if (drawOffer) {
			// Remove " draw?" from the move command to process the move normally
			move = move.substring(0, move.length() - 6);
		}

		if (p != null && p.isValid(movedPiece.pieceFile, firstRank, sF, secondRank, playColor, false, startingBoard)) {
			if (!p.isIllegalCheck(playColor, sF, secondRank, p.findKing(playColor, startingBoard), null)) {
				if (p.getPieceName() == PieceType.WK || p.getPieceName() == PieceType.BK) {
					p.isFirstMove(PieceType.WK, playColor);
				} else if (p.getPieceName() == PieceType.WR || p.getPieceName() == PieceType.BR) {
					p.isFirstMove(PieceType.WR, playColor);
				}
				p.makeMove(movedPiece.pieceFile, firstRank, sF, secondRank, startingBoard);
				

				if (validCastlingMove) {
					Rook rook = null;
					if (playColor == Player.white) {
						if (globalIsRightRook) {
							rook = new Rook(PieceFile.h, 1, PieceType.WR);
						}
						else {
							rook = new Rook(PieceFile.a, 1, PieceType.WR);
						}
					}
					if (playColor == Player.black) {
						if (globalIsRightRook) {
							rook = new Rook(PieceFile.h, 8, PieceType.BR);
						}
						else {
							rook = new Rook(PieceFile.a, 8, PieceType.BR);
						}
					}
					if (rook != null) {
						p.makeCastlingMove(p, rook.getFile(), rook.getRank(), startingBoard);
					}
					validCastlingMove = false;
					
				}
				if (p.isCheck(playColor, sF, secondRank)) {
					result.message = ReturnPlay.Message.CHECK;
				}
				if (((result.message == null || result.message == ReturnPlay.Message.CHECK))) {
					playColor = p.switchTurn(playColor);
					System.out.println("\n| " + playColor + "'s turn |\n");
				}

				// If a draw was offered and the move was valid, set the game state to draw
				if (drawOffer) {
					result.message = null;
					result.message = ReturnPlay.Message.DRAW;
				}
			}
			else {
				result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			}
		}
		else {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
		}

		testBoard.piecesOnBoard = startingBoard.piecesOnBoard;

		if (result.message == ReturnPlay.Message.CHECK && p != null && temp.isCheckmate(playColor, testBoard)) {
			result.message = null;
			
			if (playColor == Player.white) {
				result.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
			}
			if (playColor == Player.black) {
				result.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
			}
		}
		//System.out.println(p.getPieceName() + " (MOVED PIECE (Chess.java)): " + movedPiece.pieceFile +""+movedPiece.pieceRank);


		// Parse the promotion piece from the move string, assuming the last character
		// indicates the promotion
		char promotionChar = move.trim().charAt(move.trim().length() - 1); // Get the last character for promotion
		int promotionType = 0; // Default to no promotion
		switch (promotionChar) {
			case 'Q':
				promotionType = 1;
				break;
			case 'R':
				promotionType = 2;
				break;
			case 'B':
				promotionType = 3;
				break;
			case 'N':
				promotionType = 4;
				break;
		}

		// Assuming `p` represents the piece that was moved and `moveset` holds the move
		// details
		if (p instanceof Pawn && (secondRank == 1 || secondRank == 8)) {
			// Promotion logic

			ReturnPiece.PieceType newType = null;

			// Default to Queen if no promotion piece is specified or if the promotion type
			// is unrecognized
			if (promotionType == 0) {
				promotionType = 1; // Default to Queen
			}

			
			boolean isWhitePawn = p.getColor(p.getPieceName()) == 'W';
			

			switch (promotionType) {
				case 1:
					newType = isWhitePawn ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
					break;
				case 2:
					newType = isWhitePawn ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
					break;
				case 3:
					newType = isWhitePawn ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
					break;
				case 4:
					newType = isWhitePawn ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
					break;
				default:
					// Fallback to Queen if unrecognized
					newType = isWhitePawn ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
					break;

			}

			if (newType != null) {
				// Find and remove the pawn from the board
				startingBoard.piecesOnBoard
						.removeIf(piece -> piece.pieceFile == movedPiece.pieceFile && piece.pieceRank == firstRank);

				// Add the new promoted piece to the board
				ReturnPiece promotedPiece = new ReturnPiece();
				promotedPiece.pieceType = newType;
				promotedPiece.pieceFile = sF;
				promotedPiece.pieceRank = secondRank;
				startingBoard.piecesOnBoard.add(promotedPiece);
			}
		}

    	// Check if the move is a resignation
    	if (move.equalsIgnoreCase("resign")) {
			result.message = null;
			
			if (playColor == Player.white) {
				result.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
			}
			if (playColor == Player.black) {
				result.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			}
		}


		//System.out.println(rightWRookFirstMove);
		result.piecesOnBoard = startingBoard.piecesOnBoard;
		return result;
	}

	// returns the file type to the left of the current file (used for diagonal moves)
	public static ReturnPiece.PieceFile getPrev(PieceFile currPf) {
		PieceFile[] values = PieceFile.values();
    	int ordinal = currPf.ordinal();
    	int previousOrdinal = (ordinal - 1 + values.length) % values.length;
    	return values[previousOrdinal];
	}

	// returns the file type to the right of the current file (used for diagonal moves)
	public static ReturnPiece.PieceFile getNext(PieceFile currPf) {
		PieceFile[] values = PieceFile.values();
    	int ordinal = currPf.ordinal();
    	int nextOrdinal = (ordinal + 1) % values.length;
    	return values[nextOrdinal];
	}

	// Method to identify the type of a piece at a given position and then asign a Piece object
	public static Piece identifyPieceType(ReturnPiece.PieceFile file, int rank, ReturnPlay board) {
		ReturnPiece foundPiece = new ReturnPiece();
        for (ReturnPiece piece : board.piecesOnBoard) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                foundPiece.pieceType = piece.pieceType; // Returns the type of the piece found at the given location
            }
        }

		if (foundPiece.pieceType == PieceType.WP || foundPiece.pieceType == PieceType.BP) {
			Piece pawn;
			if (foundPiece.pieceType == PieceType.WP) {
				pawn = new Pawn(file, rank, ReturnPiece.PieceType.WP);
			} else {
				pawn = new Pawn(file, rank, ReturnPiece.PieceType.BP);
			}
			return pawn;
		}

		else if (foundPiece.pieceType == PieceType.WQ || foundPiece.pieceType == PieceType.BQ) {
			Piece queen;
			if (foundPiece.pieceType == PieceType.WQ) {
				queen = new Queen(file, rank, ReturnPiece.PieceType.WQ);
			} else {
				queen = new Queen(file, rank, ReturnPiece.PieceType.BQ);
			}
			return queen;
		}

		else if (foundPiece.pieceType == PieceType.WR || foundPiece.pieceType == PieceType.BR) {
			Piece rook;
			if (foundPiece.pieceType == PieceType.WR) {
				rook = new Rook(file, rank, ReturnPiece.PieceType.WR);
			} else {
				rook = new Rook(file, rank, ReturnPiece.PieceType.BR);
			}
			return rook;
		}

		else if (foundPiece.pieceType == PieceType.WB || foundPiece.pieceType == PieceType.BB) {
			Piece bishop;
			if (foundPiece.pieceType == PieceType.WB) {
				bishop = new Bishop(file, rank, ReturnPiece.PieceType.WB);
			} else {
				bishop = new Bishop(file, rank, ReturnPiece.PieceType.BB);
			}
			return bishop;
		}
		else if (foundPiece.pieceType == PieceType.WK || foundPiece.pieceType == PieceType.BK) {
			Piece king;
			if (foundPiece.pieceType == PieceType.WK) {
				king = new King(file, rank, ReturnPiece.PieceType.WK);
			} else {
				king = new King(file, rank, ReturnPiece.PieceType.BK);
			}
			return king;
		}
		else if (foundPiece.pieceType == PieceType.WN || foundPiece.pieceType == PieceType.BN) {
			Piece knight;
			if (foundPiece.pieceType == PieceType.WN) {
				knight = new Knight(file, rank, ReturnPiece.PieceType.WN);
			} else {
				knight = new Knight(file, rank, ReturnPiece.PieceType.BN);
			}
			return knight;
		}

        return null; // No piece found at the given location
    }



	// converts a char to the PieceFile type
	public static ReturnPiece.PieceFile convertToPieceFile(char c) {
		for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) { 
			if (file.name().charAt(0) == c) {
				return file;
			}
		}
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		forCheckmateCheck = false;
		bKingFirstMove = true;
    	wKingFirstMove = true;
    	rightBRookFirstMove = true;
    	leftBRookFirstMove = true;
    	rightWRookFirstMove = true;
    	leftWRookFirstMove = true;
		validCastlingMove = false;
		globalIsRightRook = false;


		playColor = Player.white;
		System.out.println("\n| White's turn |\n");
		startingBoard = new ReturnPlay();
		startingBoard.piecesOnBoard = new ArrayList<>();


		// add white pieces to board
		for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
			addPieceToBoard(ReturnPiece.PieceType.WP, file, 2); 				// pawns
		}

		addPieceToBoard(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1);	// left rook
		addPieceToBoard(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1);	// left knight
		addPieceToBoard(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1);	// left bishop
		addPieceToBoard(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1);	// queen
		addPieceToBoard(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1);	// king
		addPieceToBoard(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1);	// right bishop
		addPieceToBoard(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1);	// right knight
		addPieceToBoard(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1);	// right rook

		// add black pieces to board
		for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
			addPieceToBoard(ReturnPiece.PieceType.BP, file, 7);
		}

		addPieceToBoard(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8);
		addPieceToBoard(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8);
		addPieceToBoard(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8);
		addPieceToBoard(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8);
		addPieceToBoard(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8);
		addPieceToBoard(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8);
		addPieceToBoard(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8);
		addPieceToBoard(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8);

		//PlayChess.printBoard(startingBoard.piecesOnBoard);

	}

	private static void addPieceToBoard(ReturnPiece.PieceType type, ReturnPiece.PieceFile file, int rank) {
		ReturnPiece piece = new ReturnPiece();
		piece.pieceType = type;
		piece.pieceFile = file;
		piece.pieceRank = rank;
		startingBoard.piecesOnBoard.add(piece);
	}

	

}
