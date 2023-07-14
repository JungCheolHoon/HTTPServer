package kr.co.mz.tutorial.jdbc.file;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.Part;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.exception.NoFilePermissionException;

public class FileService {

    public static final String BASIC_DIRECTORY = "/Users/mz01-junghunee/Documents/tutorial_directory/";

    public void createDirectory() {
        java.io.File fileDirectory = new java.io.File(generateDirectoryName());
        if (!fileDirectory.exists()) {
            boolean flag = fileDirectory.mkdirs();
            if (!flag) {
                System.out.println("디렉토리가 생성되지 않았습니다.");
            } else {
                System.out.println("디렉토리가 생성되었습니다.");
            }
        }
    }

    public String generateDirectoryName() {
        return BASIC_DIRECTORY + LocalDateTime.now().toLocalDate().toString().substring(0, 10);
    }

    public Set<BoardFile> upload(Collection<Part> parts, List<BoardFile> writeFileList, int writeYN)
        throws IOException {
        String uploadPath = generateDirectoryName();
        createDirectory();
        Set<BoardFile> boardFileSet = new HashSet<>();
        if (fileCount(parts) > 3) {
            return null;
        }
        for (Part part : parts) {
            Optional<String> optionalFileName = getName(part);
            String fileName;
            if (optionalFileName.isPresent() && !(fileName = optionalFileName.get()).isEmpty()) {
                String uuid = null;
                if (writeFileList != null) {
                    for (BoardFile boardFile : writeFileList) {
                        if (fileName.equals(boardFile.getFileName())) {
                            uuid = boardFile.getFileUuid();
                        }
                    }
                } else {
                    uuid = UUID.randomUUID().toString();
                }
                System.out.println(uuid);
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                String filePath = uploadPath + java.io.File.separator + uuid + "." + fileExtension;
                var boardFile = new BoardFile(uuid, fileName, filePath, part.getSize(), fileExtension);
                boardFileSet.add(boardFile);
                if (writeYN == 1) {
                    part.write(filePath);
                }
            }
        }
        return boardFileSet;
    }

    private int fileCount(Collection<Part> parts) {
        int fileCount = 0;
        for (Part part : parts) {
            Optional<String> optionalFileName = getName(part);
            if (optionalFileName.isPresent() && !optionalFileName.get().isEmpty()) {
                fileCount++;
            }
        }
        return fileCount;
    }

    private Optional<String> getName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        String fileName = null;
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                fileName = element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return Optional.ofNullable(fileName);
    }

    public boolean delete(String filePath) {
        File file = new File(filePath);
        boolean flag = false;
        try {
            if (file.exists()) {
                if (file.delete()) {
                    flag = true;
                    System.out.println("Successful deleted");
                } else {
                    System.out.println("Failed to delete file");
                }
            }
        } catch (SecurityException e) {
            System.out.println("File deletion failed because a security exception occurred");
            throw new NoFilePermissionException(e);
        }
        return flag;
    }

    public String getMimeType(String fileName) {
        Path filePath = Paths.get(fileName);
        String mimeType;
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        mimeType = fileNameMap.getContentTypeFor(filePath.toString());
        return mimeType;
    }
}
