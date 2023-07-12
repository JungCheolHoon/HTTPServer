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

public class BoardDao {

    private final DataSource dataSource;
    private final BoardFileDao boardFileDao = new BoardFileDao();
    private final CommentDao commentDao = new CommentDao();
    private final LikesDao likesDao = new LikesDao();

    public BoardDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Board> findOne(int boardSeq) throws SQLException {
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
                    System.out.println("Successful Find One Board! PK :  " + board.getSeq());
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

    public List<Board> findAll() throws SQLException {
        var query = "select * from Board b left join customer c on b.customer_seq = c.seq ";
        try (var connection = dataSource.getConnection();
            var ps = connection.prepareStatement(query)) {
            var rs = ps.executeQuery();
            var boardList = new ArrayList<Board>();
            while (rs.next()) {
                var board = Board.fromResultSet(rs);
                boardList.add(board);
            }
            int rows = boardList.size();
            if (rows != 0) {
                System.out.println("Successful Find All Board! Rows : " + rows);
            }
            return boardList;
        }
    }

    public int insertOne(Board board) throws SQLException {
        var query = "insert into board(title,content,customer_seq,category) values(?,?,?,?)";
        Connection connection = null;
        var boardSeq = 0;
        try {
            connection = dataSource.getConnection();
            try (var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                connection.setAutoCommit(false);
                ps.setString(1, board.getTitle());
                ps.setString(2, board.getContent());
                ps.setInt(3, board.getCustomerSeq());
                ps.setString(4, board.getCategory());
                int result = ps.executeUpdate();
                if (result != 0) {
                    System.out.println("Successful Insert One Board! Rows : " + result);
                }
                var primaryKey = ps.getGeneratedKeys();
                if (primaryKey.next()) {
                    boardSeq = primaryKey.getInt(1);
                }
                for (BoardFile boardFile : board.getBoardFileSet()) {
                    boardFile.setBoardSeq(boardSeq);
                    boardFileDao.insertOne(connection, boardFile);
                }
            }
            connection.commit();
            System.out.println("Successful Transaction Commit!");
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

    // TODO
    public Optional<List<String>> updateOne(Board board) {
        var query = "update board set title=?, content=?, category=? where seq=?";
        Connection connection = null;
        List<String> filePathList = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try (var ps = connection.prepareStatement(query)) {
                ps.setString(1, board.getTitle());
                ps.setString(2, board.getContent());
                ps.setString(3, board.getCategory());
                ps.setInt(4, board.getSeq());
                int result = ps.executeUpdate();
                System.out.println("Successful Update One Board! Rows : " + result);
                filePathList = boardFileDao.findAllFromBoardSeq(connection, board.getSeq());
                boardFileDao.deleteAllFromBoardSeq(connection, board.getSeq());
                for (BoardFile boardFile : board.getBoardFileSet()) {
                    boardFile.setBoardSeq(board.getSeq());
                    boardFileDao.insertOne(connection, boardFile);
                }
            }
            connection.commit();
        } catch (SQLException sqle) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Successful Transaction Rollback!");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
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
        return Optional.ofNullable(filePathList);
    }

    public Optional<List<String>> deleteOne(int boardSeq) {
        var query = "delete from board where seq=?";
        Connection connection = null;
        int result = 0;
        List<String> filePathList = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try (
                var ps = connection.prepareStatement(query)
            ) {
                filePathList = boardFileDao.findAllFromBoardSeq(connection, boardSeq);
                boardFileDao.deleteAllFromBoardSeq(connection, boardSeq);
                commentDao.deleteAllFromBoardSeq(connection, boardSeq);
                ps.setInt(1, boardSeq);
                result = ps.executeUpdate();
                if (result != 0) {
                    System.out.println("Successful Delete One Board! Rows : " + result);
                }
            }
            connection.commit();
            System.out.println("Successful Transaction Commit!");
        } catch (SQLException sqle) {
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Successful Transaction Rollback!");
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
        return Optional.ofNullable(filePathList);
    }

    public int updateOneOfLikesCount(int boardSeq, int customerSeq) {
        var updateAddLikesQuery = "update board set likes_count=likes_count+1 where seq=?";
        var updateMinusLikesQuery = "update board set likes_count=likes_count-1 where seq=?";
        int result = 0;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try (
                PreparedStatement psAddCount = connection.prepareStatement(updateAddLikesQuery);
                PreparedStatement psMinusCount = connection.prepareStatement(updateMinusLikesQuery)
            ) {
                int likesPrimaryKey = likesDao.findOne(connection, boardSeq, customerSeq);
                if (likesPrimaryKey != 0) {
                    likesDao.deleteOne(connection, likesPrimaryKey);
                    psMinusCount.setInt(1, boardSeq);
                    result = psMinusCount.executeUpdate();
                    if (result != 0) {
                        System.out.println("Successful Add One Likes Count In Board! Rows : " + result);
                    }
                } else {
                    System.out.println("Successful Not Find One Likes : " + likesPrimaryKey);
                    likesDao.insertOne(connection, boardSeq, customerSeq);
                    psAddCount.setInt(1, boardSeq);
                    result = psAddCount.executeUpdate();
                    if (result != 0) {
                        System.out.println("Successful Subtract One LikesCount In Board! Rows : " + result);
                    }
                }
            }
            connection.commit();
            System.out.println("Successful Transaction Commit!");
        } catch (SQLException sqle) {
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Successful Transaction Rollback!");
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
