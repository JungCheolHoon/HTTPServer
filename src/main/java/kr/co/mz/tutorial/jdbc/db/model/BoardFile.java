package kr.co.mz.tutorial.jdbc.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class BoardFile extends AbstractModel {

    private int seq;
    private int boardSeq;
    private String fileUuid;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileExtension;

    public BoardFile() {
    }

    public BoardFile(int seq, int boardSeq, String fileUuid, String fileName, String filePath, long fileSize,
        String fileExtension) {
        this.seq = seq;
        this.boardSeq = boardSeq;
        this.fileUuid = fileUuid;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public BoardFile(String fileUuid, String fileName, String filePath, long fileSize, String fileExtension) {
        this.fileUuid = fileUuid;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getBoardSeq() {
        return boardSeq;
    }

    public void setBoardSeq(int boardSeq) {
        this.boardSeq = boardSeq;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardFile boardFile = (BoardFile) o;
        return seq == boardFile.seq && Objects.equals(fileUuid, boardFile.fileUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, fileUuid);
    }

    public static BoardFile fromResultSet(ResultSet resultSet) {
        var boardFile = new BoardFile();
        try {
            boardFile.setBoardSeq(resultSet.getInt("b.seq"));
            boardFile.setFilePath(resultSet.getString("bf.file_path"));
            boardFile.setModifiedTime(resultSet.getTimestamp("bf.modified_time"));
            boardFile.setSeq(resultSet.getInt("bf.seq"));
            boardFile.setFileName(resultSet.getString("bf.file_name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boardFile;
    }
}
