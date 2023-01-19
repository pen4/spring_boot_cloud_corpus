package com.springboot.tools.controller;

import com.springboot.tools.domain.FileMessage;
import com.springboot.tools.qiniu.handler.MyUploadCompletionHandler;
import com.springboot.tools.service.QiNiuCloudUtilsService;
import com.springboot.tools.domain.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

import static com.google.common.io.ByteStreams.toByteArray;

/**
 * @author kxd
 * @date 2018/12/21 15:35
 * description:
 */
@Controller
@Slf4j
@RequestMapping("/api")
public class QiniuController {

    private final String bucketName = "test";

    @Autowired
    QiNiuCloudUtilsService qiNiuCloudUtilsService;
    @Autowired
    MyUploadCompletionHandler myUploadCompletionHandler;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, name = "上传文件到七牛")
    @ResponseBody
    public String upload(@RequestBody FileMessage info) throws Exception {

        File file = new File(info.getFilePath());
        InputStream downloadInfo = new FileInputStream(file);
        //通过文件流来上传文件
        qiNiuCloudUtilsService.upload(bucketName, downloadInfo, file.getName());
        return "上传成功";
    }


    @RequestMapping(value = "/uploadWithRecorder", method = RequestMethod.POST, name = "分段断点上传大文件到七牛")
    @ResponseBody
    public String uploadWithRecorder(@RequestBody FileMessage info) throws Exception {

        File file = new File(info.getFilePath());

        //通过文件流来上传文件
        qiNiuCloudUtilsService.uploadWithRecorder(bucketName, file, info.getRemoteFileName(), null, "zip", true);
        return "分段上传成功";
    }


    @RequestMapping(value = "/asyncupload", method = RequestMethod.POST, name = "异步上传文件到七牛")
    @ResponseBody
    public String asyncUploadFile(@RequestBody FileMessage info) throws Exception {

        File file = new File(info.getFilePath());
        InputStream inputStream = new FileInputStream(file);
        //通过字节数组来上传文件
        //速度快，立即返回，即使断网也会成功
        qiNiuCloudUtilsService.asyncUpload(bucketName, toByteArray(inputStream), info.getRemoteFileName(), null, "zip", true, myUploadCompletionHandler);
        return "分段上传成功";
    }


    /**
     * 下载接口
     * {
     * "filePath":"http://test-coreasset.oss.test",
     * "fileName":"02.txt"
     * }
     *
     * @param info
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST, name = "上传文件到七牛")
    @ResponseBody
    public String download(@RequestBody FileMessage info) {
        qiNiuCloudUtilsService.download(info);
        return "下载成功";
    }


    /**
     * 查找指定bucket_name下的文件信息
     */
    @RequestMapping(value = "/queryFileInfo", method = RequestMethod.POST, name = "查找指定bucket_name下的文件信息")
    @ResponseBody
    public ResponseInfo query(@RequestParam String bucketName) {
        List res = qiNiuCloudUtilsService.getFileInfo(bucketName);
        return ResponseInfo.builder().msg(res).build();
    }

}
