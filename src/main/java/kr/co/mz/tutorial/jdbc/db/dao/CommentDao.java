package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class CommentDao {

    private final DataSource dataSource;

    public CommentDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertComment(String content, int customerSeq, int boardSeq) throws SQLException {
        var query = "insert into board_comment(content,customer_seq,board_seq) values(?,?,?)";
        try (var connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, content);
            ps.setInt(2, customerSeq);
            ps.setInt(3, boardSeq);
            return ps.executeUpdate();
        }
    }
}
