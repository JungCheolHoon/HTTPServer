package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;

public class BoardFileDao {

    private final Connection connection;

    public BoardFileDao(Connection connection) {
        this.connection = connection;
    }

    public List<String> findAllFromBoardSeq(int boardSeq) throws SQLException {
        var query = "select file_path from board_file where board_seq=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query);) {
            ps.setInt(1, boardSeq);
            ResultSet rs = ps.executeQuery();
            List<String> filePathList = new ArrayList<>();
            while (rs.next()) {
                filePathList.add(rs.getString("file_path"));
            }
            return filePathList;
        }
    }

    public List<BoardFile> findAllByBoardSeq(int boardSeq) throws SQLException {
        var query = "select * from board_file where board_seq = ?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardSeq);
            ResultSet rs = ps.executeQuery();
            List<BoardFile> insertBoardFileList = new ArrayList<>();
            while (rs.next()) {
                BoardFile boardFile = new BoardFile(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getLong(6), rs.getString(7), rs.getTimestamp(8), rs.getTimestamp(9));
                insertBoardFileList.add(boardFile);
            }
            return insertBoardFileList;
        }
    }

    public Optional<BoardFile> findOneFromFileUuid(String fileUuid) throws SQLException {
        var query = "select file_name,file_path from board_file where file_uuid = ?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
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

    public void insertOne(BoardFile boardFile) throws SQLException {
        var query = "insert into board_file(board_seq,file_uuid,file_name,file_path,file_size,file_extension) "
            + "values(?,?,?,?,?,?)";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query)) {
            ps.setInt(1, boardFile.getBoardSeq());
            ps.setString(2, boardFile.getFileUuid());
            ps.setString(3, boardFile.getFileName());
            ps.setString(4, boardFile.getFilePath());
            ps.setLong(5, boardFile.getFileSize());
            ps.setString(6, boardFile.getFileExtension());
            int result = ps.executeUpdate();
        }
    }


    public void deleteAllFromBoardSeq(int boardSeq) throws SQLException {
        var query = "delete from board_file where board_seq=?";
        System.out.println("Query : " + query);
        try (var ps = connection.prepareStatement(query);) {
            ps.setInt(1, boardSeq);
            ps.executeUpdate();
        }
    }
}
