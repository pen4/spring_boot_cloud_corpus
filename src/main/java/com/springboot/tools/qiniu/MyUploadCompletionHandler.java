package com.springboot.tools.qiniu;

import com.qiniu.http.Response;
import com.qiniu.storage.UpCompletionHandler;
import org.springframework.stereotype.Service;

/**
 * @author kxd
 * @date 2019/2/21 16:10
 * description:上传完成后相关处理类
 */
@Service
public class MyUploadCompletionHandler implements UpCompletionHandler {
    @Override
    public void complete(String key, Response r) {
        System.out.println("----------key" + r.getInfo());
    }
}
