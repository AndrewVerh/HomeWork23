import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static java.lang.System.out;

public class Basket {

    protected int[] prices;
    protected String[] nameProduct;
    protected int[] prodAmount;
//    protected int number;
//    protected int amount;

    public Basket(int[] prices, String[] nameProduct, int[] prodAmount) {
//        this.number = number;
//        this.amount = amount;
        this.prices = prices;
        this.nameProduct = nameProduct;
        this.prodAmount = prodAmount;
    }

    public void addToCart(int productNumber, int amount) {  //номер продукта, штук продукта
        prodAmount[productNumber] += amount;
    }

    public void printCart() {
        out.println("В корзину добавлено:");
        int sumProducts = 0;

        for (int i = 0; i < nameProduct.length; i++) {
            int currSum = prices[i] * prodAmount[i];
            out.println(prodAmount[i] + " шт. " + nameProduct[i] + " - " + prices[i] + "руб./шт, на сумму - " + currSum + " руб.");
            sumProducts = sumProducts + prices[i] * prodAmount[i];
        }
        out.println("Общая сумма покупок: " + sumProducts + " руб.");

    }

    //Запись файла в формат *.json
    public void saveJSON(File textFile) throws IOException {

        GsonBuilder gsb = new GsonBuilder();
        Gson gson = gsb.create();

        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write(gson.toJson(this));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Чтение файла формата *.json
    public Basket loadFromJson (File textFile) throws IOException, ParseException {

        GsonBuilder gsb = new GsonBuilder();
        Gson gson = gsb.create();
        Basket basket = gson.fromJson(new FileReader(textFile), Basket.class);
        out.println(gson.toJson(basket));

        return basket;
    }



    static Basket loadFromTxtFile(File textFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            //считываем построчно данные, переданные текстФайлом
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();

            //Создание массива строк из 1й строки и перевод его значений в число
            String[] exeptedPriceArr = line1.split("@");
            int[] exeptedPriceArrInt = new int[exeptedPriceArr.length];
            for (int i = 0; i < exeptedPriceArr.length; i++) {
                exeptedPriceArrInt[i] = Integer.parseInt(exeptedPriceArr[i]);
            }
            //Создание массива строк из 2й строки
            String[] exeptedNameProduct = line2.split("@");

            //Создание массива строк из 3й строки и перевод его значений в число
            String[] exeptedCountProduct = line3.split("@");
            int[] exeptedCountProductInt = new int[exeptedCountProduct.length];
            for (int i = 0; i < exeptedCountProduct.length; i++) {
                exeptedCountProductInt[i] = Integer.parseInt(exeptedCountProduct[i]);
            }

            //Вывод на экран всех строк, извлеченных из файла
            for (String s : exeptedPriceArr) {
                System.out.print(s + " руб./шт., ");
            }
            System.out.print("\n");
            for (String s : exeptedNameProduct) {
                System.out.print(s + ", ");
            }
            System.out.print("\n");
            for (String s : exeptedCountProduct) {
                System.out.print(s + " шт., ");
            }
            return new Basket(exeptedPriceArrInt, exeptedNameProduct, exeptedCountProductInt);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
