/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.file;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.pos.common.util.basic.UUIDUnsigned32;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;

/**
 * Zip&Rar工具类
 *
 * @author cc
 * @version 1.0, 16/7/11
 */
public class ZipAndRarFileUtils {

    private final static Logger logger = LoggerFactory.getLogger(ZipAndRarFileUtils.class);

    /**
     * zip解压缩
     *
     * @param srcZipPath     需要解压缩的文件路径
     * @param outDir         解压后的目标目录
     * @param randomChildDir 是否在outDir下生成一个随机名称的子文件夹
     */
    public static String decompressZip(String srcZipPath, String outDir, boolean randomChildDir) {
        try {
            String newOutPath = randomChildDir ? generateUUIDDirectory(outDir) : outDir;
            ZipFile zf = new ZipFile(srcZipPath);
            for (Enumeration entries = zf.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String zipEntryName = entry.getName();
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(new File(newOutPath + zipEntryName + File.separator));
                } else {
                    IOUtils.copy(zf.getInputStream(entry), FileUtils.openOutputStream(new File(newOutPath + zipEntryName)));
                }
            }
            return newOutPath;
        } catch (IOException e) {
            logger.error("zip解压缩发生异常，zipFile={}，outDir={}", srcZipPath, outDir, e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * rar解压缩
     *
     * @param srcRarPath     原始rar路径
     * @param outDir         解压到的文件夹
     * @param randomChildDir 是否在outDir下生成一个随机名称的子文件夹
     */
    public static String decompressRar(String srcRarPath, String outDir, boolean randomChildDir) {
        try {
            String newOutPath = randomChildDir ? generateUUIDDirectory(outDir) : outDir;
            Archive archive = new Archive(new File(srcRarPath));
            FileHeader entry = archive.nextFileHeader();
            while (entry != null) {
                String fileName;
                if (entry.isUnicode()) {
                    fileName = entry.getFileNameW().trim().replaceAll("\\\\", "/");
                } else {
                    fileName = entry.getFileNameString().trim().replaceAll("\\\\", "/");
                }
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(new File(newOutPath + fileName + File.separator));
                } else {
                    File out = new File(newOutPath + File.separator + fileName);
                    File parent = out.getParentFile();
                    if (parent != null && !parent.exists()) {
                        FileUtils.forceMkdir(parent);
                    }
                    FileOutputStream os = new FileOutputStream(out);
                    archive.extractFile(entry, os);
                    os.close();
                }
                entry = archive.nextFileHeader();
            }
            archive.close();
            return newOutPath;
        } catch (Exception e) {
            logger.error("rar解压缩发生异常，srcRarPath={}，outDir={}", srcRarPath, outDir, e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 产生随机的文件夹名
     *
     * @param outZipPath 原路径
     * @return 新路径
     * @throws IOException
     */
    private static String generateUUIDDirectory(String outZipPath) throws IOException {
        String randDirName = UUIDUnsigned32.randomUUIDString();
        String newOutZipPath = outZipPath + randDirName + File.separator;
        File file = new File(newOutZipPath);
        FileUtils.forceMkdir(file);
        return newOutZipPath;
    }

}
