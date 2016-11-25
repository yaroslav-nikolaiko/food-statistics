package demo;

import demo.parser.CSVParser;

import java.net.MalformedURLException;
import java.net.URL;

public class SparkServiceManager {
    static SparkServiceManager manager = new SparkServiceManager();
    SparkService sparkService;


    public static synchronized SparkService getSparkService(){
        if(manager.sparkService == null){
            manager.sparkService = new SparkService();
            manager.sparkService.setParser(new CSVParser());
            String sample = SparkServiceTest.class.getResource("/sample.csv").getFile();
            manager.sparkService.load(sample);
        }
        return manager.sparkService;
    }
}
