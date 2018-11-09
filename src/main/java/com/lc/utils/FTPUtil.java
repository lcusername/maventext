package com.lc.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {
    private static final String FTP_IP=PropertiesUtil.getBykey("ftp.ip");
    private static final String FTP_USERNAME=PropertiesUtil.getBykey("ftp.username");
    private static final String FTP_PASSWORD=PropertiesUtil.getBykey("ftp.password");

    private String ftpIp;
    private String ftpUser;
    private String ftppass;
    private Integer port;

    public FTPUtil(String ftpIp, String ftpUser, String ftppass, Integer port) {
        this.ftpIp = ftpIp;
        this.ftpUser = ftpUser;
        this.ftppass = ftppass;
        this.port = port;
    }

    /*
    * 图片上传到ftp
    * */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil=new FTPUtil(FTP_IP,FTP_USERNAME,FTP_PASSWORD,21);
        System.out.println("开始连接服务器");
        ftpUtil.uploadFile("img",fileList);
        return false;
    }

    private  boolean uploadFile(String removePath,List<File> fileList) throws IOException {
        FileInputStream fileInputStream=null;
        //连接诶ftp
        if (connectFTPServer(ftpIp,ftpUser,ftppass)){
            try {
                ftpClient.changeWorkingDirectory(removePath);//改变传输到达路径
                ftpClient.setBufferSize(1024);//缓冲大小
                ftpClient.setControlEncoding("UTF-8");//编码格式
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//文件类型二进制格式
                ftpClient.enterLocalPassiveMode();//打开被动传输模式
                //遍历
                for (File file:fileList){
                    fileInputStream=new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),fileInputStream);//保存文件
                }
                System.out.println("------文件上传成功--------");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件上传出错...");
            }finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }

        }

        return false;
    }

    /*
    * 连接到ftp服务器
    * */
    FTPClient ftpClient=null;
    private  boolean connectFTPServer(String ftpIp,String ftpUser,String ftppass){
       ftpClient =new FTPClient();
        try {
            ftpClient.connect(ftpIp);
          return ftpClient.login(ftpUser,ftppass);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接服务器异常...");
        }
        return false;
    }
}
