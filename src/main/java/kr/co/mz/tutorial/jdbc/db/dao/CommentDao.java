package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;

public class CommentDao {

    private final Connection connection;

    public CommentDao(Connection connection) {
        this.connection = connection;
    }

    public int insertOne(String content, int customerSeq, int boardSeq) throws SQLException {
        var query = "insert into board_comment(content,customer_seq,board_seq) values(?,?,?)";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, content);
            ps.setInt(2, customerSeq);
            ps.setInt(3, boardSeq);
            return ps.executeUpdate();
        }
    }

    public void deleteAllByBoardSeq(int boardSeq) throws SQLException {
        var query = "delete from board_comment where board_seq=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ps.executeUpdate();
        }
    }

    public int deleteBySeq(int commentSeq) {
        var query = "delete from board_comment where seq = ?";
        System.out.println(query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, commentSeq);
            return ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }
}
