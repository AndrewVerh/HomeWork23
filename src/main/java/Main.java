import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, ParserConfigurationException, SAXException, XPathExpressionException, ClassNotFoundException {

        String[] listOfProducts = {"Хлеб", "Яблоки", "Молоко 2,5%"};
        int[] prices = {40, 95, 49}; // 0 - 40, 1 - 95, 2 - 49
        int[] countIndex = new int[prices.length]; //массив хранения количества товара по позициям (индексам)

        // Вывод списка продуктов с ценами на экран
        System.out.println("Список возможных продуктов:");
        for (int i = 0; i < listOfProducts.length; i++) {
            System.out.println((i + 1) + ". " + listOfProducts[i] + " - " + prices[i] + " руб./шт");
        }

        //Начальные значения номера продукта и его количества
        int productNumber = 0;
        int productCount = 0;

//==================Извлечение блока <load> из файла shop.xml==================
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        boolean isLoadEnabled = Boolean.parseBoolean(xPath
                .compile("config/load/enabled")
                .evaluate(doc));
        String loadFileName = xPath
                .compile("config/load/fileName")
                .evaluate(doc);
        String loadFormat = xPath
                .compile("config/load/format")
                .evaluate(doc);

        if (isLoadEnabled) {
            switch (loadFormat) {
                case "text" -> Basket.loadFromTxtFile(new File(loadFileName));
                case "bin" -> Basket.loadFromBinFile(new File(loadFileName));
                case "json" -> Basket.loadFromJson(new File(loadFileName));
            }
        } else {
            Basket basket = new Basket(listOfProducts, prices);
        }
//==================Конец извлечения блока <load> файла shop.xml==================

        //Создаем объекты класса корзины
        Basket basket = new Basket(listOfProducts, prices);

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
            productNumber = Integer.parseInt(parts[0]) - 1; //это числовой индекс продукта
            productCount = Integer.parseInt(parts[1]); //это множитель количества этого продукта

            //Добавляем количество продуктов в корзину
            basket.addToCart(productNumber, productCount);

            //=========Извлечение блока <save> из файла shop.xml=========

            boolean isSaveEnabled = Boolean.parseBoolean(xPath
                    .compile("config/save/enabled")
                    .evaluate(doc));
            String saveFileName = xPath
                    .compile("config/save/fileName")
                    .evaluate(doc);
            String saveFormat = xPath
                    .compile("config/save/format")
                    .evaluate(doc);

            if (isSaveEnabled) {
                switch (saveFormat) {
                    case "text" -> basket.saveTxtFile(new File(saveFileName));
                    case "bin" -> basket.saveBinFile(new File(saveFileName));
                    case "json" -> basket.saveJSON(new File(saveFileName));
                }
            }
            //=========Конец извлечения блока <save> из файла shop.xml=========

        } //конец цикла while(true)

//==================Извлечение блока <log> из файла shop.xml==================

        //Создаем объект класса ведения покупок
        ClientLog cl = new ClientLog();

        //Сохранение покупки клиентом
        cl.log(productNumber, productCount);

        boolean isLogEnabled = Boolean.parseBoolean(xPath
                .compile("config/log/enabled")
                .evaluate(doc));
        String logFileName = xPath
                .compile("config/log/fileName")
                .evaluate(doc);

        if (isLogEnabled) {
            //экспорт в файл *.csv
            cl.exportAsCSV(new File(logFileName));
        }
//==================Конец извлечения блока <log> из файла shop.xml==================

        // выводим корзину на экран
        basket.printCart();
    }
}



