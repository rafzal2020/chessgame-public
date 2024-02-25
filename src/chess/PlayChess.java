package chess;

import java.util.ArrayList;
import java.util.Scanner;

public class PlayChess {

	public static void main(String[] args) {
		 // TODO Auto-generated method stub
		 Scanner sc = new Scanner(System.in);
		 Chess.start();
		
		 String line = sc.nextLine();
		 while (!line.equals("quit")) {
		 	if (line.equals("reset")) {
		 		Chess.start();
		 		System.out.println();
		 		line = sc.nextLine();
		 		continue;
		 	}
		 	// move 
		 	ReturnPlay res = Chess.play(line);
			
		 	// print result message
		 	if (res.message != null) {
		 		System.out.println("\n"+res.message);
		 	}
		 	System.out.println();
			
		 	// print result board
		 	printBoard(res.piecesOnBoard);
		 	System.out.println();
			
		 	// next line
		 	line = sc.nextLine();
			}
		
		 sc.close();

		// Scanner sc = new Scanner(System.in);
        // Chess.start();

        // System.out.println("Enter commands (move, reset, identify, quit):");
        // String line = sc.nextLine();
        // while (!line.equals("quit")) {
        //     if (line.startsWith("reset")) {
        //         Chess.start();
        //         System.out.println("Board reset.\n");
        //     } else if (line.startsWith("identify")) {
        //         // Process the identify command
        //         String[] parts = line.split(" ");
        //         if (parts.length == 3) {
        //             char fileChar = parts[1].charAt(0);
        //             int rank = Integer.parseInt(parts[2]);
        //             ReturnPiece.PieceFile file = Chess.convertToPieceFile(fileChar);
        //             ReturnPiece.PieceType type = Chess.identifyPieceType(file, rank);
        //             System.out.println("Piece at " + fileChar + rank + " is: " + type);
        //         } else {
        //             System.out.println("Invalid identify command. Use 'identify <file><rank>' (e.g., 'identify e2').");
        //         }
        //     } else {
        //         // Assume the input is a move command
        //         ReturnPlay res = Chess.play(line);
                
        //         // Print result message
        //         if (res.message != null) {
        //             System.out.println("\n" + res.message);
        //         }

        //         // Print result board
        //         printBoard(res.piecesOnBoard);
        //         System.out.println();
        //     }

        //     // Prompt for next line
        //     line = sc.nextLine();
        // }

        // sc.close();
	}
	
	static void printBoard(ArrayList<ReturnPiece> pieces) {
		String[][] board = makeBlankBoard();
		if (pieces != null) {
			printPiecesOnBoard(pieces, board);
		}
		for (int r=0; r < 8; r++) {
			for (int c=0; c < 8; c++) {
				System.out.print(board[r][c] + " ");
			}	
			System.out.println((8-r));
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}
	
	static String[][] makeBlankBoard() {
		String[][] board = new String[8][8];
		for (int r=0; r < 8; r++) {
			for (int c=0; c < 8; c++) {
				if (r % 2 == 0) {
					board[r][c] = c % 2 == 0 ? "  " : "##";
				} else {
					board[r][c] = c % 2 == 0 ? "##" : "  ";
				}
			}
		}
		return board;
	}
	
	static void printPiecesOnBoard(
			ArrayList<ReturnPiece> pieces, String[][] board) {
		for (ReturnPiece rp: pieces) {
			int file = (""+rp.pieceFile).charAt(0) - 'a';
			String pieceStr = "" + rp.pieceType;
			String ppstr = "";
			ppstr += Character.toLowerCase(pieceStr.charAt(0));
			ppstr += pieceStr.charAt(1) == 'P' ? 'p' : pieceStr.charAt(1);
			board[8-rp.pieceRank][file] = ppstr;
		}	
	}


	
	
}