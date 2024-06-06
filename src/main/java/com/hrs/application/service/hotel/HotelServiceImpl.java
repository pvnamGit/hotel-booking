package com.hrs.application.service.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.service.hotel.HotelService;
import com.hrs.core.service.hotel.request.*;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
// import org.springframework.data.redis.core.RedisTemplate;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
  private final HotelRepository hotelRepository;
  private final HotelRoomRepository hotelRoomRepository;

  @SneakyThrows
  @Override
  public List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria) {
    List<Hotel> hotels = hotelRepository.findHotels(searchCriteria);

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
  public List<HotelRoomDetailResponse> findRooms(Long hotelId) {
    List<HotelRoom> hotelRooms = hotelRoomRepository.findByHotelId(hotelId);
    return hotelRooms.stream()
        .map(room -> HotelRoomDetailResponse.build(room))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId) {
    if (!hotelRoomRepository.validateHotelRoom(hotelId, roomId))
      throw new BadRequestException("Room not belong to hotel");
    var room = hotelRoomRepository.findById(roomId);
    return HotelRoomDetailResponse.build(room);
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
