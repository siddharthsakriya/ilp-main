package uk.ac.ed.inf;
import uk.ac.ed.inf.handlers.DeliveryHandler;

public class App 
{
    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.exit(1);
        }
        String date = args[0];
        String url = args[1];
        DeliveryHandler.deliverOrders(date, url);
    }
}
