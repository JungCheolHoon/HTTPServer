package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class CommentDao {

    private DataSource dataSource;

    public CommentDao() {
    }

    public CommentDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertOne(String content, int customerSeq, int boardSeq) throws SQLException {
        var query = "insert into board_comment(content,customer_seq,board_seq) values(?,?,?)";
        try (var connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, content);
            ps.setInt(2, customerSeq);
            ps.setInt(3, boardSeq);
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Insert One Comment : " + result);
            }
            return result;
        }
    }

    public void deleteAllFromBoardSeq(Connection connection, int boardSeq) throws SQLException {
        var query = "delete from board_comment where board_seq=?";
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Delete Comments : " + result);
            }
        }
    }
}
