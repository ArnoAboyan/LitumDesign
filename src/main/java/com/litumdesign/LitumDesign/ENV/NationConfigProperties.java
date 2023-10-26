package com.litumdesign.LitumDesign.ENV;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Service;

@ConfigurationProperties("nation")
public record NationConfigProperties(String privateId, String privateKey, String clientMail, String clientId) {
}
