import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[36m";

    public static void main(String[] args) throws IOException {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                try {
                    sekUsdRate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000);

    }


    public static void sekUsdRate() throws IOException {
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

        String str =  String.valueOf(sekObject.get("changes"));
        System.out.println("ticker : USD/SEK ");
        System.out.println("Price : " +sekObject.get("high"));
        System.out.println((Double.valueOf(str) > 0) ? ANSI_GREEN + "change : " +str + ANSI_RESET : ANSI_RED + "change : " + str  + ANSI_RESET );


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
