import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;

public class ClientLog {
//    public int productNum;
//    public int amount;

    public Collection<String []> listNumAmount = new ArrayList<>();

    public void log(int productNum, int amount) {
        String [] tmp = {Integer.toString(productNum)+1, Integer.toString(amount)};
        listNumAmount.add(tmp);
        }

    public void exportAsCSV(File txtFile) {
        // Создаем экземпляр CSVWriter
        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile))) {

            for (String [] s: listNumAmount) {
                writer.writeNext (s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
