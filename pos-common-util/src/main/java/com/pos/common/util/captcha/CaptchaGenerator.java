package com.pos.common.util.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 验证码生成器
 */
public class CaptchaGenerator {

    /**
     * 图片的宽度
     */
    private int width = 81;

    /**
     * 图片的高度
     */
    private int height = 35;

    /**
     * 验证码字符个数
     */
    private int codeCount = 4;

    /**
     * 验证码干扰线数
     */
    private int lineCount = 80;

    /**
     * 验证码
     */
    private static String code = null;

    /**
     * 验证码图片Buffer
     */
    private BufferedImage buffImg = null;

    /**
     * 可能的字符
     */
    private char[] codeSequence = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U',
            'V', 'Y', 'Z', '2', '3', '5', '6', '7', '8'
    };

    /**
     * 无参构造器
     */
    public CaptchaGenerator(Font font) {
        this.createCode(font);
    }

    /**
     * 通过图片宽高进行构造
     *
     * @param width  图片宽
     * @param height 图片高
     */
    public CaptchaGenerator(int width, int height, Font font) {
        this.width = width;
        this.height = height;
        this.createCode(font);
    }

    /**
     * 通过图片宽高，字符数，干扰线条数构造
     *
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public CaptchaGenerator(int width, int height, int codeCount, int lineCount, Font font) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.createCode(font);
    }

    /**
     * 创建验证码
     */
    public void createCode(Font font) {
        int x = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount + 2);// 每个字符的宽度
        codeY = height - 4;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // randomCode记录随机产生的验证码
        StringBuilder randomCode = new StringBuilder();
        // 随机产生codeCount个字符的验证码
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的随机数组合在一起
            randomCode.append(strRand);
        }
        // 将验证码保存到code中
        code = randomCode.toString();
    }

    /**
     * 将验证码保存到文件
     *
     * @param path 文件路径
     * @throws IOException 输入输出异常
     */
    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    /**
     * 将输出流写入到图像数据缓冲区的图片
     *
     * @param sos 输出流
     * @throws IOException 输入输出异常
     */
    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    /**
     * 获取图像数据缓冲区的图片
     *
     * @return 图像数据缓冲区的图片
     */
    public BufferedImage getBuffImg() {
        return buffImg;
    }

    /**
     * 获取验证码字符串
     *
     * @return 验证码字符串
     */
    public String getCode() {
        return code;
    }

}