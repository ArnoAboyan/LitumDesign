package com.litumdesign.LitumDesign;

import com.litumdesign.LitumDesign.ENV.NationConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableMBeanExport;

@SpringBootTest
@EnableConfigurationProperties(NationConfigProperties.class)
class LitumDesignApplicationTests {


}
