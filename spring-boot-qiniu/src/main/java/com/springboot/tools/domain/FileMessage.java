package com.springboot.tools.domain;

import com.sun.istack.internal.NotNull;
import lombok.Data;

/**
 * @author kxd
 * @date 2018/12/21 20:03
 * description:
 */
@Data
public class FileMessage {

    /**
     * 服务本地上传文件的全路径
     */
    @NotNull
    String filePath;
    /**
     * 上传之后在七牛云中新的文件名
     */
    String remoteFileName;
}
