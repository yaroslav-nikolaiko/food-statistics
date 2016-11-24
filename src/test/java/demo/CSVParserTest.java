package demo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVParserTest {


    @Test
    public void csvParserTest(){
        Parser parser = new CSVParser();
        String line = "4,B000UA0QIQ,A395BORC6FGVXV,Karl,3,3,2,1307923200,Cough Medicine,If you are looking for the secret";
        Review expected = new Review("Karl", "B000UA0QIQ", "If you are looking for the secret");

        Review review = parser.parse(line);

        assertEquals(expected, review);
    }
}