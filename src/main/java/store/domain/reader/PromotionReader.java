package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import store.domain.items.Promotion;

public class PromotionReader {

    public List<Promotion> read(String path) throws IOException {
        List<Promotion> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line;
        boolean isFirstFieldNameLine = true;

        while ((line = br.readLine()) != null) {
            if (isFirstFieldNameLine) {
                isFirstFieldNameLine = false;
                continue;
            }
            String[] splitLine = line.split(",");

            String name = splitLine[0];
            int buy = Integer.parseInt(splitLine[1]);
            int get = Integer.parseInt(splitLine[2]);
            LocalDateTime startDate = LocalDate.parse(
                    splitLine[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(
                    splitLine[4], DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay();
            result.add(new Promotion(name, buy, get, startDate, endDate));
        }
        br.close();
        return result;
    }

}
