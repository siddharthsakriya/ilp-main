package uk.ac.ed.inf;
import uk.ac.ed.inf.handlers.DeliveryHandler;

public class App 
{
    public static void main(String[] args)
    {
        if (args.length != 1) {
            System.exit(1);
        }
        String date = args[0];
        DeliveryHandler.deliverOrders(date);
    }
}
