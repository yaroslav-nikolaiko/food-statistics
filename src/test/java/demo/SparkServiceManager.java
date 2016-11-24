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
            //URL sample = SparkServiceTest.class.getResource("/sample.csv");
            URL sample = null;
            try {
                sample = new URL("file:///home/ynikolaiko/Downloads/amazon-fine-foods/Reviews.csv");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            manager.sparkService.load(sample);
        }
        return manager.sparkService;
    }
}
