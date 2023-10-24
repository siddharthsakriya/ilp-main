package uk.ac.ed.inf.model;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.List;
public class OrderValidator implements uk.ac.ed.inf.ilp.interfaces.OrderValidation{
    /**
     *
     * @param orderToValidate
     * @param definedRestaurants
     * @return orderToValidate if it is valid, null otherwise
     */
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants){
        if (!isCardNumInvalid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return orderToValidate;
        }
        if (!isSizeValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return orderToValidate;
        }
        if (!isExpiryValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return orderToValidate;
        }
        if (!isCVVValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return orderToValidate;
        }
        if (isPizzaValid(orderToValidate, definedRestaurants) == 1){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            return orderToValidate;
        }
        if (isPizzaValid(orderToValidate, definedRestaurants) == 2){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return orderToValidate;
        }
        if (!isTotalCostValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return orderToValidate;
        }
        Restaurant orderRestaurant = getOrderRestraunt(orderToValidate, definedRestaurants);
        if(orderRestaurant == null){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return orderToValidate;
        }
        if(isRestrauntOpen(orderRestaurant, orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            return orderToValidate;
        }
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return orderToValidate;
    }

    /**
     *
     * @param order
     * @return true if card number is valid, false otherwise
     */
    private static Boolean isCardNumInvalid(Order order){
        String cardNum = order.getCreditCardInformation().getCreditCardNumber();
        if (cardNum.length() != 16 || !isNum(cardNum)){
            return false;
        }
        return true;
    }
    /**
     *
     * @param order
     * @return true if size is valid, false otherwise
     */
    private static Boolean isSizeValid(Order order){
        Pizza[] pizzas = order.getPizzasInOrder();
        if (pizzas.length > SystemConstants.MAX_PIZZAS_PER_ORDER){
            return false;
        }
        return true;
    }

    /**
     *
     * @param order
     * @return true if expiry date is valid, false otherwise
     */
    private static Boolean isExpiryValid(Order order){
        String creditCardExpiry = order.getCreditCardInformation().getCreditCardExpiry();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        Date creditCardExpiryFormatted = simpleDateFormat.parse(creditCardExpiry, new java.text.ParsePosition(0));
        Date orderDate = new Date();
        orderDate = simpleDateFormat.parse(simpleDateFormat.format(orderDate), new java.text.ParsePosition(0));
        if(creditCardExpiryFormatted != null){
            return creditCardExpiryFormatted.after(orderDate) || creditCardExpiryFormatted.equals(orderDate);
        }
        return false;
    }

    /**
     *
     * @param order
     * @return true if CVV is valid, false otherwise
     */
    private static Boolean isCVVValid(Order order){
        String cvv = order.getCreditCardInformation().getCvv();
        if (cvv.length() != 3 || !isNum(cvv)){
            return false;
        }
        return true;
    }

    /**
     *
     * @param order
     * @param restaurants
     * @return 0 if pizza is valid, 1 if pizza is not defined, 2 if pizza is defined but price is incorrect
     */
    private static int isPizzaValid(Order order, Restaurant[] restaurants){
        HashMap<String, Integer> orderPizzaMap = new HashMap<>();
        HashMap<String, Integer> restaurantPizzaMap = new HashMap<>();
        Pizza[] pizzas = order.getPizzasInOrder();
        for (Restaurant restaurant: restaurants){
            for (Pizza pizza: restaurant.menu()){
                restaurantPizzaMap.put(pizza.name(), pizza.priceInPence());
            }
        }
        for(Pizza pizza: order.getPizzasInOrder()){
            orderPizzaMap.put(pizza.name(), pizza.priceInPence());
        }
        for(String pizzaName: orderPizzaMap.keySet()){
            if(!restaurantPizzaMap.containsKey(pizzaName)){
                return 1;
            } else if (!restaurantPizzaMap.get(pizzaName).equals(orderPizzaMap.get(pizzaName))){
                return 2;
            }
        }
        return 0;
    }
    /**
     *
     * @param order
     * @return true if total cost is valid, false otherwise
     */
    private static Boolean isTotalCostValid(Order order){
        Pizza[] pizzas = order.getPizzasInOrder();
        double correctTotal = 0;
        for(Pizza pizza : pizzas){
            correctTotal += pizza.priceInPence();
        }
        return (correctTotal+100) == order.getPriceTotalInPence();
    }
    /**
     *
     * @param order
     * @param restaurants
     * @return restaurant if order is valid, null otherwise
     */
    public static Restaurant getOrderRestraunt(Order order, Restaurant[] restaurants){
        HashSet<Pizza> pizzas = new HashSet<>();
        pizzas.addAll(List.of(order.getPizzasInOrder()));
        for (Restaurant restaurant : restaurants) {
            if (Arrays.asList(restaurant.menu()).containsAll(pizzas)){;
                return restaurant;
            }
        }
        return null;
    }
    /**
     *
     * @param restaurant
     * @param orderToValidate
     * @return true if restaurant is open, false otherwise
     */
    private static Boolean isRestrauntOpen(Restaurant restaurant, Order orderToValidate){
        DayOfWeek dayOfWeek = orderToValidate.getOrderDate().getDayOfWeek();
        if(!Arrays.asList(restaurant.openingDays()).contains(dayOfWeek)){
            return true;
        }
        return false;
    }
    /**
     *
     * @param num
     * @return true if num is a number, false otherwise
     */
    private static Boolean isNum(String num){
        try {
            Long.parseLong(num);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
