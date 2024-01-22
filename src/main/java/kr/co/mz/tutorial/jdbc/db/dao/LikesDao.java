package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesDao {

    private final Connection connection;

    public LikesDao(Connection connection) {
        this.connection = connection;
    }

    public int findOne(int boardSeq, int customerSeq) throws SQLException {
        var query = "select seq from board_likes where board_seq=? AND customer_seq=?";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ps.setInt(2, customerSeq);
            ResultSet rs = ps.executeQuery();
            int primaryKey = 0;
            if (rs.next()) {
                primaryKey = rs.getInt(1);
                System.out.println("Successful Find One Likes! Query : " + query);
            }
            return primaryKey;
        }
    }

    public int insertOne(int boardSeq, int customerSeq) throws SQLException {
        var query = "insert into board_likes(board_seq,customer_seq) values(?,?)";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ps.setInt(2, customerSeq);
            return ps.executeUpdate();
        }
    }

    public int deleteOne(int primaryKey) throws SQLException {
        var query = "delete from board_likes where seq=?";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, primaryKey);
            return ps.executeUpdate();
        }
    }

}
