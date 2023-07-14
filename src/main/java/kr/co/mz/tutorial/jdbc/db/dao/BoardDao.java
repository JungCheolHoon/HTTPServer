package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.db.model.Comment;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;

public class BoardDao {

    private final Connection connection;

    public BoardDao(Connection connection) {
        this.connection = connection;
    }

    public List<Board> findAll() {
        var query = "select * from Board b left join customer c on b.customer_seq = c.seq ";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            var rs = ps.executeQuery();
            var boardList = new ArrayList<Board>();
            while (rs.next()) {
                var board = Board.fromResultSet(rs);
                boardList.add(board);
            }
            return boardList;
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public List<Board> findAny(String category) {
        var query = "select * from Board b left join customer c on b.customer_seq = c.seq where category=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setString(1, category);
            var rs = ps.executeQuery();
            var boardList = new ArrayList<Board>();
            while (rs.next()) {
                var board = Board.fromResultSet(rs);
                boardList.add(board);
            }
            return boardList;
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public Optional<Board> findOne(int boardSeq) {
        var query = "select * from board b "
            + "left join customer c on b.customer_seq = c.seq "
            + "left join Board_file bf on b.seq = bf.board_seq "
            + "left join Board_comment bc on b.seq = bc.board_seq where b.seq = ?";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public int insertOne(Board board) {
        var query = "insert into board(title,content,customer_seq,category) values(?,?,?,?)";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContent());
            ps.setInt(3, board.getCustomerSeq());
            ps.setString(4, board.getCategory());
            ps.executeUpdate();
            var primaryKey = ps.getGeneratedKeys();
            int boardSeq = 0;
            if (primaryKey.next()) {
                boardSeq = primaryKey.getInt(1);
            }
            return boardSeq;
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public void updateOne(Board board) {
        var query = "update board set title=?, content=?, category=? where seq=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContent());
            ps.setString(3, board.getCategory());
            ps.setInt(4, board.getSeq());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public void deleteOne(int boardSeq) {
        var query = "delete from board where seq=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            var result = ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public int updateOneOfLikesCount(int boardSeq, int exist) {
        var updateAddLikesQuery = "update board set likes_count=likes_count+1 where seq=?";
        var updateMinusLikesQuery = "update board set likes_count=likes_count-1 where seq=?";
        try (
            PreparedStatement psAddCount = connection.prepareStatement(updateAddLikesQuery);
            PreparedStatement psMinusCount = connection.prepareStatement(updateMinusLikesQuery)
        ) {
            int result;
            if (exist != 0) {
                psMinusCount.setInt(1, boardSeq);
                System.out.println("Query : " + updateMinusLikesQuery);
                result = psMinusCount.executeUpdate();
            } else {
                psAddCount.setInt(1, boardSeq);
                System.out.println("Query : " + updateAddLikesQuery);
                result = psAddCount.executeUpdate();
            }
            return result;
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }
}
