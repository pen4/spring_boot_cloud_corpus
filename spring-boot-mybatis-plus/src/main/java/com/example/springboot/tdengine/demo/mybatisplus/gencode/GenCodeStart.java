package com.example.springboot.tdengine.demo.mybatisplus.gencode;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: User
 * @date: 2023/6/1
 * @Description:此类用于生成代码
 */
@Slf4j
public class GenCodeStart extends BaseGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://127.0.0.1:3306/gen_code?serverTimezone=Asia/Shanghai", "root", "123456")
            .schema("test_gen")
            .build();


    public static void gen() {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig().build());
        generator.global(globalConfig().build());
        generator.packageInfo(packageConfig().build());
        generator.execute();
    }

    public static void main(String[] args) {
        gen();
        log.info("生成成功");
    }
}
