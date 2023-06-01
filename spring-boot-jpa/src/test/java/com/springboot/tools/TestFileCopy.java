package com.springboot.tools;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: User
 * @date: 2023/4/27
 * @Description:此类用于xxx
 */
public class TestFileCopy {


    @Test
    public void test() {
        FileUtil.copyContent(FileUtil.file("D:\\logs1\\2.txt"), FileUtil.file("D:\\logs1\\3.txt"), true);
    }

}
