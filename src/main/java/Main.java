import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, ParserConfigurationException, SAXException {

        String[] listOfProducts = {"Хлеб", "Яблоки", "Молоко 2,5%"};
        int[] prices = {40, 95, 49}; // 0 - 40, 1 - 95, 2 - 49
        int[] countIndex = new int[prices.length]; //массив хранения количества товара по позициям (индексам)
//        int[] sumIndex = new int[prices.length]; //массив хранения общей суммы по позициям

        // Вывод списка продуктов с ценами на экран
        System.out.println("Список возможных продуктов:");
        for (int i = 0; i < listOfProducts.length; i++) {
            System.out.println((i + 1) + ". " + listOfProducts[i] + " - " + prices[i] + " руб./шт");
        }

        //Начальные значения номера продукта и его количества
        int productNumber = 0;
        int productCount = 0;

        //Создаем объекты классов корзины и ведения покупок
        ClientLog cl = new ClientLog();
        Basket basket = new Basket(prices, listOfProducts, countIndex);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("Выберите товар и количество в формате ХХ ХХ или введите «end»");
            String input = scanner.nextLine();

            if ("end".equals(input)) {
                break;
            }

            // Разделяем введенные данные на два числа (1 -номер продукта и 2 - количество)
            String[] parts = input.split(" ");
            productNumber = Integer.parseInt(parts[0])-1; //это числовой индекс продукта
            productCount = Integer.parseInt(parts[1]); //это множитель количества этого продукта

            //Добавляем количество продуктов в корзину
            basket.addToCart(productNumber, productCount);

            //Сохранение покупки клиентом
            cl.log(productNumber, productCount);

            //экспорт в файл *.csv
            cl.exportAsCSV(new File("log.csv"));

            //экспорт в файл *.json
            basket.saveJSON(new File("basket.json"));

        }
        //выводим корзину на экран
        basket.printCart();

        basket.loadFromJson(new File("basket.json"));
    }

}



