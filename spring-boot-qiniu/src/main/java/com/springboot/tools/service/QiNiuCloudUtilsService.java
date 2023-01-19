package com.springboot.tools.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.springboot.tools.domain.FileMessage;
import com.springboot.tools.config.QiNiuPropertiesConfig;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @author kxd
 * @date 2018/12/18 21:08
 * description:七牛云相关服务类
 */
@Slf4j
@Component
public class QiNiuCloudUtilsService implements ApplicationRunner {
    private final int maxSize = 1000;
    private static final String PRODUCTION = "production";
    @Autowired
    private QiNiuPropertiesConfig qiNiuPropertiesConfig;
    private Auth auth;
    private Configuration configuration;
    private Recorder recorder;
    private UploadManager uploadManager;
    private BucketManager bucketManager;


    /**
     * 获取bucket里面所有文件的信息
     *
     * @param bucketNm String
     */
    public List<String> getFileInfo(String bucketNm) {
        //文件名前缀
        String prefix = "";
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator iterator = bucketManager.createFileListIterator(bucketNm, prefix, maxSize, delimiter);
        List<String> res = Lists.newArrayList();
        while (iterator.hasNext()) {
            com.qiniu.storage.model.FileInfo[] fileInfos = iterator.next();
            for (com.qiniu.storage.model.FileInfo fileInfo : fileInfos) {
                String info = fileInfo.key + "|" + fileInfo.endUser + "|" + fileInfo.fsize + "|" + fileInfo.mimeType + "|" + fileInfo.putTime + "|" + fileInfo.type;
                res.add(info);
            }
        }
        return res;
    }

    /**
     * 获取所有的bucket
     */
    public void getBucketsInfo() {
        try {
            BucketManager bucketManager = getBucketManager();
            //获取所有的bucket信息
            String[] bucketNms = bucketManager.buckets();
            for (String bucket : bucketNms) {
                log.info(bucket);
            }
        } catch (Exception e) {
            log.error("方法异常getBucketsInfo", e);
        }
    }

