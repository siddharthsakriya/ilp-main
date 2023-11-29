package uk.ac.ed.inf.unit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.handlers.OrderHandler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class OrderTest extends TestCase {
    // Declare a variable to store an instance of the Order class
    private Restaurant[] definedRestaurants;
    private OrderHandler orderValidator;

    // This method runs before every test method
    protected void setUp() throws Exception {
        super.setUp();
        definedRestaurants = new Restaurant[] {
                new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597,55.945535152517735),
                        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)}),
                new Restaurant("Sora Lella Vegan Restaurant", new LngLat(-3.202541470527649,55.943284737579376),
                        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                        new Pizza[] {new Pizza("Meat Lover", 1400), new Pizza("Vegan Delight", 1100)}),
                new Restaurant("Domino's Pizza - Edinburgh - Southside", new LngLat(-3.1838572025299072,55.94449876875712),
                        new DayOfWeek[] {DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)}),
                new Restaurant("Soderberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                        new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)})
        };
        orderValidator = new OrderHandler();
    }
    public void testCardNumberInvalidLength() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("123456789012345",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCardNumberInvalidLength2() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("12345678911012345",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCardNumberInvalidLength3() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891101234528282828",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCardNumberInvalidLength4() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891101234528282828",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCardNumberInvalidType() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("abbdhdyhsahhshds",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCardNumberInvalidType1() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838aaha",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCardNumberInvalidType2() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2ajaja38838aaha",
                "12/25",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testExpiryDate(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "09/23",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testExpiryDate2(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/23",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testExpiryDate3(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "1b123",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testExpiryDate4(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "1b1bsbsb",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testExpiryDate5(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "19/02",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCVV1(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "167379"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCVV2(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "bdbd"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCVV3(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "1d3"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testCVV4(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.NO_ERROR,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "1293"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testTotal(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.NO_ERROR,
                1100,
                new Pizza[] {new Pizza("Margarita", 111000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.TOTAL_INCORRECT, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testTotal1(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.NO_ERROR,
                1000,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/26",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.TOTAL_INCORRECT, orderInvalidCardNumber.getOrderValidationCode());
    }
    public void testPizzaUndefinedName(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }

    public void testMaxPizzaExceeded(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }

    public void testPizzaFromMultiple(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2500,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Proper Pizza", 1400)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }

    public void testRestaurantClosed(){
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1500,
                new Pizza[] {new Pizza("Super Cheese", 1400)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }
}
