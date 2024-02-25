# chessgame
Created by RAYAAN AFZAL and JUNAID GHANI
A 2-player chess game assignmet for CS213 @ Rutgers U

bin/chess : contains class files when compiling main chess package <br>
src/chess : the chess package. All source code goes here. <br>


-------GETTING STARTED---------
Open a command prompt inside the chessgame-main folder.
Type the following:

javac -d bin src/chess/*.java

Make sure to specifiy the bin folder in order to dump class files into one organized spot.

Run the program with:
java -cp bin chess.PlayChess 


--------HOW TO PLAY-----------
The rules of this implementation of chess is same as any other, except this<br>
one does not have stalemate.

Moving Pieces:
This chess game uses the rank/file system to move pieces within the terminal.<br>
Specify the piece you want to move with its rank/file location, followed by<br>
the rank/file location of where you want to move
EXAMPLE: e2 e4 (moves the pawn at e2 to e4)

Pawn Promotion:
You can promote your pawn to a different piece when you reach your opponent's<br>
side. By default, the pawn will promote to queen, but you can promote it to<br>
Rook, Bishop, or a Knight like so: <br>
e7 e8 R (for Rook) <br>
e7 e8 B (for Bishop) <br>
e7 e8 N (for Knight) *Make sure to use N for knight as K is for king.*<br>
e7 e8 Q (for Queen) *You can specify a queen promotion if you'd like, but it is not necessary.

Resign:
You can resign and let your opponent win by simply typing 'resign'

Draw:
You can call for a draw within the move you just made by typing 'draw?'.<br> 
You must make the move first then draw. <br>
Example: e4 e5 draw?
Just typing 'draw?' will not work.

General Rules:
This implementation of chess includes all the rules chess has including <br>
castling, en passant, check, and checkmate. This game, however, does not <br>
have stalemate. This is substituted by the 'draw?' statement.<br>
<br>
<br>


Pieces we need to implement:<br>
Pawn (WP BP)    *IMPLEMENTED*<br>
Rook (WR BR)    *IMPLEMENTED*<br>
Knight (WN BN)  *IMPLEMENTED*<br>
Bishop (WB BB)  *IMPLEMENTED*<br>
Queen (WQ BQ)   *IMPLEMENTED*<br>
King (WK BK)    *IMPLEMENTED*<br>

Remaining implementations: <br>
All legitimate basic moves for all pieces       *IMPLEMENTED*<br>
Castling                                        *IMPLEMENTED*<br>
Enpassant                                       *IMPLEMENTED*<br>
Promotion                                       *IMPLEMENTED<br>
Identification of check                         *IMPLEMENTED*<br>
Identifcation of checkmate                      *IMPLEMENTED*<br>
Identification of illegal move                  *IMPLEMENTED*<br>
Resign                                          *IMPLEMENTED*<br>
Draw                                            *IMPLEMENTED*<br>

