import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.util.Scanner;




public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        Double tempBid = 0.0;
        Double tempAsk = 0.0;
        Double tempChange = 0.0;

        Scanner exit = new Scanner(System.in);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    while (true) {
                        String input = exit.nextLine();
                        if (input.equals("exit")) {
                            System.exit(0);
                        } else {
                            System.out.println("Invalid Input !! ");
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            JSONObject  newObject = sekUsdRate();
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\n This is made by Zacky, Jesper, Edis");
            System.out.println("************************************\n");
            System.out.println("ticker : USD/SEK ");
            System.out.println((newObject.getDouble("bid") > tempBid ? ANSI_GREEN + "bid : " +newObject.getDouble("bid") + ANSI_RESET : ANSI_RED + "bid : " + newObject.getDouble("bid")  + ANSI_RESET ));
            System.out.println((newObject.getDouble("ask") > tempAsk ? ANSI_GREEN + "ask : " +newObject.getDouble("ask") + ANSI_RESET : ANSI_RED + "ask : " + newObject.getDouble("ask")  + ANSI_RESET ));
            System.out.println("High : " +newObject.get("high"));
            System.out.println("Low : " +newObject.get("low"));
            System.out.println("open : " + newObject.get("open"));
            System.out.println((newObject.getDouble("changes") > tempChange ? ANSI_GREEN + "change : " +newObject.getDouble("changes") + ANSI_RESET : ANSI_RED + "change : " + newObject.getDouble("changes")  + ANSI_RESET ));
            tempAsk = newObject.getDouble("ask");
            tempBid = newObject.getDouble("bid");
            tempChange = newObject.getDouble("changes");

            System.out.println("Enter 'exit' to quit : ");
            try {
                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static JSONObject sekUsdRate() throws IOException {

        String Url = "https://financialmodelingprep.com/api/v3/forex/";
        JSONObject originalList = new JSONObject(requestURL(Url));
        originalList.get("forexList");
        JSONArray secondLevel = new JSONArray(originalList.get("forexList").toString());
        JSONObject sekObject = new JSONObject();
        for (int i = 0; i <secondLevel.length() ; i++) {
            JSONObject  jsonObject = new JSONObject(secondLevel.get(i).toString());
            if(jsonObject.get("ticker").equals("USD/SEK")){
                sekObject=jsonObject;
            }
        }
        return  sekObject;
    }



    public static String requestURL(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");
        connection.connect();

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println("The number you inserted to search is invalid... Try again");
            System.exit(0);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();

    }

}
