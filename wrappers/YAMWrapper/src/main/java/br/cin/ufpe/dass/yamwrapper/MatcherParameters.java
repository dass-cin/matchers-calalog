package br.cin.ufpe.dass.yamwrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diego on 29/03/17.
 */
public class MatcherParameters {

    private String source;

    private String target;

    private Map<String, Object> configParams = new HashMap<String, Object>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, Object> getConfigParams() {
        return configParams;
    }

    public void setConfigParams(Map<String, Object> configParams) {
        this.configParams = configParams;
    }
}
