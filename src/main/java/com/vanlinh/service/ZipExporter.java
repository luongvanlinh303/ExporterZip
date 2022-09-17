package com.vanlinh.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipExporter {
    static final String FILE_LOCATION = new File("static\\file_to_zip").getAbsolutePath() + "\\";

    public static String genStringForDirectory() {
        return UUID.randomUUID().toString();
    }

    public static void copyFile(String from, String to) {
        File srcFile = new File(from);
        File destDir = new File(to);
        try {
            FileUtils.copyFileToDirectory(srcFile, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFolder(Path source) throws IOException {
        String zipFileName = source.getFileName().toString() + ".zip";
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            Files.walkFileTree(source, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }
                    try (FileInputStream fis = new FileInputStream(file.toFile())) {
                        Path targetFile = source.relativize(file);
                        zos.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                        System.out.printf("Zip file : %s%n", file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("Unable to zip : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public void export(String fileJson, String fileImage) throws IOException {
        String dir = FILE_LOCATION + genStringForDirectory() + "\\";
        File file = new File(dir);
        copyFile(fileJson, file.toString());
        copyFile(fileImage, file.toString());
        Path source = Paths.get(file.toString());
        if (!Files.isDirectory(source)) {
            return;
        }
        try {
            zipFolder(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
