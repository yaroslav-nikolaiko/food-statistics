package demo;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

public class SparkService implements Serializable{
    private final String APP_NAME = "Amazon Food Statistic";
    JavaRDD<Review> reviews;

    private Parser parser;

    public void load(URL fileURL) {
        JavaSparkContext sc = new JavaSparkContext(format("local[%s]", numberOfCores()), APP_NAME);
        JavaRDD<String> csvRDD = sc.textFile(fileURL.getPath());
        this.reviews = csvRDD.map(line->parser.parse(line));
    }

    public Map<String, Integer> mostActiveUsers(int number) {
        return reviews
                .mapToPair(r -> new Tuple2<>(r.profileName, 1))
                .reduceByKey((v1, v2) -> v1 + v2)
                .mapToPair(t -> new Tuple2<>(t._2, t._1))
                .sortByKey(false)
                .take(number)
                .stream()
                .collect(toMap(Tuple2::_2, Tuple2::_1,(u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        LinkedHashMap::new));
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    Integer numberOfCores(){
        return Runtime.getRuntime().availableProcessors();
    }



}
