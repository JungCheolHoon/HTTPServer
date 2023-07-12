package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesDao {

    public int findOne(Connection connection, int boardSeq, int customerSeq) throws SQLException {
        var query = "select seq from board_likes where board_seq=? AND customer_seq=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ps.setInt(2, customerSeq);
            ResultSet rs = ps.executeQuery();
            int primaryKey = 0;
            if (rs.next()) {
                primaryKey = rs.getInt(1);
                System.out.println("Successful Find One Likes! PK : " + primaryKey);
            }
            return primaryKey;
        }
    }

    public void insertOne(Connection connection, int boardSeq, int customerSeq) throws SQLException {
        var query = "insert into board_likes(board_seq,customer_seq) values(?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ps.setInt(2, customerSeq);
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Insert One Likes! Rows : " + result);
            }
        }
    }

    public void deleteOne(Connection connection, int primaryKey) throws SQLException {
        var query = "delete from board_likes where seq=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, primaryKey);
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Delete One Likes! Rows : " + result);
            }
        }
    }

}
