package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChessPosition)) {
            return false;
        }
        ChessPosition other = (ChessPosition) o;
        return this.row == other.row && this.col == other.col;
    }

    public int hashCode() {
        return row * 8 + col;
    }

    public String toString() {
        return "ChessPosition(" + row + ", " + col + ")";
    }

    /**
     * @return Which team this position belongs to
     */

    public ChessGame.TeamColor getTeamColor() {
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                return ChessGame.TeamColor.WHITE;
            } else {
                return ChessGame.TeamColor.BLACK;
            }
        } else {
            if (col % 2 == 0) {
                return ChessGame.TeamColor.BLACK;
            } else {
                return ChessGame.TeamColor.WHITE;
            }
        }
    }
}