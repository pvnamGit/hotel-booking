package com.hrs.shared.helper;

import com.hrs.core.domain.account.Account;
import com.hrs.core.domain.account.Authority;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.account.AccountRepository;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.repository.user.UserRepository;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private static final Random random = new Random();
  private static final List<String> CITIES =
      Arrays.asList(
          "Hanoi",
          "Ho Chi Minh City",
          "Da Nang",
          "Nha Trang",
          "Hai Phong",
          "Hue",
          "Can Tho",
          "Vung Tau",
          "Quy Nhon",
          "Rach Gia");
  private static final List<String> COUNTRIES =
      Arrays.asList(
          "Vietnam",
          "Thailand",
          "Malaysia",
          "Indonesia",
          "Philippines",
          "Singapore",
          "Laos",
          "Cambodia",
          "Myanmar",
          "Brunei");
  private static final List<String> ADDRESSES =
      Arrays.asList(
          "123 Main St",
          "456 High St",
          "789 Park Ave",
          "101 First St",
          "202 Second St",
          "303 Third St",
          "404 Fourth St",
          "505 Fifth St",
          "606 Sixth St",
          "707 Seventh St",
          "808 Eighth St",
          "909 Ninth St",
          "1010 Tenth St",
          "1111 Eleventh St",
          "1212 Twelfth St",
          "1313 Thirteenth St",
          "1414 Fourteenth St",
          "1515 Fifteenth St",
          "1616 Sixteenth St",
          "1717 Seventeenth St",
          "1818 Eighteenth St",
          "1919 Nineteenth St",
          "2020 Twentieth St",
          "2121 Twenty-First St",
          "2222 Twenty-Second St",
          "2323 Twenty-Third St",
          "2424 Twenty-Fourth St",
          "2525 Twenty-Fifth St",
          "2626 Twenty-Sixth St",
          "2727 Twenty-Seventh St",
          "2828 Twenty-Eighth St",
          "2929 Twenty-Ninth St",
          "3030 Thirtieth St",
          "3131 Thirty-First St",
          "3232 Thirty-Second St",
          "3333 Thirty-Third St",
          "3434 Thirty-Fourth St",
          "3535 Thirty-Fifth St",
          "3636 Thirty-Sixth St",
          "3737 Thirty-Seventh St",
          "3838 Thirty-Eighth St",
          "3939 Thirty-Ninth St",
          "4040 Fortieth St",
          "4141 Forty-First St",
          "4242 Forty-Second St");
  private static final List<String> HOTEL_NAMES =
      Arrays.asList(
          "Hilton",
          "Marriott",
          "Hyatt",
          "Sheraton",
          "InterContinental",
          "Ritz-Carlton",
          "Four Seasons",
          "Waldorf Astoria",
          "St. Regis",
          "Fairmont");

  private final boolean INITIALIZE = true;
  @Autowired private HotelRepository hotelRepository;
  @Autowired private HotelRoomRepository hotelRoomRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (!INITIALIZE) return;
    Account account =
        Account.builder()
            .email("account@gmail.com")
            .password(passwordEncoder.encode("password"))
            .authority(Authority.USER)
            .build();
    accountRepository.persistAndFlush(account);
    User user = User.builder().firstName("First").lastName("Last").account(account).build();
    userRepository.persist(user);


    IntStream.range(0, 100)
        .forEach(
            i -> {
              Hotel hotel = new Hotel();
              hotel.setName(HOTEL_NAMES.get(random.nextInt(HOTEL_NAMES.size())));
              String city = CITIES.get(random.nextInt(CITIES.size()));
              hotel.setAddress(ADDRESSES.get(random.nextInt(ADDRESSES.size())) + ", " + city);
              hotel.setCity(city);
              hotel.setCountry(COUNTRIES.get(random.nextInt(COUNTRIES.size())));
              hotel.setNoOfAvailableRooms(random.nextInt(100) + 1);
              hotelRepository.persistAndFlush(hotel);

              IntStream.range(0, random.nextInt(10) + 1)
                  .forEach(
                      j -> {
                          double price = 5 + (random.nextDouble() * (100 - 5));
                          DecimalFormat df = new DecimalFormat("#.##");
                          price = Double.parseDouble(df.format(price));
                        HotelRoom hotelRoom = new HotelRoom();
                        hotelRoom.setHotel(hotel);
                        hotelRoom.setCode("Room_" + (i + 1) + "_" + (j + 1));
                        hotelRoom.setNoOfBeds(random.nextInt(3) + 1);
                        hotelRoom.setNoOfBedrooms(random.nextInt(2) + 1);
                        hotelRoom.setNoOfBathrooms(random.nextInt(2) + 1);
                        hotelRoom.setPrice(price);
                        hotelRoomRepository.persist(hotelRoom);
                      });
            });
  }
}
