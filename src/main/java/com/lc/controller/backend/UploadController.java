package com.lc.controller.backend;

import com.lc.common.ServerResponse;
import com.lc.service.ProductServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//使用控制器来进入web-inf目录下的jsp页面
@Controller
@RequestMapping(value = "/manage/product")
public class UploadController {
    @Autowired
    ProductServer productServer;
//    浏览器访问该页面使用get请求，浏览器发送请求使用post请求
    @RequestMapping(value = "/upload",method =RequestMethod.GET )
    public String upload(){
        return "upload";//逻辑视图
    }
    @RequestMapping(value = "/upload")
    @ResponseBody
    public ServerResponse uploadpost(@RequestParam(value = "upload_file",required = false)MultipartFile file){

       String path="F:\\内网通\\filepicture";

        return productServer.upload(file,path);
    }
}
