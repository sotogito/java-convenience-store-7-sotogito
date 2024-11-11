package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import store.constants.OrderInputForm;
import store.domain.policies.Promotion;
import store.domain.reader.constants.PromotionReaderValue;

public class PromotionReader {
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public List<Promotion> read(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        return addPromotions(br);
    }

    private List<Promotion> addPromotions(BufferedReader br) throws IOException {
        try (br) {
            return br.lines()
                    .skip(PromotionReaderValue.SKIP_LINE.get())
                    .map(line -> createProduct(line.split(OrderInputForm.ORDER_DELIMITER.get())))
                    .collect(Collectors.toList());
        }
    }

    private Promotion createProduct(String[] splitLine) {
        String name = splitLine[PromotionReaderValue.NAME.get()];
        int buy = Integer.parseInt(splitLine[PromotionReaderValue.BUY.get()]);
        int get = Integer.parseInt(splitLine[PromotionReaderValue.GET.get()]);
        LocalDateTime startDate = getLocalDateTime(splitLine[PromotionReaderValue.START_DATE.get()]);
        LocalDateTime endDate = getLocalDateTime(splitLine[PromotionReaderValue.END_DATE.get()]);

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private LocalDateTime getLocalDateTime(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)).atStartOfDay();
    }

}
