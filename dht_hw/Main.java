import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {

    public static void main (String [] args) {
        // System.out.println("Select operation to perform");
		// System.out.println("1. PUT");
		// System.out.println("2. GET");

        // Scanner reader = new Scanner(System.in);
        // System.out.println("Enter the operation number: ");

        // int n = reader.nextInt();
        // reader.close();

        //number of servers

        int numberOfServers = 9;

        DHT_server[] servers = new DHT_server[numberOfServers];

        DHT_server dht = new DHT_server();

        Result result = JUnitCore.runClasses(UnitTests.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
        
        //trying to test by passing arguments through a file
        Path path = Paths.get("input.txt");
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path.toString());
        System.setIn(inputStream);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String operator = sc.next();
            if (operator.equals("PUT")) {
                String key = sc.next();
                String value = sc.next();
                dht.put(Long(key), value.getBytes());
            } else { 
                String key = sc.next();
                Integer version = null;
                if (sc.hasNextInt()) {
                    version = sc.nextInt();
                }
                if (version == null) {
                    dht.get(Long(key));
                } else {
                    dht.get(Long(key), version);
                }
            }
        }
        sc.close();
        System.out.println();       
    }

    private static Long Long(String key) {
        return Long.parseLong(key);
    }
}
