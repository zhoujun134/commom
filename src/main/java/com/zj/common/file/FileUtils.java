package com.zj.common.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
        Path path = Paths.get(pathString);
        // 文件是否存在
        boolean isExists = Files.exists(path);
        if (!isExists) {
            String message = String.format("%s 文件不存在！", pathString);
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
        Path path = Paths.get(pathString);
        // 文件是否存在
        boolean isExists = Files.exists(path);
        if (!isExists) {
            String message = String.format("%s 文件不存在！", pathString);
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

}
