package com.springboot.tools.controller;

import com.qiniu.storage.BucketManager;
import com.springboot.tools.qiniu.QiNiuCloudUtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

/**
 * @author kxd
 * @date 2018/12/21 15:35
 * description:
 */
@Controller
@Slf4j
@RequestMapping("/api")
public class QiniuController {

    private final String bucketName = "zabx-repay";

    @Autowired
    QiNiuCloudUtilsService qiNiuCloudUtilsService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, name = "上传文件到七牛")
    @ResponseBody
    public String upload(@RequestBody FileMessage info) throws FileNotFoundException {

        File file = new File(info.getFilePath());
        InputStream downloadInfo = new FileInputStream(file);
        //通过文件流来上传文件
        qiNiuCloudUtilsService.upload(bucketName, downloadInfo, file.getName());
        return "上传成功";
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
        return ResponseInfo.builder().mesg(res).build();

    }


}
