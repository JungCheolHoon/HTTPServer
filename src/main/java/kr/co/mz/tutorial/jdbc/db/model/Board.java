package kr.co.mz.tutorial.jdbc.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Board extends AbstractModel {

    private int seq;
    private String title;
    private String content;
    private String customerName;
    private int customerSeq;
    private int likesCount;
    private String category;
    private Set<BoardFile> boardFileSet = new LinkedHashSet<>();

    private final Set<Comment> commentSet = new HashSet<>();

    public Board() {
    }

    public Board(String title, String content, String category, Set<BoardFile> boardFileSet) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.boardFileSet = boardFileSet;
    }

    public Board(String title, String content, int customerSeq, String category) {
        this.title = title;
        this.content = content;
        this.customerSeq = customerSeq;
        this.category = category;
    }

    public Board(int seq, String title, String content, String category) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Board(int seq, String title, String content, String customerName, int likes_count, Timestamp modified_time) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.customerName = customerName;
        this.likesCount = likes_count;
        super.modifiedTime = modified_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public Set<BoardFile> getBoardFileSet() {
        return boardFileSet;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void addBoardFile(BoardFile boardFile) {
        boardFileSet.add(boardFile);
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCustomerSeq() {
        return customerSeq;
    }

    public void setCustomerSeq(int customerSeq) {
        this.customerSeq = customerSeq;
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
        Board board = (Board) o;
        return seq == board.seq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    public static Board fromResultSet(ResultSet rs) {
        Board board = new Board();
        try {
            board.setSeq(rs.getInt("b.seq"));
            board.setTitle(rs.getString("b.title"));
            board.setContent(rs.getString("b.content"));
            board.setLikesCount(rs.getInt("b.likes_count"));
            board.setModifiedTime(rs.getTimestamp("b.modified_time"));
            board.setCategory(rs.getString("b.category"));
            board.setCustomerName(rs.getString("c.name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }
}
