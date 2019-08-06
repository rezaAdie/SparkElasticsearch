package com.reza.dev.learn.config;

import org.json.JSONObject;

public class ElasticsearchConfig {
    private String esIP;
    private String esPort;
    private String esIndex;
    private String esType;

    /**
     * Set variable in constructor.
     * @param stringJSONParam -> String JSON parameter
     */
    public ElasticsearchConfig(String stringJSONParam) {
        JSONObject jsonParam = new JSONObject(stringJSONParam);
        esIP = jsonParam.getString("ip");
        esPort = jsonParam.getString("port");
        esIndex = jsonParam.getString("index");
        esType = jsonParam.getString("type");
    }

    /**
     * Elasticsearch IP address
     * @return String IP address
     */
    public String ip() {
        return esIP;
    }

    /**
     * Elasticsearch port
     * @return String port
     */
    public String port() {
        return esPort;
    }

    /**
     * Elasticsearch index name
     * @return String index name
     */
    public String index() {
        return esIndex;
    }

    /**
     * Elasticsearch type
     * @return String type
     */
    public String type() {
        return esType;
    }
}