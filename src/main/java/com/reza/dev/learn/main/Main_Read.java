package com.reza.dev.learn.main;

import com.reza.dev.learn.config.ElasticsearchConfig;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

public class Main_Read {

    public static void main(String[] args) {

        // Instantiate config
        ElasticsearchConfig esConfig = new ElasticsearchConfig(args[0]);

        // Instantiate Spark
        SparkSession sparkSession = SparkSession.builder()
                .appName("ReadMain")
                .config("es.nodes", esConfig.ip())
                .config("es.port", esConfig.port())
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        // Load data from Elasticsearch then print the value
        JavaEsSpark.esJsonRDD(sparkContext, esConfig.index() + "/" + esConfig.type(), args[1])
                .foreach(tuple2 -> System.out.println(tuple2._2));
    }
}
