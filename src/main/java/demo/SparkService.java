package demo;

import demo.model.Review;
import demo.parser.Parser;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

public class SparkService implements Serializable{
    private final String APP_NAME = "Amazon Food Statistic";
    private JavaRDD<Review> reviews;

    private Parser parser;

    public void load(URL fileURL) {
        JavaSparkContext sc = new JavaSparkContext(format("local[%s]", numberOfCores()), APP_NAME);
        JavaRDD<String> csvRDD = sc.textFile(fileURL.getPath());
        this.reviews = csvRDD.map(line->parser.parse(line));
        this.reviews = dropHeader(reviews);
    }

    public Iterator<Review> iterator(){
        return reviews.toLocalIterator();
    }

    public Map<String, Integer> mostActiveUsers(int number) {
        return reviews
                .mapToPair(r -> new Tuple2<>(r.getProfileName(), 1))
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

    public Map<String, Integer> mostCommentedFoodItems(int number) {
        //TODO:remove redundant mapToPair
        /*
        Could not find sort() not by key...so have to use mapToPair to swap key value
         */
        return reviews
                .mapToPair(r -> new Tuple2<>(r.getItemID(), 1))
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

    public Map<String, Integer> mostUsedWordsInReview(int number) {
        //TODO:remove redundant mapToPair
        /*
        Could not find sort() not by key...so have to use mapToPair to swap key value
         */
        return reviews
                .flatMap(r -> asList(r.getText().split("\\s+")).iterator())
                .map(String::toLowerCase)
                .mapToPair(word -> new Tuple2<>(word, 1))
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

    JavaRDD<Review> dropHeader(JavaRDD<Review> reviews){
        Review first = reviews.first();
        return reviews.filter(row -> !row.equals(first));
    }
}
