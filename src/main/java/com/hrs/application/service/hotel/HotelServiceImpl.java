package com.hrs.application.service.hotel;

import com.hrs.shared.helper.GenericPatcher;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.*;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
  private final RedisTemplate redisTemplate;
  private final HotelRepository hotelRepository;
  private final HotelRoomRepository hotelRoomRepository;
  private final String HOTEL_SEARCH_CATCH_PREFIX = "HOTEL_SEARCH_";

  @Override
  @SneakyThrows
  public HotelDetailResponse createHotel(HotelCreateRequest request) {
    Hotel hotel =
        Hotel.builder()
            .name(request.getName())
            .city(request.getCity())
            .country(request.getCountry())
            .address(request.getAddress())
            .noOfAvailableRooms(request.getNoOfAvailableRooms())
            .build();
    hotelRepository.persistAndFlush(hotel);
    return HotelDetailResponse.build(hotel);
  }

  @Override
  @SneakyThrows
  public HotelDetailResponse updateHotel(Long id, HotelUpdateRequest request) {
    var hotel = getHotelById(id);
    GenericPatcher<Hotel> patcher = new GenericPatcher<>();
    Hotel hotelUpdate =
        Hotel.builder()
            .name(request.getName())
            .city(request.getCity())
            .country(request.getCountry())
            .address(request.getAddress())
            .noOfAvailableRooms(request.getNoOfAvailableRooms())
            .noOfRooms(0)
            .build();
    patcher.patch(hotel, hotelUpdate);
    hotel.setUpdatedAt(Instant.now().toEpochMilli());
    hotelRepository.merge(hotel);
    return HotelDetailResponse.build(hotel);
  }

  @SneakyThrows
  @Override
//  @Cacheable(cacheNames = "hotel_search")
  public List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria) {
    List<Hotel> hotels = hotelRepository.findAvailableHotels(searchCriteria);

    return hotels.stream()
        .map(hotel -> HotelDetailResponse.build(hotel))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public HotelDetailResponse getHotelDetail(Long id) {
    var hotel = getHotelById(id);
    return HotelDetailResponse.build(hotel);
  }

  @SneakyThrows
  @Override
  public void deleteHotel(Long id) {
    hotelRepository.softDelete(id);
  }

  @SneakyThrows
  @Override
  public HotelRoomDetailResponse createRoom(Long hotelId, HotelRoomCreateRequest request) {
    var hotel = getHotelById(hotelId);
    HotelRoom hotelRoom =
        HotelRoom.builder()
            .code(request.getCode())
            .noOfBathrooms(request.getNoOfBathrooms())
            .noOfBedrooms(request.getNoOfBedrooms())
            .noOfBeds(request.getNoOfBeds())
            .hotel(hotel)
            .build();
    hotelRoomRepository.persist(hotelRoom);
    // increase room
    hotel.increaseRoom();
    hotel.setUpdatedAt(Instant.now().toEpochMilli());
    hotelRepository.merge(hotel);
    return HotelRoomDetailResponse.build(hotelRoom);
  }

  @SneakyThrows
  @Override
  public HotelRoomDetailResponse updateRoom(
      Long hotelId, Long roomId, HotelRoomUpdateRequest request) {
    var hotelRoom = getHotelRoomById(hotelId, roomId);
    HotelRoom hotelRoomUpdate =
        HotelRoom.builder()
            .code(request.getCode())
            .noOfBathrooms(request.getNoOfBathrooms())
            .noOfBedrooms(request.getNoOfBedrooms())
            .noOfBeds(request.getNoOfBeds())
            .build();
    GenericPatcher<HotelRoom> patcher = new GenericPatcher<>();
    patcher.patch(hotelRoom, hotelRoomUpdate);
    hotelRoom.setUpdatedAt(Instant.now().toEpochMilli());
    hotelRoomRepository.merge(hotelRoom);
    return HotelRoomDetailResponse.build(hotelRoom);
  }

  @SneakyThrows
  @Override
  public List<HotelRoomDetailResponse> findRooms(Long hotelId) {
    List<HotelRoom> hotelRooms = hotelRoomRepository.findByHotelId(hotelId);
    return hotelRooms.stream()
        .map(room -> HotelRoomDetailResponse.build(room))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId) {
    getHotelById(hotelId);
    var room = hotelRoomRepository.findById(roomId);
    return HotelRoomDetailResponse.build(room);
  }

  @SneakyThrows
  @Override
  public void deleteRoom(Long hotelId, Long roomId) {
    var hotel = getHotelById(hotelId);
    hotelRoomRepository.softDelete(roomId);
    // decrease room
    hotel.decreaseRoom();
    hotel.setUpdatedAt(Instant.now().toEpochMilli());
    hotelRepository.merge(hotel);
  }

  @Override
  public Hotel getHotelById(Long id) {
    var hotel = hotelRepository.findById(id);
    if (hotel == null) throw new EntityNotFoundException();
    return hotel;
  }

  @Override
  public HotelRoom getHotelRoomById(Long hotelId, Long roomId) {
    hotelRoomRepository.validateHotelRoom(hotelId, roomId);
    var hotelRoom = hotelRoomRepository.findById(roomId);
    if (hotelRoom == null) throw new EntityNotFoundException();
    return hotelRoom;
  }
}
