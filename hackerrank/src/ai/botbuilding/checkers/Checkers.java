package ai.botbuilding.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ai.botbuilding.checkers.Checkers.Colors.BLACK;
import static ai.botbuilding.checkers.Checkers.Colors.WHITE;
import static ai.botbuilding.checkers.Checkers.MoveType.CAPTURE;
import static ai.botbuilding.checkers.Checkers.MoveType.NON_CAPTURE;
import static ai.botbuilding.checkers.Checkers.Types.KING;
import static ai.botbuilding.checkers.Checkers.Types.REG;

public class Checkers {
    public enum Types {
        REG, KING
    }

    public enum Colors {
        BLACK, WHITE;

        public static Colors getColor(char c) {
            if (c == 'b') {
                return BLACK;
            } else {
                return WHITE;
            }
        }
    }

    public enum MoveType {
        CAPTURE, NON_CAPTURE
    }

    private static class Checker {
        private Types type;
        private Colors color;

        public Checker(final Types type, final Colors color) {
            this.type = type;
            this.color = color;
        }
    }

    private static class BoardCell {
        private int row;
        private int col;

        public BoardCell(final int row, final int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class Move {
        private MoveType moveType;
        private BoardCell initialPosition;
        private List<BoardCell> targetPositions = new ArrayList<>();

        public Move(final MoveType moveType, final BoardCell initialPosition, final BoardCell targetPosition) {
            this.moveType = moveType;
            this.initialPosition = initialPosition;
            targetPositions.add(targetPosition);
        }

        public Move(final MoveType moveType) {
            this.moveType = moveType;
        }
    }

    private static class CheckerPosition {
        private Checker checker;
        private BoardCell cell;

        public CheckerPosition(final Checker checker, final BoardCell cell) {
            this.checker = checker;
            this.cell = cell;
        }

        public boolean canCapture(final Board board) {
            if (checker.color == BLACK) {
                boolean canCapture = canCaptureRightDown(board, WHITE) || canCaptureLeftDown(board, WHITE);

                if (checker.type == KING) {
                    canCapture = canCapture || canCaptureRightUp(board, WHITE) || canCaptureLeftUp(board, WHITE);
                }

                return canCapture;
            } else {
                boolean canCapture = canCaptureRightUp(board, BLACK) || canCaptureLeftUp(board, BLACK);

                if (checker.type == KING) {
                    canCapture = canCapture || canCaptureRightDown(board, BLACK) || canCaptureLeftDown(board, BLACK);
                }

                return canCapture;
            }
        }

        public List<Move> generateNonCaptureMoves(final Board board) {
            List<Move> moves = new ArrayList<>();

            if (checker.color == BLACK) {
                if (canMoveRightDown(board)) {
                    moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row + 1, cell.col + 1)));
                }

                if (canMoveLeftDown(board)) {
                    moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row + 1, cell.col - 1)));
                }

                if (checker.type == KING) {
                    if (canMoveRightUp(board)) {
                        moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row - 1, cell.col + 1)));
                    }

                    if (canMoveLeftUp(board)) {
                        moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row - 1, cell.col - 1)));
                    }
                }
            } else {
                if (canMoveRightUp(board)) {
                    moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row - 1, cell.col + 1)));
                }

                if (canMoveLeftUp(board)) {
                    moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row - 1, cell.col - 1)));
                }

                if (checker.type == KING) {
                    if (canMoveRightDown(board)) {
                        moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row + 1, cell.col + 1)));
                    }

                    if (canMoveLeftDown(board)) {
                        moves.add(new Move(NON_CAPTURE, cell, new BoardCell(cell.row + 1, cell.col - 1)));
                    }
                }
            }

            return moves;
        }

        private List<Move> generateCaptureMoves(final Board board) {
            return generateCaptureMoves(board, false, new ArrayList<>());
        }

        private List<Move> generateCaptureMoves(Board board, boolean canCaptureBack, List<BoardCell> positions) {
            positions.add(new BoardCell(cell.row, cell.col));

            List<Move> moves = new ArrayList<>();

            if (checker.color == BLACK) {
                if (canCaptureRightDown(board, WHITE)) {
                    CheckerPosition toRemove = moveChecker(board, 1, 1);
                    moves.addAll(generateCaptureMoves(board, true, positions));
                    restoreChecker(board, toRemove, 1, 1);
                }

                if (canCaptureLeftDown(board, WHITE)) {
                    CheckerPosition toRemove = moveChecker(board, 1, -1);
                    moves.addAll(generateCaptureMoves(board, true, positions));
                    restoreChecker(board, toRemove, 1, -1);
                }

                if (checker.type == KING || canCaptureBack) {
                    if (canCaptureRightUp(board, WHITE)) {
                        CheckerPosition toRemove = moveChecker(board, -1, 1);
                        moves.addAll(generateCaptureMoves(board, true, positions));
                        restoreChecker(board, toRemove, -1, 1);
                    }

                    if (canCaptureLeftUp(board, WHITE)) {
                        CheckerPosition toRemove = moveChecker(board, -1, -1);
                        moves.addAll(generateCaptureMoves(board, true, positions));
                        restoreChecker(board, toRemove, -1, -1);
                    }
                }
            } else {
                if (canCaptureRightUp(board, BLACK)) {
                    CheckerPosition toRemove = moveChecker(board, -1, 1);
                    moves.addAll(generateCaptureMoves(board, true, positions));
                    restoreChecker(board, toRemove, -1, 1);
                }

                if (canCaptureLeftUp(board, BLACK)) {
                    CheckerPosition toRemove = moveChecker(board, -1, -1);
                    moves.addAll(generateCaptureMoves(board, true, positions));
                    restoreChecker(board, toRemove, -1, -1);
                }

                if (checker.type == KING || canCaptureBack) {
                    if (canCaptureRightDown(board, BLACK)) {
                        CheckerPosition toRemove = moveChecker(board, 1, 1);
                        moves.addAll(generateCaptureMoves(board, true, positions));
                        restoreChecker(board, toRemove, 1, 1);
                    }

                    if (canCaptureLeftDown(board, BLACK)) {
                        CheckerPosition toRemove = moveChecker(board, 1, -1);
                        moves.addAll(generateCaptureMoves(board, true, positions));
                        restoreChecker(board, toRemove, 1, -1);
                    }
                }
            }

            if (moves.isEmpty()) {
                Move move = new Move(CAPTURE);
                moves.add(move);
                boolean isFirst = true;

                for (BoardCell pos : positions) {
                    if (isFirst) {
                        move.initialPosition = pos;
                        isFirst = false;
                    } else {
                        move.targetPositions.add(pos);
                    }
                }
            }

            positions.remove(positions.size() - 1);

            return moves;
        }

        //rowDir = -1 up
        //rowDir = 1 down

        //colDir = -1 left
        //colDir = 1 right
        private CheckerPosition moveChecker(Board board, int rowDir, int colDir) {
            CheckerPosition toRemove = board.board[cell.row + rowDir * 1][cell.col + colDir * 1];
            board.whites.remove(toRemove);
            board.board[cell.row][cell.col] = null;
            board.board[cell.row + rowDir * 1][cell.col + colDir * 1] = null;
            board.board[cell.row + rowDir * 2][cell.col + colDir * 2] = this;
            cell.col += colDir * 2;
            cell.row += rowDir * 2;

            return toRemove;
        }

        private void restoreChecker(Board board, CheckerPosition removed, int rowDir, int colDir) {
            cell.col -= colDir * 2;
            cell.row -= rowDir * 2;
            board.board[cell.row][cell.col] = this;
            board.board[cell.row + rowDir * 1][cell.col + colDir * 1] = removed;
            board.board[cell.row + rowDir * 2][cell.col + colDir * 2] = null;
            board.whites.add(removed);
        }

        private boolean canMoveRightDown(final Board board) {
            return cell.row + 1 < board.side && cell.col + 1 < board.side
                    && null == board.board[cell.row + 1][cell.col + 1];
        }

        private boolean canMoveLeftDown(final Board board) {
            return cell.row + 1 < board.side && cell.col - 1 >= 0
                    && null == board.board[cell.row + 1][cell.col - 1];
        }

        private boolean canMoveRightUp(final Board board) {
            return cell.row - 1 >= 0 && cell.col + 1 < board.side
                    && null == board.board[cell.row - 1][cell.col + 1];
        }

        private boolean canMoveLeftUp(final Board board) {
            return cell.row - 1 >= 0 && cell.col - 1 >= 0
                    && null == board.board[cell.row - 1][cell.col - 1];
        }

        private boolean canCaptureRightDown(final Board board, final Colors colorToCapture) {
            return cell.row + 1 < board.side && cell.col + 1 < board.side
                    && null != board.board[cell.row + 1][cell.col + 1]
                    && board.board[cell.row + 1][cell.col + 1].checker.color == colorToCapture
                    && cell.row + 2 < board.side && cell.col + 2 < board.side
                    && null == board.board[cell.row + 2][cell.col + 2];
        }

        private boolean canCaptureLeftDown(final Board board, final Colors colorToCapture) {
            return cell.row + 1 < board.side && cell.col - 1 >= 0
                    && null != board.board[cell.row + 1][cell.col - 1]
                    && board.board[cell.row + 1][cell.col - 1].checker.color == colorToCapture
                    && cell.row + 2 < board.side && cell.col - 2 >= 0
                    && null == board.board[cell.row + 2][cell.col - 2];
        }

        private boolean canCaptureRightUp(final Board board, final Colors colorToCapture) {
            return cell.row - 1 >= 0 && cell.col + 1 < board.side
                    && null != board.board[cell.row - 1][cell.col + 1]
                    && board.board[cell.row - 1][cell.col + 1].checker.color == colorToCapture
                    && cell.row - 2 >= 0 && cell.col + 2 < board.side
                    && null == board.board[cell.row - 2][cell.col + 2];
        }

        private boolean canCaptureLeftUp(final Board board, final Colors colorToCapture) {
            return cell.row - 1 >= 0 && cell.col - 1 >= 0
                    && null != board.board[cell.row - 1][cell.col - 1]
                    && board.board[cell.row - 1][cell.col - 1].checker.color == colorToCapture
                    && cell.row - 2 >= 0 && cell.col - 2 >= 0
                    && null == board.board[cell.row - 2][cell.col - 2];
        }
    }

    private static class Board {
        private CheckerPosition[][] board;
        private int side;
        private Set<CheckerPosition> blacks = new HashSet<>();
        private Set<CheckerPosition> whites = new HashSet<>();

        public Board(final int side, final char[][] positions) {
            this.side = side;
            this.board = new CheckerPosition[side][side];

            for (int row = 0; row < side; row++) {
                for (int col = 0; col < side; col++) {
                    switch (positions[row][col]) {
                        case 'w':
                            board[row][col] = new CheckerPosition(new Checker(REG, WHITE), new BoardCell(row, col));
                            whites.add(board[row][col]);
                            break;
                        case 'b':
                            board[row][col] = new CheckerPosition(new Checker(REG, BLACK), new BoardCell(row, col));
                            blacks.add(board[row][col]);
                            break;
                        case 'W':
                            board[row][col] = new CheckerPosition(new Checker(KING, WHITE), new BoardCell(row, col));
                            whites.add(board[row][col]);
                            break;
                        case 'B':
                            board[row][col] = new CheckerPosition(new Checker(KING, BLACK), new BoardCell(row, col));
                            blacks.add(board[row][col]);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        public List<Move> generateAllMoves(final Colors colorToMove) {
            List<Move> moves = new ArrayList<>();
            List<CheckerPosition> capturePositions = new ArrayList<>();
            List<CheckerPosition> nonCapturePositions = new ArrayList<>();

            Set<CheckerPosition> positionsToProc = colorToMove == WHITE ? whites : blacks;

            for (CheckerPosition pos : positionsToProc) {
                if (pos.canCapture(this)) {
                    capturePositions.add(pos);
                } else {
                    nonCapturePositions.add(pos);
                }
            }

            if (!capturePositions.isEmpty()) {
                for (CheckerPosition pos : capturePositions) {
                    moves.addAll(pos.generateCaptureMoves(this));
                }
            } else {
                for (CheckerPosition pos : nonCapturePositions) {
                    moves.addAll(pos.generateNonCaptureMoves(this));
                }
            }

            return moves;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char moveColor = br.readLine().charAt(0);
        int side = Integer.parseInt(br.readLine());

        char[][] inptBoard = new char[side][side];
        for (int row = 0; row < side; row++) {
            inptBoard[row] = br.readLine().toCharArray();
        }

        Board board = new Board(side, inptBoard);

        List<Move> moves = board.generateAllMoves(Colors.getColor(moveColor));

        for (Move move : moves) {
           for (BoardCell pos : move.targetPositions) {
               System.out.printf("%s %s\n", pos.row, pos.col);
           }
           System.out.println("===============");
        }

        System.out.println();
    }
}
