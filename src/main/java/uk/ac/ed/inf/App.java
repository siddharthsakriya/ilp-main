package uk.ac.ed.inf;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java -jar PizzaDronz-1.0-SNAPSHOT.jar <date> <url>");
            System.exit(1);
        }
        String date = args[0];
        String url = args[1];
    }
}
