package com.springstart.Configuration;

import com.springstart.Utils.StreamSandbox;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilContext {

    @Bean
    public StreamSandbox getStreamSandbox(){
        return new StreamSandbox();
    }
}
