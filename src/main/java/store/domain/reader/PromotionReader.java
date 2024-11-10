package store.domain.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.policies.Promotion;
import store.domain.reader.constants.PromotionReaderValue;

public class PromotionReader {
    private final static String VALUE_DELIMITER = ",";
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public List<Promotion> read(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        return addPromotions(br);
    }

    private List<Promotion> addPromotions(BufferedReader br) throws IOException {
        try (br) {
            return br.lines()
                    .skip(PromotionReaderValue.SKIP_LINE.getValue())
                    .map(line -> createProduct(line.split(VALUE_DELIMITER)))
                    .collect(Collectors.toList());
        }
    }

    private Promotion createProduct(String[] splitLine) {
        String name = splitLine[PromotionReaderValue.NAME.getValue()];
        int buy = Integer.parseInt(splitLine[PromotionReaderValue.BUY.getValue()]);
        int get = Integer.parseInt(splitLine[PromotionReaderValue.GET.getValue()]);
        LocalDateTime startDate = getLocalDateTime(splitLine[PromotionReaderValue.START_DATE.getValue()]);
        LocalDateTime endDate = getLocalDateTime(splitLine[PromotionReaderValue.END_DATE.getValue()]);

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private LocalDateTime getLocalDateTime(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)).atStartOfDay();
    }

}
