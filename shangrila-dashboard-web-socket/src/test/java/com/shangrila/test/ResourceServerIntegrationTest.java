package com.shangrila.test;

import com.iot.app.springboot.config.CorsFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorsFilter.IoTDataDashboard.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceServerIntegrationTest {

    @Test
    public void whenLoadApplication_thenSuccess() {

    }
}
