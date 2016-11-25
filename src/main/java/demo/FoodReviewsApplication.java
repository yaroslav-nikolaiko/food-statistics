package demo;

import demo.model.Review;
import demo.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

@SpringBootApplication
@Component
public class FoodReviewsApplication implements ApplicationRunner{
    public static void main(String[] args) {
        SpringApplication.run(FoodReviewsApplication.class, args);
    }

    @Autowired
    TranslatorService translator;
    @Autowired
    SparkService sparkService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.getSourceArgs().length == 0)
            return;

        URL sample = null;
        try {
            sample = new URL("file:///home/yaroslav/Documents/Reviews.csv");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        sparkService.load(sample);

        //mostActiveUsers();
        //mostCommentedFoodItems();
        //mostUsedWordsInReview();

        String filePath = "/tmp/result.csv";
        Files.deleteIfExists(new File(filePath).toPath());

        FileWriter fileWritter = new FileWriter(filePath,true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);


        Iterator<Review> reviewIterator = translator.translate(sparkService.iterator());

        reviewIterator.forEachRemaining(r->{
            //if(atomicInteger.getAndIncrement()%10000 == 0)
            //System.out.println(r.getText());
            appendToFile(r.toCsv(), bufferWritter);
        });

        bufferWritter.close();
        System.exit(0);
    }

    void mostActiveUsers(){
        System.out.println("**************mostActiveUsers***************");
        Map<String, Integer> stringIntegerMap = sparkService.mostActiveUsers(1000);
        stringIntegerMap.forEach((k, v)-> System.out.println(k+" : "+v));
    }

    void mostCommentedFoodItems(){
        System.out.println("**************mostCommentedFoodItems***************");
        Map<String, Integer> stringIntegerMap = sparkService.mostCommentedFoodItems(1000);
        stringIntegerMap.forEach((k, v)-> System.out.println(k+" : "+v));
    }

    void mostUsedWordsInReview(){
        System.out.println("**************mostUsedWordsInReview***************");
        Map<String, Integer> stringIntegerMap = sparkService.mostCommentedFoodItems(1000);
        stringIntegerMap.forEach((k, v)-> System.out.println(k+" : "+v));
    }

    void appendToFile(String text, BufferedWriter bufferWritter){
        try {
            bufferWritter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
