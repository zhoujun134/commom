package com.zj.common.file;

import com.zj.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class FileCopyWithHierarchy {

    public static void main(String[] args) {
        // 示例用法
        String sourcePath = "/Desktop/tengxun-oss/zbus/blog202406040032944.webp";
        String targetFolder = "/Users/zj/Desktop/tengxun-oss2";
        String basePath = "/Users/zj/Desktop/tengxun-oss";

        try {
            String[] split = sourcePath.split("/Desktop/tengxun-oss");
            if (split.length != 2) {
                return;
            }
            String endPath = split[1];
            String newPath = targetFolder + endPath;
            log.info("{}", JsonUtil.toJSONString(split));
            Path target = Paths.get(newPath).toAbsolutePath();
            log.info("父级目录: {}", target.getParent());
            // 创建所有必要的父目录
            Files.createDirectories(target.getParent());
            // 复制文件
            Path source = Paths.get(sourcePath).toAbsolutePath();
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("复制文件时出错: " + e.getMessage());
        }
    }

    public static void copyFile(String sourcePath, String sourceDirPath, String targetDirPath) {
        try {
            String[] split = sourcePath.split(sourceDirPath);
            if (split.length != 2) {
                return;
            }
            String endPath = split[1];
            String newPath = targetDirPath + endPath;
            log.info("当前新的目录路径为: {}", JsonUtil.toJSONString(split));
            Path target = Paths.get(newPath).toAbsolutePath();
            log.info("父级目录: {}", target.getParent());
            // 创建所有必要的父目录
            Files.createDirectories(target.getParent());
            // 复制文件
            Path source = Paths.get(sourcePath).toAbsolutePath();
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("复制文件时出错: " + e.getMessage());
        }
    }

    /**
     * 复制文件并保持层级结构
     *
     * @param sourcePath   源文件路径
     * @param targetFolder 目标文件夹
     * @param basePath     要保持的基准路径
     * @throws IOException
     */
    public static void copyFileWithHierarchy(String sourcePath, String targetFolder, String basePath) throws IOException {
        Path source = Paths.get(sourcePath).toAbsolutePath();
        Path base = Paths.get(basePath).toAbsolutePath();
        Path targetDir = Paths.get(targetFolder).toAbsolutePath();

        // 验证基准路径是否是源文件的父路径
        if (!source.startsWith(base)) {
            throw new IllegalArgumentException("源文件不在指定的基准路径下");
        }

        // 计算相对路径
        Path relativePath = base.relativize(source.getParent());

        // 创建目标路径
        Path targetPath = targetDir.resolve(relativePath).resolve(source.getFileName());

        // 创建所有必要的父目录
        Files.createDirectories(targetPath.getParent());

        // 复制文件
        Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 复制整个目录并保持层级结构
     *
     * @param sourceDir    源目录
     * @param targetFolder 目标文件夹
     * @param basePath     要保持的基准路径
     * @throws IOException
     */
    public static void copyDirectoryWithHierarchy(String sourceDir, String targetFolder, String basePath) throws IOException {
        Path source = Paths.get(sourceDir).toAbsolutePath();
        Path base = Paths.get(basePath).toAbsolutePath();
        Path targetDir = Paths.get(targetFolder).toAbsolutePath();

        if (!source.startsWith(base)) {
            throw new IllegalArgumentException("源目录不在指定的基准路径下");
        }

        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relativePath = base.relativize(dir);
                Path targetPath = targetDir.resolve(relativePath);
                Files.createDirectories(targetPath);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = base.relativize(file);
                Path targetPath = targetDir.resolve(relativePath);
                Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
