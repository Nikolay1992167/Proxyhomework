package util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Constants {

    public static final UUID CAR_UUID = UUID.fromString("573f63f9-7f16-4c61-a9c0-a53423cd2eee");
    public static final String CAR_NAME = "Камаз";
    public static final String CAR_DESCRIPTION = "Перспективный автомобиль.";
    public static final BigDecimal CAR_PRICE = new BigDecimal(5);
    public static final LocalDateTime CAR_CREATED = LocalDateTime.of(2023, 10, 27, 18, 30, 0);
    public static final String  UPDATE_CAR_NAME = "Маруся";
    public static final String UPDATE_CAR_DESCRIPTION = "Был отличный автомобиль.";
    public static final BigDecimal UPDATE_CAR_PRICE = new BigDecimal(15);
}
