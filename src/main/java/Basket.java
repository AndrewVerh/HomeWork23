import java.io.*;

import static java.lang.System.out;

public class Basket implements Serializable {

    protected int[] prices;
    protected String[] nameProduct;
    protected int[] prodAmount;

    public Basket(int[] prices, String[] nameProduct, int[] prodAmount) {
        this.prices = prices;
        this.nameProduct = nameProduct;
        this.prodAmount = prodAmount;

    }

    public void saveBin(File file) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    static Basket loadFromBinFile(File file) throws Exception {
        Basket basket = null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            basket = (Basket) ois.readObject();

        } catch (Exception e) {
            out.println(e.getMessage());
        }
        return basket;
    }

}
