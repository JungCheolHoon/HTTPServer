package kr.co.mz.tutorial.jdbc.db.dao.init;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import kr.co.mz.tutorial.jdbc.db.HikariPoolFactory;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class CreateBoardFile {

    private static final String QUERY = """
        insert into board_file(board_seq,file_uuid,file_name,file_path,file_size,file_extension) 
        values(?,?,?,?,?,?)""";

    public static void main(String[] args) throws SQLException, IOException {
        var dataSource = HikariPoolFactory.createHikariDataSource();
        System.out.println("쿼리 성공한 행수 : " + createBoardFile(
                dataSource.getConnection(),
                new BoardFile(UUID.randomUUID().toString(), "감나비부리박기.txt",
                    FileService.BASIC_DIRECTORY + FileService.generateDirectoryName() + "/감나비잔나비.txt",
                    999, "txt"
                )
            )
        );
    }

    public static int createBoardFile(Connection connection, BoardFile boardFile)
        throws SQLException {
        String fileDirectoryName =
            FileService.generateDirectoryName() + java.io.File.separator + boardFile.getFileName();
        try (
            var preparedStatement = connection.prepareStatement(QUERY);
        ) {
            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, UUID.randomUUID().toString());
            preparedStatement.setString(4, boardFile.getFileName());
            preparedStatement.setString(3, fileDirectoryName);
            preparedStatement.setLong(5, boardFile.getFileSize());
            preparedStatement.setString(6, boardFile.getFileExtension());
            return preparedStatement.executeUpdate();
        }
    }
}
