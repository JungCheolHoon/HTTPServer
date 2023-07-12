package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;

public class BoardFileDao {

    private DataSource dataSource;

    public BoardFileDao() {
    }

    public BoardFileDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> findAllFromBoardSeq(Connection connection, int boardSeq) throws SQLException {
        var query = "select file_path from board_file where board_seq=?";
        try (
            var ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, boardSeq);
            ResultSet rs = ps.executeQuery();
            List<String> filePathList = new ArrayList<>();
            while (rs.next()) {
                filePathList.add(rs.getString("file_path"));
            }
            System.out.println("Successful Find Files! Rows : " + filePathList.size());
            return filePathList;
        }
    }

    public List<String> findAllFromBoardSeq(int boardSeq) throws SQLException {
        var query = "select file_path,file_name from board_file where board_seq=?";
        try (
            var connection = dataSource.getConnection();
            var ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, boardSeq);
            ResultSet rs = ps.executeQuery();
            List<String> filePathList = new ArrayList<>();
            while (rs.next()) {
                filePathList.add(rs.getString("file_path"));
            }
            System.out.println("Successful Find Files! Rows : " + filePathList.size());
            return filePathList;
        }
    }

    public Optional<BoardFile> findOneFromFileUuid(String fileUuid) throws SQLException {
        var query = "select file_name,file_path from board_file where file_uuid = ?";
        try (var connection = dataSource.getConnection();
            var ps = connection.prepareStatement(query)) {
            ps.setString(1, fileUuid);
            var rs = ps.executeQuery();
            BoardFile boardFile = null;
            if (rs.next()) {
                boardFile = new BoardFile();
                boardFile.setFileName(rs.getString("file_name"));
                boardFile.setFilePath(rs.getString("file_path"));
            }
            return Optional.ofNullable(boardFile);
        }
    }

    public void insertOne(Connection connection, BoardFile boardFile) throws SQLException {
        var query = "insert into board_file(board_seq,file_uuid,file_name,file_path,file_size,file_extension) "
            + "values(?,?,?,?,?,?)";
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardFile.getBoardSeq());
            ps.setString(2, boardFile.getFileUuid());
            ps.setString(3, boardFile.getFileName());
            ps.setString(4, boardFile.getFilePath());
            ps.setLong(5, boardFile.getFileSize());
            ps.setString(6, boardFile.getFileExtension());
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Insert One BoardFile! Rows : " + result);
            }
        }
    }
    

    public void deleteAllFromBoardSeq(Connection connection, int boardSeq) throws SQLException {
        var query = "delete from board_file where board_seq=?";
        try (var ps = connection.prepareStatement(query);) {
            ps.setInt(1, boardSeq);
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Delete BoardFiles! Rows : " + result);
            }
        }
    }
}
