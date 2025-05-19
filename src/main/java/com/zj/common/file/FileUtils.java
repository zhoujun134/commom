package com.zj.common.file;

import com.zj.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author junzhou
 * @date 2022/9/18 10:02
 * @since 1.8
 */
@Slf4j
public class FileUtils {

    /**
     * 按行读取文件内容信息
     *
     * @param pathString 读取文件内容信息的 path
     * @return 每一行的内容信息
     */
    public static List<String> readLines(String pathString) {
        List<String> result = Collections.emptyList();
        final Path path = Paths.get(pathString);
        // 文件是否存在
        final boolean isExists = Files.exists(path);
        if (!isExists) {
            final String message = String.format("%s 文件不存在！", pathString);
            throw new RuntimeException(message);
        }
        try (Stream<String> lines = Files.lines(path)) {
            result = lines.collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("读取文件发生异常！", exception);
        }
        return result;
    }

    /**
     * 从路径 pathString 中查询指定关键字 keyword 的文件内容
     *
     * @param pathString 文件路径
     * @param keyword    查询的关键字
     * @return 在该文件中查询到的文件内容 <行号, 行内容>
     * 其返回的值，将按照行号排序
     */
    public static Map<Integer, String> findContentByKeyWord(String pathString, String keyword) {
        Map<Integer, String> result = new HashMap<>();
        final Path path = Paths.get(pathString);
        // 文件是否存在
        final boolean isExists = Files.exists(path);
        if (!isExists) {
            final String message = String.format("%s 文件不存在！", pathString);
            throw new RuntimeException(message);
        }
        try (final FileInputStream fileInputStream = new FileInputStream(pathString);
             final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             final LineNumberReader reader = new LineNumberReader(inputStreamReader)) {
            String line = "";
            while (Objects.nonNull(line = reader.readLine())) {
                if (line.contains(keyword)) {
                    result.put(reader.getLineNumber(), line);
                }
            }
        } catch (IOException exception) {
            log.error("findContentByKeyWord: 读取文件发生异常！", exception);
        }
        return result;
    }

    public static List<String> findAllFilePaths(String directoryPath) {
        List<String> result = new ArrayList<>();
        Path startPath = Paths.get(directoryPath);
        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.forEach(filePath -> {
                // 打印文件或目录的路径
                System.out.println(filePath);
                result.add(filePath.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/zj/IdeaProjects/zj/zj-blogs/blog";
        List<String> filePathList = findAllFilePaths(path);
        Set<String> fileList = new HashSet<>();
        filePathList.forEach(filePath -> {
            boolean isMdFile = filePath.endsWith(".md");
            if (!isMdFile) {
                return;
            }
            List<String> allLines = readLines(filePath);
            if (CollectionUtils.isEmpty(allLines)) {
                return;
            }
            List<String> allLinks = findLinks(filePath);
            log.info("当前文件: {},中所有的图片链接为: {}", filePath, JsonUtil.toJSONString(allLinks));
            if (CollectionUtils.isEmpty(allLines)) {
                return;
            }
            replaceLinksInPlace(filePath, "/images");
//            allLinks.forEach(oneLink -> {
//                String[] split = oneLink.split("https://zj134-file\\.cpolar\\.cn/file/");
//                if (split.length == 2) {
//                    String fileName = split[1];
//                    if (fileName.endsWith(")")) {
//                        fileName = fileName.replace(")", "");
//                    }
//                    if (StringUtils.hasText(fileName)) {
//                        fileList.add(fileName);
//                    }
//                } else {
//                    System.out.println("异常 line: " + oneLink);
//                }
//            });

        });
        log.info("图片链接数:{}, 所有链接为: {} ", fileList.size(), JsonUtil.toJSONString(fileList));

//        String path = "/Users/zj/IdeaProjects/zj/zj-blogs/docs/08-面试相关/2024-05-13-00-17-30-redis xiang-guan-mian-shi-ti-gui-zong.md";

//        String basePath = "/Users/zj/Desktop/tengxun-oss";
//        String targetDirPath = "/Users/zj/IdeaProjects/zj/zj-blogs/static/images";
//        Set<String> errorSet = new HashSet<>();
//        fileList.forEach(filePath -> {
//            log.info("处理当前文件 path: " + filePath);
//            String baseFilePath = basePath + "/" + filePath;
//            copyFile(baseFilePath, basePath, targetDirPath, errorSet);
//        });
//        log.info("出现异常的文件路径为: {}", JsonUtil.toJSONString(errorSet));

    }

    public static void copyFile(String sourcePath, String sourceDirPath, String targetDirPath, Set<String> errorSet) {
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
            if (Files.exists(target)) {
                log.info("当前文件已经存在了，不进行复制：newPath:{}", newPath);
                return;
            }
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("复制文件时出错： sourcePath:{}, sourceDirPath:{}, targetDirPath:{}",
                    sourcePath, sourceDirPath, targetDirPath, e);
            errorSet.add(sourcePath);
        }
    }

    public static List<String> findLinks(String filePath) {
        List<String> result = new ArrayList<>();
        // 定义正则表达式模式，匹配以 https://img.zbus.top/ 开头的URL
        String patternString = "https://zj134-file\\.cpolar\\.cn/file/[^\\s\"']+";
        Pattern pattern = Pattern.compile(patternString);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    result.add(matcher.group());
                }
            }
        } catch (Exception exception) {
            log.error("findLinks: 读取文件发生异常！", exception);
        }
        return result;
    }

    public static int replaceLinksInPlace(String filePath, String replacement) {
        try {
            // 创建临时备份文件
            String backupPath = filePath + ".bak";
            Files.copy(Paths.get(filePath), Paths.get(backupPath), StandardCopyOption.REPLACE_EXISTING);

            // 定义正则表达式模式
            String patternString = "https://zj134-file\\.cpolar\\.cn/file/[^\\s\"']+";
            Pattern pattern = Pattern.compile(patternString);
            int count = 0;
            // 读取原始文件内容到内存
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            // 执行替换
            Matcher matcher = pattern.matcher(content);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                count++;
                String group = matcher.group();
                String[] split = group.split("https://zj134-file\\.cpolar\\.cn/file/");
                if (split.length == 2) {
                    String fileName = split[1];
                    String curReplacement = replacement + "/" + fileName;
                    matcher.appendReplacement(sb, curReplacement);
                } else {
                    System.out.println("异常 line: " + group);
                }
            }
            matcher.appendTail(sb);
            // 将修改后的内容写回原文件
            Files.write(Paths.get(filePath), sb.toString().getBytes());

            // 删除备份文件（如果成功）
            Files.deleteIfExists(Paths.get(backupPath));
            return count;
        } catch (Exception e) {
            log.error("出现异常了: ", e);
        }
        return 0;
    }

}
