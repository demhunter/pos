/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.common;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.List;

/**
 * ConnectionManager
 *
 * @author cc
 * @version 1.0, 16/9/20
 */
@SuppressWarnings("all")
@Component
public class ConnectionManager {

    private static Session sshSession;

    private static String SRC_DB_NAME = "ywmj";

    private static String DEST_DB_NAME = "ywmj";

    private static String LOCAL_HOST_IP = "localhost";

    private static String DEST_HOST_IP = "139.196.5.114";

    private static String SRC_MYSQL_ACCOUNT = "root";

    private static String SRC_MYSQL_PASSWORD = "root";

    private static String DEST_MYSQL_ACCOUNT = "ywmj";

    private static String DEST_MYSQL_PASSWORD = "ywmj16530";

    private static String SSH_ACCOUNT = "root";

    private static String SSH_PASSWORD = "YW+mj=2016";

    private static int LOCAL_MIRROR_PORT = 12345;

    private static int SSH_PORT = 10022;

    private static String SRC_URL =
            "jdbc:mysql://" + LOCAL_HOST_IP + ":3306/" + SRC_DB_NAME + "?useUnicode=true&amp;" +
                    "zeroDateTimeBehavior=convertToNull&amp;useServerPrepStmts=false";

    private static String DEST_URL =
            "jdbc:mysql://" + DEST_HOST_IP + ":3306/" + DEST_DB_NAME + "?useUnicode=true&amp;" +
                    "zeroDateTimeBehavior=convertToNull&amp;useServerPrepStmts=false";

    private static String DEST_SSH_URL =
            "jdbc:mysql://" + LOCAL_HOST_IP + ":" + LOCAL_MIRROR_PORT + "/" + DEST_DB_NAME + "?useUnicode=true&amp;" +
                    "zeroDateTimeBehavior=convertToNull&amp;useServerPrepStmts=false";

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private Connection connection;


//     @PostConstruct
//    public void initialize() {
//        srcConn = doConnect(SRC_URL, SRC_MYSQL_ACCOUNT, SRC_MYSQL_PASSWORD);
//        buildSSHTunnel();
//        destConn = doConnect(DEST_SSH_URL, DEST_MYSQL_ACCOUNT, DEST_MYSQL_PASSWORD);
//    }

    /**
     * SELECT
     */
    public ResultSet excuteQuery(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            connection.prepareStatement(sql);

            return statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("excuteQuery", e);
            throw new IllegalStateException("excuteQuery");
        }
    }

    /**
     * SELECT
     */
    public ResultSet excuteQuery(String type, String sql, Object... vals) {
        return excuteQuery(getConnection(type), String.format(sql, vals));
    }

    /**
     * INSERT UPDATE DELETE
     */
    public int excuteUpdate(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            connection.prepareStatement(sql);

            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("excuteUpdate", e);
            throw new IllegalStateException("excuteUpdate");
        }
    }

    /**
     * INSERT UPDATE DELETE
     */
    public int excuteUpdate(String type, String sql, Object... vals) {
        return excuteUpdate(getConnection(type), String.format(sql, vals));
    }

    public void excuteBatchUpdate(String type, List<String> updateSqls) {
        try {
            connection = getConnection(type);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            if (!CollectionUtils.isEmpty(updateSqls)) {
                updateSqls.forEach(sql -> {
                    try {
                        statement.addBatch(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("executeBatchUpdate", e);
            throw new IllegalStateException("executeBatchUpdate");
        }
    }


    public Connection getConnection(String type) {
        if ("test".equals(type)) {
            connection = doConnect(SRC_URL, SRC_MYSQL_ACCOUNT, SRC_MYSQL_PASSWORD);

        } else {
            buildSSHTunnel();
            connection = doConnect(DEST_SSH_URL, DEST_MYSQL_ACCOUNT, DEST_MYSQL_PASSWORD);
        }
        return connection;
    }

    private Connection doConnect(String url, String account, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("forName exception!", e);
            throw new IllegalStateException("forName exception!");
        }

        try {
            return DriverManager.getConnection(url, account, password);
        } catch (SQLException e) {
            logger.error("连接数据库失败！url = {}, account = {}, password = {}", url, account, password);
            throw new IllegalStateException("连接数据库失败！");
        }
    }

    private void buildSSHTunnel() {
        try {
            sshSession = new JSch().getSession(SSH_ACCOUNT, DEST_HOST_IP, SSH_PORT);
            sshSession.setPassword(SSH_PASSWORD);
            // 如果不设置此项会抛出UnknownHostKey异常
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();
            // 打印ssh服务器版本信息
            logger.info(sshSession.getServerVersion());
            // 端口转发，本地起一个socket进行监听，通过ssh安全通道传输数据
            sshSession.setPortForwardingL(LOCAL_MIRROR_PORT, LOCAL_HOST_IP, 3306);
        } catch (JSchException e) {
            logger.error("构建SSH通道发生异常！", e);
            throw new IllegalStateException("构建SSH通道发生异常！");
        }
    }

    private void destroySSHTunnel() {
        sshSession.disconnect();
    }
}
