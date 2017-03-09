package br.cin.ufpe.dass.matchers.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diego on 07/03/17.
 */
@Document
public class Matcher {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String version;

    private String endPoint;

    private Map<MatcherFeature, String> features = new HashMap<MatcherFeature, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<MatcherFeature, String> getFeatures() {
        return features;
    }

    public void setFeatures(Map<MatcherFeature, String> features) {
        this.features = features;
    }

    public Map<String, Object> configurationParameters = new HashMap<String, Object>();

    public Map<String, Object> getConfigurationParameters() {
        return configurationParameters;
    }

    public void setConfigurationParameters(Map<String, Object> configurationParameters) {
        this.configurationParameters = configurationParameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
