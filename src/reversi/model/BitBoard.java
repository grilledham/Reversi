/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author James
 */
public class BitBoard {

    public static final int BLACK_COLOR = 1;
    public static final int NONE_COLOR = 0;
    public static final int WHITE_COLOR = -1;

    public static final String ZEROS = ""
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000";

    public static final long BOTTOM_ONES = stringToBoard(""
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "11111111");

    public static final long RIGHT_ONES = stringToBoard(""
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001"
            + "00000001");

    public static final long NOT_LEFT_ONES = stringToBoard(""
            + "01111111"
            + "01111111"
            + "01111111"
            + "01111111"
            + "01111111"
            + "01111111"
            + "01111111"
            + "01111111");

    public static final long NOT_RIGHT_ONES = stringToBoard(""
            + "11111110"
            + "11111110"
            + "11111110"
            + "11111110"
            + "11111110"
            + "11111110"
            + "11111110"
            + "11111110");

    public static final long NOT_SIDES = stringToBoard(""
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110");

    public static final long MIDDLE = stringToBoard(""
            + "00000000"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "01111110"
            + "00000000");

    public static final long CORNERS = stringToBoard(""
            + "10000001"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "00000000"
            + "10000001");

    private long whitePieces, blackPieces; //stablePieces;

    public BitBoard() {
        resetBoard();
    }

    public BitBoard(BoardModel bm) {
        Owner[][] board = bm.getBoard();

        for (int x = 0; x < bm.getColumns(); x++) {
            for (int y = 0; y < bm.getRows(); y++) {
                if (board[x][y].equals(Owner.BLACK)) {
                    blackPieces = placePiece(blackPieces, x, y);
                } else if (board[x][y].equals(Owner.WHITE)) {
                    whitePieces = placePiece(whitePieces, x, y);
                }
            }
        }
    }

    public BitBoard(BitBoard bb) {
        whitePieces = bb.whitePieces;
        blackPieces = bb.blackPieces;
//        stablePieces = bb.stablePieces;
    }

    public void takeTurn(long pos, Owner owner) {

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

    public void takeTurn(int x, int y, Owner owner) {
        long pos = placePiece(0, x, y);
        takeTurn(pos, owner);
    }

    public void takeTurn(long pos, int color) {
        if (color == 1) {
            pos |= flipPieces(pos, blackPieces, whitePieces);
            blackPieces = placePieces(blackPieces, pos);
            whitePieces = removePieces(whitePieces, pos);
        } else {
            pos |= flipPieces(pos, whitePieces, blackPieces);
            whitePieces = placePieces(whitePieces, pos);
            blackPieces = removePieces(blackPieces, pos);
        }
    }

    /**
     *
     * Does include the position piece.
     *
     * @param x
     * @param y
     * @param owner
     * @return
     */
    public List<Point> flippedPieces(int x, int y, Owner owner) {
        long pos = placePiece(0, x, y);
        List<Point> list = new LinkedList<>();
        list.add(new Point(x, y));

        if (owner.equals(Owner.BLACK)) {
            list.addAll(splitToPoints(flipPieces(pos, blackPieces, whitePieces)));
        } else {
            list.addAll(splitToPoints(flipPieces(pos, whitePieces, blackPieces)));
        }

        return list;
    }

    public static Point longToPoint(long pos) {
        Point p = new Point();

        for (int y = 0; y < 8; y++) {
            long line = BOTTOM_ONES << (y * 8);
            if ((line & pos) != 0) {
                p.y = y;
                break;
            }
        }
        for (int x = 0; x < 8; x++) {
            long line = RIGHT_ONES << x;
            if ((line & pos) != 0) {
                p.x = x;
                break;
            }
        }

        return p;
    }

    public static List<Point> splitToPoints(long board) {
        List<Point> list = new LinkedList<>();

        for (long l = 1L << 63; l != 0; l = l >>> 1) {
            if ((l & board) != 0) {
                list.add(longToPoint(l));
            }
        }

        return list;
    }

    public static long[] splitToArray(long board) {
        long[] list = new long[Long.bitCount(board)];

        int i = 0;
        for (long l = 1L << 63; l != 0; l = l >>> 1) {
            if ((l & board) != 0) {
                list[i++] = l;
            }
        }
        return list;
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

    public long getLegalMoves(Owner owner) {
        if (owner.equals(Owner.BLACK)) {
            return legalMoves(blackPieces, whitePieces);
        } else {
            return legalMoves(whitePieces, blackPieces);
        }
    }

    public long getLegalMove(int color) {
        return color == 1 ? legalMoves(blackPieces, whitePieces) : legalMoves(whitePieces, blackPieces);
    }

    public int calculateScoreDifference(Owner turn) {
        if (turn.equals(Owner.BLACK)) {
            return Long.bitCount(blackPieces) - Long.bitCount(whitePieces);
        } else {
            return Long.bitCount(whitePieces) - Long.bitCount(blackPieces);
        }
    }

    public int calculateScoredifferenceForBlack() {
        return Long.bitCount(blackPieces) - Long.bitCount(whitePieces);
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

    public final void resetBoard() {
        blackPieces = placePiece(0, 3, 4);
        blackPieces = placePiece(blackPieces, 4, 3);

        whitePieces = placePiece(0, 4, 4);
        whitePieces = placePiece(whitePieces, 3, 3);

//        stablePieces = STARTING_STABLE;
    }

//    public long getStablePieces(int color) {
//        return stablePieces & (color == 1 ? blackPieces : whitePieces);
//    }
//
//    private long findStablePieces(int color) {
//        long us = color == 1 ? blackPieces : whitePieces;
//        return 0;
//    }
//
//    private long stablePiecesRight(long us) {
////        long stable = (stablePieces >>> 1) & NOT_LEFT_ONES & us;
////        stable |= (stable >>> 1) & us;
////        stable |= (stable >>> 1) & us;
////        stable |= (stable >>> 1) & us;
////        stable |= (stable >>> 1) & us;
////        stable |= (stable >>> 1) & us;
////        stable |= (stable >>> 1) & us;
////
////        return stable&us;
//        return 0;
//    }
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
        long t = them & (pos << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        return (us & (t << 8)) != 0 ? t : 0;
    }

    private static long flipDown(long pos, long us, long them) {
        long t = them & (pos >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        return (us & (t >>> 8)) != 0 ? t : 0;
    }

    private static long flipLeft(long pos, long us, long them) {
        long w = them & NOT_SIDES;
        long t = w & (pos << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        return (us & (t << 1)) != 0 ? t : 0;
    }

    private static long flipRight(long pos, long us, long them) {
        long w = them & NOT_SIDES;
        long t = w & (pos >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        return (us & (t >>> 1)) != 0 ? t : 0;
    }

    private static long flipUpLeft(long pos, long us, long them) {
        long w = them & MIDDLE;
        long t = w & (pos << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        return (us & (t << 9)) != 0 ? t : 0;
    }

    private static long flipDownRight(long pos, long us, long them) {
        long w = them & MIDDLE;
        long t = w & (pos >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        return (us & (t >>> 9)) != 0 ? t : 0;
    }

    private static long flipUpRight(long pos, long us, long them) {
        long w = them & MIDDLE;
        long t = w & (pos << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        return (us & (t << 7)) != 0 ? t : 0;
    }

    private static long flipDownLeft(long pos, long us, long them) {
        long w = them & MIDDLE;
        long t = w & (pos >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        return (us & (t >>> 7)) != 0 ? t : 0;
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
        long w = them & MIDDLE;
        long t = w & (us << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        t |= w & (t << 9);
        return (~(us | them) & (t << 9));
    }

    private static long legalMovesUpLeft(long us, long them) {
        long w = them & MIDDLE;
        long t = w & (us >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        t |= w & (t >>> 9);
        return (~(us | them) & (t >>> 9));
    }

    private static long legalMovesUpRight(long us, long them) {
        long w = them & MIDDLE;
        long t = w & (us >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        t |= w & (t >>> 7);
        return (~(us | them) & (t >>> 7));
    }

    private static long legalMovesDownLeft(long us, long them) {
        long w = them & MIDDLE;
        long t = w & (us << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        t |= w & (t << 7);
        return (~(us | them) & (t << 7));
    }

    private static long legalMovesRight(long us, long them) {
        long w = them & NOT_SIDES;
        long t = w & (us >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        t |= w & (t >>> 1);
        return (~(us | them) & (t >>> 1));
    }

    private static long legalMovesLeft(long us, long them) {
        long w = them & NOT_SIDES;
        long t = w & (us << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        t |= w & (t << 1);
        return (~(us | them) & (t << 1));
    }

    private static long legalMovesDown(long us, long them) {

        long t = them & (us >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        t |= them & (t >>> 8);
        return (~(us | them) & (t >>> 8));
    }

    private static long legalMovesUp(long us, long them) {

        long t = them & (us << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        t |= them & (t << 8);
        return (~(us | them) & (t << 8));
    }

    public static long removePieces(long board, long pieces) {
        return board & (~pieces);
    }

    private static long removePiece(long board, int x, int y) {
        long pos = ~(1L << ((x) + (8 * (y))));
        return board & pos;
    }

    private static long placePieces(long board, long pieces) {
        return board | pieces;
    }

    private static long placePiece(long board, int x, int y) {
        long pos = 1L << ((x) + (8 * (y)));
        return board | pos;
    }

    public long getEmptySquares() {
        return ~(whitePieces | blackPieces);
    }

    public long getOccupiedSquares() {
        return whitePieces | blackPieces;
    }

    public void placePiece(Owner owner, int x, int y) {
        if (owner.equals(Owner.BLACK)) {
            blackPieces = placePiece(blackPieces, x, y);
            whitePieces = removePiece(whitePieces, x, y);
        } else if (owner.equals(Owner.WHITE)) {
            whitePieces = placePiece(whitePieces, x, y);
            blackPieces = removePiece(blackPieces, x, y);
        } else {
            blackPieces = removePiece(blackPieces, x, y);
            whitePieces = removePiece(whitePieces, x, y);
        }
    }

    public Owner getPiece(int x, int y) {
        long pos = placePiece(0, x, y);

        if ((pos & blackPieces) != 0) {
            return Owner.BLACK;
        } else if ((pos & whitePieces) != 0) {
            return Owner.WHITE;
        } else {
            return Owner.NONE;
        }
    }

    public long getWhitePieces() {
        return whitePieces;
    }

    public long getBlackPieces() {
        return blackPieces;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.whitePieces ^ (this.whitePieces >>> 32));
        hash = 59 * hash + (int) (this.blackPieces ^ (this.blackPieces >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BitBoard other = (BitBoard) obj;
        if (this.whitePieces != other.whitePieces) {
            return false;
        }
        if (this.blackPieces != other.blackPieces) {
            return false;
        }
        return true;
    }
    

}
