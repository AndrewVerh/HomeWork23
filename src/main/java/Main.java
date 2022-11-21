import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        String[] listOfProducts = {"Хлеб", "Яблоки", "Молоко 2,5%"};
        int[] prices = {40, 95, 49}; // 0 - 40, 1 - 95, 2 - 49
        int[] countIndex = new int[prices.length]; //массив хранения количества товара по позициям (индексам)

        System.out.println("Список возможных продуктов:");
        for (int i = 0; i < listOfProducts.length; i++) {
            System.out.println((i + 1) + ". " + listOfProducts[i] + " - " + prices[i] + " руб./шт");
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("Выберите товар и количество в формате ХХ ХХ или введите «end»");
            String input = scanner.nextLine();

            if ("end".equals(input)) {
                break;
            }

            Basket basket = new Basket(prices, listOfProducts, countIndex);
            basket.saveBin(new File ("basket.bin"));

            Basket.loadFromBinFile(new File ("basket.bin"));
        }
    }
}



