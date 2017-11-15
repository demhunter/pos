/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.file;

import java.io.*;

/**
 * 文件字节流的读写工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/23
 */
public class FileBytesStreamUtils {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    private FileBytesStreamUtils() {
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param filePath 目标文件的路径
     * @return 目标文件的字节数组
     * @throws IOException
     */
    public static byte[] read(String filePath) throws IOException {
        return read(new File(filePath), DEFAULT_BUFFER_SIZE);
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param filePath   目标文件的路径
     * @param bufferSize 设置缓冲区大小(单位: 字节), 小于1KB(1024)则默认为1KB
     * @return 目标文件的字节数组
     * @throws IOException
     */
    public static byte[] read(String filePath, int bufferSize) throws IOException {
        return read(new File(filePath), bufferSize);
    }

    /**
     * 读取目标文件的字节流, 将其写入数组并返回.
     *
     * @param file       目标文件
     * @param bufferSize 设置缓冲区大小(单位: 字节), 小于1KB(1024)则默认为1KB
     * @return 目标文件的字节数组
     * @throws IOException
     */
    public static byte[] read(File file, int bufferSize) throws IOException {
        if (bufferSize < DEFAULT_BUFFER_SIZE) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
        byte[] b = new byte[bufferSize];
        int len;
        try {
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * 将目标字节流写入指定的输出文件.
     *
     * @param inputBytes     目标字节数组
     * @param outputFilePath 输出文件的路径
     * @throws IOException
     */
    public static void write(byte[] inputBytes, String outputFilePath) throws IOException {
        write(inputBytes, new File(outputFilePath));
    }

    /**
     * 将目标字节流写入指定的输出文件.
     *
     * @param inputBytes 目标字节数组
     * @param outputFile 输出文件
     * @throws IOException
     */
    public static void write(byte[] inputBytes, File outputFile) throws IOException {
        FileOutputStream out = new FileOutputStream(outputFile);
        try {
            if (inputBytes != null) {
                for (byte b : inputBytes)
                    out.write(b);
            }
            out.flush();
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            out.close();
        }
    }

    /**
     * 读取目标文件的字节流, 并将其写入指定的输出文件.
     *
     * @param inputFilePath  目标文件的路径
     * @param outputFilePath 输出文件的路径
     * @param bufferSize     设置缓冲区大小(单位: 字节), 小于1KB(1024)则默认为1KB
     * @throws IOException
     */
    public static void write(String inputFilePath, String outputFilePath,
                             int bufferSize) throws IOException {
        write(new File(inputFilePath), new File(outputFilePath), bufferSize);
    }

    /**
     * 读取目标文件的字节流, 并将其写入指定的输出文件.
     *
     * @param inputFile  目标文件
     * @param outputFile 指定的输出文件
     * @param bufferSize 设置缓冲区大小(单位: 字节), 小于1KB(1024)则默认为1KB
     * @throws IOException
     */
    public static void write(File inputFile, File outputFile, int bufferSize)
            throws IOException {
        if (bufferSize < DEFAULT_BUFFER_SIZE) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] b = new byte[bufferSize];
        int len;
        try {
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            out.close();
            in.close();
        }
    }

}