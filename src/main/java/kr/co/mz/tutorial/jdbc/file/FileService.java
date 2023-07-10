package kr.co.mz.tutorial.jdbc.file;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;

public class FileService {

    public static final String BASIC_DIRECTORY = "/Users/mz01-junghunee/Documents/tutorial_directory/";

    public static void createDirectory() {
        File fileDirectory = new File(generateFileDirectoryName());
        if (!fileDirectory.exists()) {
            boolean flag = fileDirectory.mkdirs();
            if (!flag) {
                System.out.println("디렉토리가 생성되지 않았습니다.");
            } else {
                System.out.println("디렉토리가 생성되었습니다.");
            }
        }
    }

    public static String generateFileDirectoryName() {
        return BASIC_DIRECTORY + LocalDateTime.now().toLocalDate().toString().substring(0, 10);
    }

    public static Set<BoardFile> fileUpload(HttpServletRequest req) throws ServletException, IOException {
        String uploadPath = generateFileDirectoryName();
        createDirectory();
        Set<BoardFile> boardFileSet = new HashSet<>();
        for (Part part : req.getParts()) {
            var boardFile = new BoardFile();
            String fileName = getFileName(part);
            if (fileName != null && !fileName.isEmpty()) {
                String uuid = UUID.randomUUID().toString();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String filePath = uploadPath + File.separator + uuid + "." + fileExtension;
                boardFile.setFileName(fileName);
                boardFile.setFileSize(part.getSize());
                boardFile.setFileExtension(fileExtension);
                boardFile.setFilePath(filePath);
                boardFile.setFileUuid(uuid);
                boardFileSet.add(boardFile);
                part.write(filePath);
            }
        }
        return boardFileSet;
    }

    private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        boolean flag = false;
        if (file.exists()) {
            flag = file.delete();
        }
        return flag;
    }
}
