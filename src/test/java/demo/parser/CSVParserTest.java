package demo.parser;

import demo.model.Review;
import demo.parser.CSVParser;
import demo.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVParserTest {
    @Test
    public void csvParserTest(){
        Parser parser = new CSVParser();
        String line = "4,B000UA0QIQ,A395BORC6FGVXV,Karl,3,3,2,1307923200,Cough Medicine,If you are looking for the secret";
        Review expected = new Review(4l, "Karl", "B000UA0QIQ", "If you are looking for the secret");

        Review review = parser.parse(line);

        assertEquals(expected, review);
    }

    @Test
    public void csvParserDoubleQuotesTest(){
        Parser parser = new CSVParser();
        String line = "4,B000UA0QIQ,A395BORC6FGVXV,Karl,3,3,2,1307923200,Cough Medicine,\"If you are looking for the secret\"";
        Review expected = new Review(4L,"Karl", "B000UA0QIQ", "If you are looking for the secret");

        Review review = parser.parse(line);

        assertEquals(expected, review);
    }

    @Test
    public void csvParserCommaInTextTest(){
        Parser parser = new CSVParser();
        String line = "4,B000UA0QIQ,A395BORC6FGVXV,Karl,3,3,2,1307923200,Cough Medicine,\"If you are, looking for the secret\"";
        Review expected = new Review(4L,"Karl", "B000UA0QIQ", "If you are, looking for the secret");

        Review review = parser.parse(line);

        assertEquals(expected, review);
    }
}