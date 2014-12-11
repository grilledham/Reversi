/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.ai;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import reversi.model.BoardModel;
import reversi.model.Owner;

/**
 *
 * @author James
 */
public class BitBoard {

    private static final String ZEROS = "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000";

    private static final long TOP_LEFT_CORNER = stringToBoard("11111111"
            + "11111111"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000");

    private static final long TOP_RIGHT_CORNER = stringToBoard("11111111"
            + "11111111"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011");

    private static final long BOTTOM_RIGHT_CORNER = stringToBoard("00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "11111111"
            + "11111111");

    private static final long BOTTOM_LEFT_CORNER = stringToBoard("11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11111111"
            + "11111111");

    private static final long TOP_2_ROWS = stringToBoard("11111111"
            + "11111111"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long BOTTOM_2_ROWS = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11111111"
            + "11111111");

    private static final long TOP_LEFT_ONES = stringToBoard("11111111"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000");

    private static final long TOP_RIGHT_ONES = stringToBoard("11111111"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001");

    private static final long BOTTOM_LEFT_ONES = stringToBoard("10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "11111111");

    private static final long BOTTOM_RIGHT_ONES = stringToBoard("00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "11111111");

    private static final long TOP_ONES = stringToBoard("11111111"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long BOTTOM_ONES = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11111111");

    private static final long L1 = stringToBoard("00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111");

    private static final long L2 = stringToBoard("00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111");

    private static final long L3 = stringToBoard("00001111"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00001111");

    private static final long L4 = stringToBoard("00000111"
            + "00000111"
            + "00000111"
            + "00000111"
            + "00000111"
            + "00000111"
            + "00000111"
            + "00000111");

    private static final long L5 = stringToBoard("00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011"
            + "00000011");

    private static final long L6 = stringToBoard("00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001");

    private static final long R1 = stringToBoard("11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100");

    private static final long R2 = stringToBoard("11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000");

    private static final long R3 = stringToBoard("11110000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "11110000");

    private static final long R4 = stringToBoard("11100000"
            + "11100000"
            + "11100000"
            + "11100000"
            + "11100000"
            + "11100000"
            + "11100000"
            + "11100000");

    private static final long R5 = stringToBoard("11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000"
            + "11000000");

    private static final long R6 = stringToBoard("10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000"
            + "10000000");

    private static final long DL1 = stringToBoard("00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00000000"
            + "00000000");

    private static final long DL2 = stringToBoard("00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DL3 = stringToBoard("00001111"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DL4 = stringToBoard("00000111"
            + "00000111"
            + "00000111"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DL5 = stringToBoard("00000011"
            + "00000011"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DL6 = stringToBoard("00000001"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DR1 = stringToBoard("11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "00000000"
            + "00000000");

    private static final long DR2 = stringToBoard("11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DR3 = stringToBoard("11110000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DR4 = stringToBoard("11100000"
            + "11100000"
            + "11100000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DR5 = stringToBoard("11000000"
            + "11000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long DR6 = stringToBoard("10000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000");

    private static final long UL1 = stringToBoard("00000000"
            + "00000000"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111"
            + "00111111");

    private static final long UL2 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111"
            + "00011111");

    private static final long UL3 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00001111"
            + "00001111"
            + "00001111"
            + "00001111");

    private static final long UL4 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000111"
            + "00000111"
            + "00000111");

    private static final long UL5 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000011"
            + "00000011");

    private static final long UL6 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000001");

    private static final long UR1 = stringToBoard("00000000"
            + "00000000"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100"
            + "11111100");

    private static final long UR2 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000"
            + "11111000");

    private static final long UR3 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11110000"
            + "11110000"
            + "11110000"
            + "11110000");

    private static final long UR4 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11100000"
            + "11100000"
            + "11100000");

    private static final long UR5 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11000000"
            + "11000000");

    private static final long UR6 = stringToBoard("00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "10000000");

    private long whitePieces, blackPieces;
    private Owner owner;

    public BitBoard() {
    }

    public BitBoard(BoardModel bm) {
        owner = bm.getTurn();
        Owner[][] board = bm.getBoard();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y].equals(Owner.BLACK)) {
                    blackPieces = placePiece(blackPieces, x, y);
                } else if (board[x][y].equals(Owner.WHITE)) {
                    whitePieces = placePiece(whitePieces, x, y);
                }
            }
        }

        //printBoard(blackPieces);
    }

    public BitBoard(BitBoard bb) {
        whitePieces = bb.whitePieces;
        blackPieces = bb.blackPieces;
        owner = bb.owner;
    }

    public void takeTurn(long pos) {

        if (owner.equals(Owner.BLACK)) {
            pos |= flipPieces(pos, blackPieces, whitePieces);
            blackPieces = placePieces(blackPieces, pos);
            whitePieces = removePieces(whitePieces, pos);
        } else {
            pos |= flipPieces(pos, whitePieces, blackPieces);
            whitePieces = placePieces(whitePieces, pos);
            blackPieces = removePieces(blackPieces, pos);
        }

        owner = owner.opposite();
    }

    public long flips(long pos) {
        if (owner.equals(Owner.BLACK)) {
            pos |= flipPieces(pos, blackPieces, whitePieces);

        } else {
            pos |= flipPieces(pos, whitePieces, blackPieces);

        }
        return pos;
    }

    public void flipTurn() {
        owner = owner.opposite();
    }

    public static Point longToPoint(long pos) {
        Point p = new Point();

        for (int y = 0; y < 8; y++) {
            long line = BOTTOM_ONES << (y * 8);
            if ((line & pos) != 0) {
                p.y = 7 - y;
                break;
            }
        }
        for (int x = 0; x < 8; x++) {
            long line = L6 << x;
            if ((line & pos) != 0) {
                p.x = 7 - x;
                break;
            }
        }

        return p;
    }

    public static List<Long> split(long board) {
        List<Long> list = new LinkedList<>();

        for (long l = 1L << 63; l != 0; l = l >>> 1) {
            if ((l & board) != 0) {
                list.add(l);
            }
        }

        return list;
    }

    public long getLegalMoves() {
        if (owner.equals(Owner.BLACK)) {
            return legalMoves(blackPieces, whitePieces);
        } else {
            return legalMoves(whitePieces, blackPieces);
        }
    }

    public int calculateScoreDifference(Owner turn) {
        if (turn.equals(Owner.BLACK)) {
            return Long.bitCount(blackPieces) - Long.bitCount(whitePieces);
        } else {
            return Long.bitCount(whitePieces) - Long.bitCount(blackPieces);
        }
    }

    private static long removePieces(long board, long pieces) {
        return board & (~pieces);
    }

    private static long removePiece(long board, int x, int y) {
        long pos = ~(1L << (x + (8 * y)));
        return board & pos;
    }

    private static long placePieces(long board, long pieces) {
        return board | pieces;
    }

    public static long placePiece(long board, int x, int y) {
        long pos = 1L << ((7 - x) + (8 * (7 - y)));
        return board | pos;
    }

    public static void printBoard(long board) {
        String s = Long.toBinaryString(board);
        String s2 = (ZEROS + s).substring(s.length());

        for (int i = 0; i < 64;) {
            System.out.println(s2.subSequence(i, i += 8));
        }
    }

    public static long stringToBoard(String s) {
        long board = 0;

        for (int i = 0; i < 64; i++) {
            board <<= 1;
            char c = s.charAt(i);
            if (c == '1') {
                board |= 1L;
            }
        }

        return board;
    }

    private void setupBoard() {
        blackPieces = placePiece(blackPieces, 3, 3);
        blackPieces = placePiece(blackPieces, 4, 4);

        whitePieces = placePiece(whitePieces, 4, 3);
        whitePieces = placePiece(whitePieces, 3, 4);

    }

    private static long flipPieces(long pos, long us, long them) {
        return flipUp(pos, us, them)
                | flipDown(pos, us, them)
                | flipLeft(pos, us, them)
                | flipRight(pos, us, them)
                | flipUpLeft(pos, us, them)
                | flipUpRight(pos, us, them)
                | flipDownLeft(pos, us, them)
                | flipDownRight(pos, us, them);
    }

    private static long flipUp(long pos, long us, long them) {
        if ((pos & TOP_2_ROWS) != 0) {
            return 0;
        }

        pos <<= 8;

        while ((pos & them) == pos) {

            pos |= pos << 8;
            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & TOP_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipDown(long pos, long us, long them) {
        if ((pos & BOTTOM_2_ROWS) != 0) {
            return 0;
        }
        pos >>= 8;

        while ((pos & them) == pos) {
            pos |= pos >> 8;
            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & BOTTOM_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipLeft(long pos, long us, long them) {

        if ((pos & R5) != 0) {
            return 0;
        }

        pos <<= 1;

        while ((pos & them) == pos) {

            pos |= pos << 1;
            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & R6) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipRight(long pos, long us, long them) {
        if ((pos & L5) != 0) {
            return 0;
        }
        pos >>= 1;

        while ((pos & them) == pos) {

            pos |= pos >> 1;
            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & L6) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipUpLeft(long pos, long us, long them) {
        if ((pos & TOP_LEFT_CORNER) != 0) {
            return 0;
        }

        pos <<= 9;

        while ((pos & them) == pos) {

            pos |= pos << 9;

            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & TOP_LEFT_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipDownRight(long pos, long us, long them) {
        if ((pos & BOTTOM_RIGHT_CORNER) != 0) {
            return 0;
        }
        pos >>= 9;

        while ((pos & them) == pos) {

            pos |= pos >> 9;

            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & BOTTOM_RIGHT_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipUpRight(long pos, long us, long them) {
        if ((pos & TOP_RIGHT_CORNER) != 0) {
            return 0;
        }
        pos <<= 7;

        while ((pos & them) == pos) {

            pos |= pos << 7;

            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & TOP_RIGHT_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long flipDownLeft(long pos, long us, long them) {
        if ((pos & BOTTOM_LEFT_CORNER) != 0) {
            return 0;
        }
        pos >>= 7;

        while ((pos & them) == pos) {

            pos |= pos >> 7;

            if ((pos & us) != 0) {
                return pos & them;
            }
            if ((pos & BOTTOM_LEFT_ONES) != 0) {
                return 0;
            }
        }
        return 0;
    }

    private static long legalMoves(long us, long them) {
        return legalMovesUp(us, them)
                | legalMovesDown(us, them)
                | legalMovesLeft(us, them)
                | legalMovesRight(us, them)
                | legalMovesDownRight(us, them)
                | legalMovesUpRight(us, them)
                | legalMovesDownLeft(us, them)
                | legalMovesUpLeft(us, them);
    }

    private static long legalMovesDownRight(long us, long them) {
        return ~(us | them)
                & ((DR1 & them << 9 & us << 18)
                | (DR2 & them << 9 & them << 18 & us << 27)
                | (DR3 & them << 9 & them << 18 & them << 27 & us << 36)
                | (DR4 & them << 9 & them << 18 & them << 27 & them << 36 & us << 45)
                | (DR5 & them << 9 & them << 18 & them << 27 & them << 36 & them << 45 & us << 54)
                | (DR6 & them << 9 & them << 18 & them << 27 & them << 36 & them << 45 & them << 54 & us << 63));
    }

    private static long legalMovesUpLeft(long us, long them) {
        return ~(us | them)
                & ((UL1 & them >> 9 & us >> 18)
                | (UL2 & them >> 9 & them >> 18 & us >> 27)
                | (UL3 & them >> 9 & them >> 18 & them >> 27 & us >> 36)
                | (UL4 & them >> 9 & them >> 18 & them >> 27 & them >> 36 & us >> 45)
                | (UL5 & them >> 9 & them >> 18 & them >> 27 & them >> 36 & them >> 45 & us >> 54)
                | (UL6 & them >> 9 & them >> 18 & them >> 27 & them >> 36 & them >> 45 & them >> 54 & us >> 63));
    }

    private static long legalMovesUpRight(long us, long them) {
        return ~(us | them)
                & ((UR1 & them >> 7 & us >> 14)
                | (UR2 & them >> 7 & them >> 14 & us >> 21)
                | (UR3 & them >> 7 & them >> 14 & them >> 21 & us >> 28)
                | (UR4 & them >> 7 & them >> 14 & them >> 21 & them >> 28 & us >> 35)
                | (UR5 & them >> 7 & them >> 14 & them >> 21 & them >> 28 & them >> 35 & us >> 42)
                | (UR6 & them >> 7 & them >> 14 & them >> 21 & them >> 28 & them >> 35 & them >> 42 & us >> 49));
    }

    private static long legalMovesDownLeft(long us, long them) {
        return ~(us | them)
                & ((DL1 & them << 7 & us << 14)
                | (DL2 & them << 7 & them << 14 & us << 21)
                | (DL3 & them << 7 & them << 14 & them << 21 & us << 28)
                | (DL4 & them << 7 & them << 14 & them << 21 & them << 28 & us << 35)
                | (DL5 & them << 7 & them << 14 & them << 21 & them << 28 & them << 35 & us << 42)
                | (DL6 & them << 7 & them << 14 & them << 21 & them << 28 & them << 35 & them << 42 & us << 49));
    }

    private static long legalMovesRight(long us, long them) {
        return ~(us | them)
                & ((R1 & them << 1 & us << 2)
                | (R2 & them << 1 & them << 2 & us << 3)
                | (R3 & them << 1 & them << 2 & them << 3 & us << 4)
                | (R4 & them << 1 & them << 2 & them << 3 & them << 4 & us << 5)
                | (R5 & them << 1 & them << 2 & them << 3 & them << 4 & them << 5 & us << 6)
                | (R6 & them << 1 & them << 2 & them << 3 & them << 4 & them << 5 & them << 6 & us << 7));
    }

    private static long legalMovesLeft(long us, long them) {
        return ~(us | them)
                & ((L1 & them >> 1 & us >> 2)
                | (L2 & them >> 1 & them >> 2 & us >> 3)
                | (L3 & them >> 1 & them >> 2 & them >> 3 & us >> 4)
                | (L4 & them >> 1 & them >> 2 & them >> 3 & them >> 4 & us >> 5)
                | (L5 & them >> 1 & them >> 2 & them >> 3 & them >> 4 & them >> 5 & us >> 6)
                | (L6 & them >> 1 & them >> 2 & them >> 3 & them >> 4 & them >> 5 & them >> 6 & us >> 7));
    }

    private static long legalMovesDown(long us, long them) {
        return ~(us | them)
                & ((them >> 8 & us >> 16)
                | (them >> 8 & them >> 16 & us >> 24)
                | (them >> 8 & them >> 16 & them >> 24 & us >> 32)
                | (them >> 8 & them >> 16 & them >> 24 & them >> 32 & us >> 40)
                | (them >> 8 & them >> 16 & them >> 24 & them >> 32 & them >> 40 & us >> 48)
                | (them >> 8 & them >> 16 & them >> 24 & them >> 32 & them >> 40 & them >> 48 & us >> 56));
    }

    private static long legalMovesUp(long us, long them) {
        return ~(us | them)
                & ((them << 8 & us << 16)
                | (them << 8 & them << 16 & us << 24)
                | (them << 8 & them << 16 & them << 24 & us << 32)
                | (them << 8 & them << 16 & them << 24 & them << 32 & us << 40)
                | (them << 8 & them << 16 & them << 24 & them << 32 & them << 40 & us << 48)
                | (them << 8 & them << 16 & them << 24 & them << 32 & them << 40 & them << 48 & us << 56));
    }

    public long getWhitePieces() {
        return whitePieces;
    }

    public long getBlackPieces() {
        return blackPieces;
    }

    public Owner getOwner() {
        return owner;
    }
}
