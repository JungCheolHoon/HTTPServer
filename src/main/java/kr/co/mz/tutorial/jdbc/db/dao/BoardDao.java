package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.db.model.Comment;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class BoardDao {

    private final DataSource dataSource;

    public BoardDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Board> getBoard(int boardSeq) throws SQLException {
        var query = "select * from board b "
            + "left join customer c on b.customer_seq = c.seq "
            + "left join Board_file bf on b.seq = bf.board_seq "
            + "left join Board_comment bc on b.seq = bc.board_seq where b.seq = ?";
        try (var connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ResultSet rs = ps.executeQuery();
            Board board = null;
            while (rs.next()) {
                if (board == null) {
                    board = Board.fromResultSet(rs);
                }
                var boardFile = BoardFile.fromResultSet(rs);
                var boardComment = Comment.fromResultSet(rs);
                if (rs.getInt("bf.seq") != 0) {
                    board.getBoardFileSet().add(boardFile);
                }
                if (rs.getInt("bc.seq") != 0) {
                    board.getCommentSet().add(boardComment);
                }
            }
            return Optional.ofNullable(board);
        }
    }

    public List<Board> getBoardList() throws SQLException {
        var query = "select * from Board b left join customer c on b.customer_seq = c.seq ";
        try (var connection = dataSource.getConnection();
            var ps = connection.prepareStatement(query)) {
            var rs = ps.executeQuery();
            var boardList = new ArrayList<Board>();
            while (rs.next()) {
                var board = Board.fromResultSet(rs);
                boardList.add(board);
            }
            return boardList;
        }
    }

    public int insertBoard(Board board) throws SQLException {
        var boardQuery = "insert into board(title,content,customer_seq,category) values(?,?,?,?)";
        var boardFileQuery = "insert into board_file(board_seq,file_uuid,file_name,file_path,file_size,file_extension) "
            + "values(?,?,?,?,?,?)";
        Connection connection = null;
        var boardSeq = 0;
        try {
            connection = dataSource.getConnection();
            try (
                var psBoard = connection.prepareStatement(boardQuery, Statement.RETURN_GENERATED_KEYS);
                var psBoardFile = connection.prepareStatement(boardFileQuery)) {
                connection.setAutoCommit(false);
                psBoard.setString(1, board.getTitle());
                psBoard.setString(2, board.getContent());
                psBoard.setInt(3, board.getCustomerSeq());
                psBoard.setString(4, board.getCategory());
                psBoard.executeUpdate();
                var result = psBoard.getGeneratedKeys();
                if (result.next()) {
                    boardSeq = result.getInt(1);
                }
                for (BoardFile boardFile : board.getBoardFileSet()) {
                    psBoardFile.setInt(1, boardSeq);
                    psBoardFile.setString(2, boardFile.getFileUuid());
                    psBoardFile.setString(3, boardFile.getFileName());
                    psBoardFile.setString(4, boardFile.getFilePath());
                    psBoardFile.setLong(5, boardFile.getFileSize());
                    psBoardFile.setString(6, boardFile.getFileExtension());
                    psBoardFile.executeUpdate();
                }
                connection.commit();
            }
        } catch (SQLException sqle) {
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return boardSeq;
    }

    public int updateBoard(Board board) {
        var boardQuery = "update board set title=?, content=?, category=? where seq=?";
        Connection connection = null;
        int result = 0;
        try {
            connection = dataSource.getConnection();
            try (var ps = connection.prepareStatement(boardQuery)) {
                ps.setString(1, board.getTitle());
                ps.setString(2, board.getContent());
                ps.setString(3, board.getCategory());
                ps.setInt(4, board.getSeq());
                result = ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int deleteBoard(int boardSeq) {
        var deleteBoardQuery = "delete from board where seq=?";
        var selectBoardFileQuery = "select file_path from board_file where board_seq=?";
        var deleteBoardFileQuery = "delete from board_file where board_seq=?";
        var deleteCommentQuery = "delete from board_comment where board_seq=?";
        Connection connection = null;
        int result = 0;
        try {
            connection = dataSource.getConnection();
            try (var psBoard = connection.prepareStatement(deleteBoardQuery);
                var psSelectBoardFile = connection.prepareStatement(selectBoardFileQuery);
                var psBoardFile = connection.prepareStatement(deleteBoardFileQuery);
                var psBoardComment = connection.prepareStatement(deleteCommentQuery)) {
                connection.setAutoCommit(false);
                psSelectBoardFile.setInt(1, boardSeq);
                ResultSet resultSet = psSelectBoardFile.executeQuery();
                while (resultSet.next()) {
                    var flag = FileService.deleteFile(resultSet.getString(1));
                    if (flag) {
                        System.out.println("파일이 삭제되었습니다.");
                    }
                }
                psBoardFile.setInt(1, boardSeq);
                psBoardFile.executeUpdate();
                psBoardComment.setInt(1, boardSeq);
                psBoardComment.executeUpdate();
                psBoard.setInt(1, boardSeq);
                result = psBoard.executeUpdate();
                connection.commit();
            }
        } catch (SQLException sqle) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int likesCount(int boardSeq, int CustomerSeq) {
        var selectQuery = "select seq from board_likes where board_seq=? AND customer_seq=?";
        var deleteQuery = "delete from board_likes where seq=?";
        var insertQuery = "insert into board_likes(board_seq,customer_seq) values(?,?)";
        var updateAddLikesQuery = "update board set likes_count=likes_count+1 where seq=?";
        var updateMinusLikesQuery = "update board set likes_count=likes_count-1 where seq=?";
        int result = 0;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try (
                PreparedStatement psSelect = connection.prepareStatement(selectQuery);
                PreparedStatement psDelete = connection.prepareStatement(deleteQuery);
                PreparedStatement psInsertLikes = connection.prepareStatement(insertQuery);
                PreparedStatement psAddCount = connection.prepareStatement(updateAddLikesQuery);
                PreparedStatement psMinusCount = connection.prepareStatement(updateMinusLikesQuery)) {
                connection.setAutoCommit(false);
                psSelect.setInt(1, boardSeq);
                psSelect.setInt(2, CustomerSeq);
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    psDelete.setInt(1, rs.getInt(1));
                    result = psDelete.executeUpdate();
                    psMinusCount.setInt(1, boardSeq);
                    psMinusCount.executeUpdate();
                } else {
                    psInsertLikes.setInt(1, boardSeq);
                    psInsertLikes.setInt(2, CustomerSeq);
                    result = psInsertLikes.executeUpdate();
                    psAddCount.setInt(1, boardSeq);
                    psAddCount.executeUpdate();
                }
                connection.commit();
            }
        } catch (SQLException sqle) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
