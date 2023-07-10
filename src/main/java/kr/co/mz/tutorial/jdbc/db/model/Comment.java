package kr.co.mz.tutorial.jdbc.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Comment {

    private int seq;
    private int customerSeq;
    private String customerName;
    private int boardSeq;
    private String content;

    public Comment(int customerSeq, int boardSeq, String content) {
        this.customerSeq = customerSeq;
        this.boardSeq = boardSeq;
        this.content = content;
    }

    public Comment(int seq, int customerSeq, int boardSeq, String content, String customerName) {
        this.seq = seq;
        this.customerSeq = customerSeq;
        this.boardSeq = boardSeq;
        this.content = content;
        this.customerName = customerName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getCustomerSeq() {
        return customerSeq;
    }

    public void setCustomerSeq(int customerSeq) {
        this.customerSeq = customerSeq;
    }

    public int getBoardSeq() {
        return boardSeq;
    }

    public void setBoardSeq(int boardSeq) {
        this.boardSeq = boardSeq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return seq == comment.seq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    public static Comment fromResultSet(ResultSet rs) {
        Comment comment = null;
        try {
            comment = new Comment(rs.getInt("bc.seq"), rs.getInt("c.seq"),
                rs.getInt("b.seq"), rs.getString("bc.content"), rs.getString("c.name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }
}
