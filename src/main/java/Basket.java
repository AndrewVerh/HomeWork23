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

public class Basket implements Serializable {
    protected String[] nameProduct;
    protected int[] prices;
    protected int[] prodAmount;

    public Basket(String[] nameProduct, int[] prices) {

        this.prices = prices;
        this.nameProduct = nameProduct;
        this.prodAmount = new int[nameProduct.length];
    }

    private Basket() {
    }

    //Метод добавления продукта и его количества в корзину
    public void addToCart(int productNumber, int amount) {  //номер продукта, штук продукта
        prodAmount[productNumber] += amount;
    }

    //Метод вывода на экран корзины продуктов
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

    //=======================НАЧАЛО РАБОТЫ С ТЕКСТОВЫМ ФАЙЛОМ=======================
    //сохраняем в текстовый файл *.txt
    public void saveTxtFile(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            out.println(prodAmount.length); //сохраняем размер массива
            for (int i = 0; i < nameProduct.length; i++) {
                out.println(nameProduct[i] + "\t" + prices[i] + "\t" + prodAmount[i]);
            }
        }
    }

    //чтение текстового файла корзины *.txt
    public static Basket loadFromTxtFile(File textFile) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {

            //считываем построчно данные, переданные текстФайлом
            int size = Integer.parseInt(reader.readLine()); //извлекаем размер массива - 1ю строку
            String[] names = new String[size];
            int[] prices = new int[size];
            int[] amounts = new int[size];

            //добавляем продукт, цену и количество в массивы
            for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                String[] parts = line.split("\t");
                names[i] = parts[0];
                prices[i] = Integer.parseInt(parts[1]);
                amounts[i] = Integer.parseInt(parts[2]);
            }
        }
        return new Basket();
    }
    //=======================КОНЕЦ РАБОТЫ С ТЕКСТОВЫМ ФАЙЛОМ=======================


    //=======================НАЧАЛО РАБОТЫ С BIN-ФАЙЛОМ=======================
    //Запись файла в формат *.bin
    public void saveBinFile(File textFile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(textFile))) {
            oos.writeObject(this);
        }
    }

    //Чтение *.bin-файла
    public static Basket loadFromBinFile(File textFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(textFile))) {
            return (Basket) ois.readObject();
        }
    }
    //=======================КОНЕЦ РАБОТЫ С BIN-ФАЙЛОМ=======================


    //=======================НАЧАЛО РАБОТЫ С JSON-ФАЙЛОМ=======================
    //Запись файла в формат *.json
    public void saveJSON(File textFile) throws IOException {

        try (PrintWriter writer = new PrintWriter(textFile)) {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            writer.println(json);
        }
    }

    //Чтение файла формата *.json
    public static Basket loadFromJson(File textFile) throws IOException, ParseException {

        GsonBuilder gsb = new GsonBuilder();
        Gson gson = gsb.create();
        Basket basket = gson.fromJson(new FileReader(textFile), Basket.class);
        out.println(gson.toJson(basket));

        return basket;
    }
    //=======================КОНЕЦ РАБОТЫ С JSON-ФАЙЛОМ=======================

}
