import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static final String REMOTE_SERVICE_URI_TEMPLATE = "https://api.nasa.gov/planetary/apod?api_key=%s";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        System.out.println("Чтение данных API NASA");
        Scanner scan = new Scanner(System.in);

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
//        String apiKey = scan.nextLine();
        String apiKey ="avEbO6UJBmgDE9r0d8qAmx6MaUOInVrl96uWAJn8";
        String remoteServiceURL = String.format(REMOTE_SERVICE_URI_TEMPLATE, apiKey);

        HttpGet request = new HttpGet(remoteServiceURL);


        try {
            CloseableHttpResponse response = httpClient.execute(request);

            //String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            String body = new String(response.getEntity().getContent().readAllBytes());
            System.out.println(body);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(body);
            JSONObject objJSON = (JSONObject) obj;
            String url = (String)objJSON.get("url");


            int position = url.lastIndexOf("/");
            int position2 = url.lastIndexOf("?");
            String name = url.substring(position + 1, position2);
            System.out.println(name);

            String url2 = String.format("https://img.youtube.com/vi/%s/maxresdefault.jpg", name);
            BufferedImage img = ImageIO.read(new URL(url2));
            File outputfile = new File(name + ".jpg");
            ImageIO.write(img, "jpg", outputfile);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


    }
}