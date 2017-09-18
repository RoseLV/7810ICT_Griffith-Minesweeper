import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by ranlyu on 2/9/17.
 */
public class CalculatorTest {

    private static Calculator calculator = new Calculator();
    @Test
    public void add() throws Exception {
        calculator.clear();
        calculator.add(2);
        calculator.add(3);
        assertEquals(5, calculator.getResult());
    }

    @Test
    public void substract() throws Exception {
        calculator.clear();
        calculator.add(10);
        calculator.substract(2);
        assertEquals(8, calculator.getResult());
    }

    @Ignore("Multiply() Not yet implemented")
    /*@Test
    public void multiply() throws Exception {
    }
    */


    @Test
    public void divide() throws Exception {
        calculator.clear();
        calculator.add(8);
        calculator.divide(2);
        assertEquals(4, calculator.getResult());
    }

    @Test
    public void square() throws Exception {
        calculator.clear();
        calculator.square(2);
        assertEquals(4, calculator.getResult());
    }

    @Test
    public void squareRoot() throws Exception {
    }

    @Test
    public void clear() throws Exception {

        calculator.add(10);
        calculator.substract(2);
        calculator.clear();
        assertEquals(0, calculator.getResult());
    }

    @Test
    public void getResult() throws Exception {
    }

}