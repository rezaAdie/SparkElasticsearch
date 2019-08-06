package com.reza.dev.learn.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.reza.dev.learn.config.ElasticsearchConfig;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;


public class Main_Write {
    public static void main(String[] args) {

        // Instantiate config
        ElasticsearchConfig esConfig = new ElasticsearchConfig(args[0]);

        // Instantiate Spark
        SparkSession sparkSession = SparkSession.builder()
                .appName("WriteMain")
                .config("es.nodes", esConfig.ip())
                .config("es.port", esConfig.port())
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        // Read JSON from file and do transformation
        JavaRDD<Map<String, Object>> mapRDD = sparkContext.textFile("file://" + args[1])
                .map(stringJSON -> {
                    Map<String, Object> map = new ObjectMapper().readValue(stringJSON, Map.class);

                    // Create id from MD5 of link
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] messageDigest = md.digest(((String) map.get("link")).getBytes());
                    BigInteger number = new BigInteger(1, messageDigest);
                    StringBuilder stringBuilder = new StringBuilder(number.toString(16));

                    // Now we need to zero pad it if you actually want the full 32 chars.
                    while (stringBuilder.length() < 32) {
                        stringBuilder.insert(0, 0);
                    }

                    map.put("id", stringBuilder.toString());

                    return map;
                });

        // Save to Elasticsearch
        JavaEsSpark.saveToEs(mapRDD, String.format("%s/%s", esConfig.index(), esConfig.type()), ImmutableMap.of("es.mapping.id", "id"));
    }
}
