package com.weather.prediction.configs;

import com.weather.prediction.utils.EncryptionUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Data
@Slf4j
public class WeatherConfiguration {

    @Value(value = "${weather.baseUrl}")
    private String baseUrl;

    @Value(value = "${app.secretKey}")
    private String secretKey;

    @Value("${app.apiSecretKeyEncrypted}")
    private String encryptedApiKey;

    private String apikey;

    @PostConstruct
    public void init() throws Exception {
        try {
            apikey = EncryptionUtils.decrypt(secretKey, encryptedApiKey);
        } catch (Exception ex) {
            log.error("ERROR : Exception occurred while decrypting key ", ex);
            throw ex;
        }
    }

}
