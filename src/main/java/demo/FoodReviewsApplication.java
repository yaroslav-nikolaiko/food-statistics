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
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

/*        URL sample = null;
        try {
            //String input = args.getOptionValues("input").get(0);
            //sample = Paths.get(input).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        String sample = args.getOptionValues("input").get(0);
        sparkService.load(sample);

        mostActiveUsers();
        mostCommentedFoodItems();
        mostUsedWordsInReview();

        if(args.getOptionValues("translateOutput")!=null && args.getOptionValues("translateOutput").size()>0){
            String filePath = args.getOptionValues("translateOutput").get(0);
            Files.deleteIfExists(new File(filePath).toPath());

            FileWriter fileWritter = new FileWriter(filePath,true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);


            Iterator<Review> reviewIterator = translator.translate(sparkService.iterator());

            AtomicInteger counter = new AtomicInteger();
            reviewIterator.forEachRemaining(r->{
                if(r!=null){
                    if(counter.getAndIncrement()%5000 == 0)
                        System.out.println("Translated Id "+r.getId());
                    appendToFile(r.toCsv(), bufferWritter);
                }
            });

            bufferWritter.close();
        }
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
