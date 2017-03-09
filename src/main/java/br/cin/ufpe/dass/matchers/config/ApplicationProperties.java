package br.cin.ufpe.dass.matchers.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by diego on 07/03/17.
 */

@ConfigurationProperties(prefix="application")
public class ApplicationProperties {

    private String defaultURI = "http://cin.ufpe.br/dass/matchers-catalog";

    public String getDefaultURI() {
        return defaultURI;
    }

    public void setDefaultURI(String defaultURI) {
        this.defaultURI = defaultURI;
    }

}


