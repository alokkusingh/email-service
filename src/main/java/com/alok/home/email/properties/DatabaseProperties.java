package com.alok.home.email.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "database")
@Component
public class DatabaseProperties {

    private long pollRate = 3000000;

}
