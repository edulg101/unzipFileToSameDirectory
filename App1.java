import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

public class App {

    public static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {

        String zippedFilePath = "/home/eduardo/Downloads/cro";
        String destPath = "/home/eduardo/Downloads/unziped/";

        try {
            unzipAllDirectory(zippedFilePath, destPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String[] getFilesInPath(String path) throws Exception {
        File file = null;
        String[] files = null;

        try {
            file = new File(path);
            files = file.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void unzipAllDirectory(String path, String destPath) throws IOException {
        String[] files = null;
        try {
            files = getFilesInPath(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < files.length; i++) {
            unzip(path + File.separator + files[i], destPath);
        }
    }

    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        Charset charset = Charset.forName("ISO-8859-1");
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath), charset);
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {

            String filePath = destDirectory + File.separator + entry.getName();
            System.out.println("entry.getname: " + entry.getName());
            String fileName = entry.getName();
            
            if (fileName.contains(File.separator)) {
                int index = fileName.indexOf(File.separator);
                fileName = fileName.substring(index);
                filePath = destDirectory + fileName;
            }

            extractFile(zipIn, filePath);

            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File parentFile = new File(filePath).getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
