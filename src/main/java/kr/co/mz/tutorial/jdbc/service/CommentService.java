package kr.co.mz.tutorial.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import kr.co.mz.tutorial.jdbc.db.dao.CommentDao;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;

public class CommentService {

    private final Connection connection;

    public CommentService(Connection connection) {
        this.connection = connection;
    }

    public int insertComment(String content, int customerSeq, int boardSeq) {
        try {
            return new CommentDao(connection).insertOne(content, customerSeq, boardSeq);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public int deleteComment(int commentSeq) {
        return new CommentDao(connection).deleteBySeq(commentSeq);
    }
}