    /**
     * 删除多个文件
     *
     * @param bucketNm bucket的名称
     * @param keys     文件名称数组
     *                 //单次批量请求的文件数量不得超过1000 , 这个是七牛所规定的
     * @return result
     */
    public Result deleteFiles(String bucketNm, String[] keys) {
        try {
            //当文件大于1000的时候，就直接不处理
            if (keys.length > maxSize) {
                return new Result(false);
            }

            //设定删除的数据
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucketNm, keys);
            BucketManager bucketManager = getBucketManager();
            //发送请求
            Response response = bucketManager.batch(batchOperations);
            //返回数据
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    log.info("delete success");
                } else {
                    log.info(status.data.error);
                    return new Result(false);
                }
            }
            return new Result(true);
        } catch (Exception e) {
            log.error("deleteFiles方法异常", e);
            return new Result(false);
        }
    }

    /**
     * 删除bucket中的文件名称
     *
     * @param bucketNm bucker名称
     * @param key      文件名称
     * @return Result 定义的返回结果对象
     */
    public Result deleteFile(String bucketNm, String key) {
        Result result;
        try {
            BucketManager mg = getBucketManager();
            mg.delete(bucketNm, key);
            result = new Result(true);
        } catch (Exception e) {
            log.error("deleteFile方法异常", e);
            result = new Result(false);
        }
        return result;
    }

    /**
     * 通过输入流上传
     *
     * @param bucketNm bucket的名称
     * @param in       输入流
     * @return 上传结果
     */
    public Result upload(String bucketNm, InputStream in, String key) throws QiniuException {
        UploadManager uploadManager = getUploadManager();
        //获取token
        String token = getToken(bucketNm);
        //上传输入流
        Response response = uploadManager.put(in, key, token, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return new Result(true, putRet.key);
    }

    /**
     * 异步上传数据
     *
     * @param bucketNm            bucket的名称
     * @param data                上传的数据
     * @param key                 上传凭证
     * @param params              自定义参数，如 params.put("x:foo", "foo")
     * @param mime                文件类型
     * @param checkCrc            是否开启crc校验
     * @param upCompletionHandler 回调处理方法
     * @throws IOException io异常
     */
    public void asyncUpload(String bucketNm, final byte[] data, final String key, StringMap params,
                            String mime, boolean checkCrc, UpCompletionHandler upCompletionHandler) throws IOException {
        UploadManager uploadManager = getUploadManager();
        String token = getToken(bucketNm);
        uploadManager.asyncPut(data, key, token, null, mime, checkCrc, upCompletionHandler);
    }


    /**
     * 上传文件
     *
     * @param bucketNm String
     * @param file     File
     * @return Result
     */
    public Result upload(String bucketNm, File file) throws QiniuException {
        UploadManager uploadManager = getUploadManager();
        String token = getToken(bucketNm);
        Response response = uploadManager.put(file.getAbsolutePath(), newName(file.getName()), token);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return new Result(true, putRet.key);
    }

    /**
     * 断点续传，分段上传方法
     *
     * @param bucketNm 存储区域
     * @param file     文件
     * @param key      文件别名
     * @param params   文件名过滤属性
     * @param mime     文件类型
     * @param checkCrc crc文件校验是否开启
     * @return 返回结果对象
     */
    public Result uploadWithRecorder(String bucketNm, File file, String key, StringMap params, String mime, boolean checkCrc) throws Exception {
        Result result;
        UploadManager uploadManager = getUploadManagerWithRecorder();
        String token = getToken(bucketNm);
        Response response = uploadManager.put(file, key, token, params,
                mime, checkCrc);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        result = new Result(true, putRet.key);
        return result;
    }


    /**
     * 文件下载
     *
     * @param info 文件信息
     */
    public void download(FileMessage info) {
        //获取downloadUrl
        String downloadUrl = getDownloadUrl(info);
        //本地保存路径
        String filePath = qiNiuPropertiesConfig.getDownLoadSavePath();
        download(downloadUrl, info, filePath);
    }

    /**
     * 获取下载文件路径，即：donwloadUrl
     *
     * @return
     */
    public String getDownloadUrl(FileMessage info) {

        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(info.getRemoteFileName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("getDownloadUrl中异常", e);
        }
        String publicUrl = String.format("%s/%s", info.getFilePath(), encodedFileName);
        String downloadUrl = getAuth().privateDownloadUrl(publicUrl);
        return downloadUrl;
    }


    /**
     * 通过发送http get 请求获取文件资源
     *
     * @param url         文件位置
     * @param fileMessage 文件信息
     * @param savePath    保存路径
     */
    private static void download(String url, FileMessage fileMessage, String savePath) {
        OkHttpClient client = new OkHttpClient();
        System.out.println(url);
        Request req = new Request.Builder().url(url).build();
        com.squareup.okhttp.Response resp = null;
        try {
            resp = client.newCall(req).execute();
            System.out.println(resp.isSuccessful());
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                byte[] data = readInputStream(is);
                //判断文件夹是否存在，不存在则创建
                File file = new File(savePath);
                if (!file.exists() && !file.isDirectory()) {
                    System.out.println("===文件夹不存在===创建====");
                    file.mkdir();
                }

                File saveFile = new File(file, fileMessage.getRemoteFileName());
                FileOutputStream fops = new FileOutputStream(saveFile);
                fops.write(data);
                fops.close();
            }
        } catch (IOException e) {
            log.info("download方法异常", e);
        }
    }


    /**
     * 读取字节输入流内容
     *
     * @param is
     * @return
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }


    /**
     * 通过老文件的名称自动生成新的文件
     *
     * @param oldName String
     * @return String
     */
    private static String newName(String oldName) {
        String[] datas = oldName.split("\\.");
        String type = datas[datas.length - 1];
        return UUID.randomUUID().toString() + "." + type;
    }


    /**
     * 获取Bucket的管理对象
     *
     * @return BucketManager
     */
    public BucketManager getBucketManager() {
        //构造一个带指定Zone对象的配置类
        //区域要和自己的bucket对上，不然就上传不成功
        //华东    Zone.zone0()
        //华北    Zone.zone1()
        //华南    Zone.zone2()
        //北美    Zone.zoneNa0()
        if (null == bucketManager) {
            bucketManager = new BucketManager(auth, configuration);
        }
        return bucketManager;
    }


    /**
     * 获取上传管理器，非分段上传，断点上传
     *
     * @return UploadManager
     */
    public UploadManager getUploadManager() {

        if (null == uploadManager) {
            uploadManager = new UploadManager(configuration);
        }
        return uploadManager;
    }

    /**
     * 构建一个支持断点续传的上传对象。只在文件采用分片上传时才会有效
     *
     * @return 上传管理
     */
    public UploadManager getUploadManagerWithRecorder() {

        return new UploadManager(configuration, recorder);
    }

    /**
     * 获取七牛云的上传Token
     *
     * @param bucketNm 目录名
     * @return String
     */
    private String getToken(String bucketNm) {
        return auth.uploadToken(bucketNm);
    }

    /**
     * 获取配置
     *
     * @return Configuration
     */
    @NotNull
    public Configuration getConfiguration() {
        if (null == configuration) {
            Zone.Builder jstv;
            if (PRODUCTION.equals(qiNiuPropertiesConfig.getActiveProfile())) {
                jstv = new Zone.Builder().upHttp(qiNiuPropertiesConfig.getUpHttpUrlPro())
                        .upBackupHttp(qiNiuPropertiesConfig.getUpHttpUrlPro())
                        .iovipHttp(qiNiuPropertiesConfig.getIoVipHttpPro())
                        .rsHttp(qiNiuPropertiesConfig.getRsHttpPro())
                        .rsfHttp(qiNiuPropertiesConfig.getRsfHttpPro())
                        .apiHttp(qiNiuPropertiesConfig.getApiHttpPro());
            } else {
                jstv = new Zone.Builder().upHttp(qiNiuPropertiesConfig.getUpHttpUrlTest())
                        .upBackupHttp(qiNiuPropertiesConfig.getUpHttpUrlTest())
                        .iovipHttp(qiNiuPropertiesConfig.getIoVipHttpTest())
                        .rsHttp(qiNiuPropertiesConfig.getRsHttpTest())
                        .rsfHttp(qiNiuPropertiesConfig.getRsfHttpTest())
                        .apiHttp(qiNiuPropertiesConfig.getApiHttpTest());
            }
            configuration = new Configuration(jstv.build());
        }

        return configuration;
    }


    /**
     * 获取Auth
     *
     * @return Auth
     */
    @NotNull
    public Auth getAuth() {
        if (null == auth) {
            if (PRODUCTION.equals(qiNiuPropertiesConfig.getActiveProfile())) {
                auth = Auth.create(qiNiuPropertiesConfig.getProAccessKey(), qiNiuPropertiesConfig.getProSecretKey());
            } else {
                auth = Auth.create(qiNiuPropertiesConfig.getTestAccessKey(), qiNiuPropertiesConfig.getTestSecretKey());
            }
        }
        return auth;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("加载qiniu中的auth,configuration,bucketManager属性.");
        getAuth();
        getConfiguration();
        getBucketManager();
        //断点文件保存的目录
        recorder = new FileRecorder("/Users/apple/breakpoint");
        log.info("加载qiniu中属性完毕.");
    }

    static class Result {
        private String url;
        private boolean error;

        public Result(boolean error) {
            super();
            this.error = error;
        }

        public Result(boolean error, String url) {
            super();
            this.url = url;
            this.error = error;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }
    }

}
