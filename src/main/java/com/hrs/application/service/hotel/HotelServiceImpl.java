package com.hrs.application.service.hotel;

import com.hrs.application.dto.hotel.request.HotelSearchCriteria;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.service.hotel.HotelService;
import com.hrs.application.dto.hotel.response.HotelDetailResponse;
import com.hrs.application.dto.hotel.response.HotelRoomDetailResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
  private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

  private final HotelRepository hotelRepository;
  private final HotelRoomRepository hotelRoomRepository;

  @SneakyThrows
  @Override
  public List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria) {
    logger.info("findHotels called with search criteria: {}", searchCriteria);

    List<Hotel> hotels = hotelRepository.findHotels(searchCriteria);

    logger.info("Found {} hotels for the search criteria.", hotels.size());
    return hotels.stream().map(HotelDetailResponse::build).collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public HotelDetailResponse getHotelDetail(Long id) {
    logger.info("getHotelDetail called for hotel ID: {}", id);

    var hotel = getHotelById(id);

    logger.info("Returning details for hotel ID: {}", id);
    return HotelDetailResponse.build(hotel);
  }

  @SneakyThrows
  @Override
  public List<HotelRoomDetailResponse> findRooms(Long hotelId) {
    logger.info("findRooms called for hotel ID: {}", hotelId);

    List<HotelRoom> hotelRooms = hotelRoomRepository.findByHotelId(hotelId);

    logger.info("Found {} rooms for hotel ID: {}", hotelRooms.size(), hotelId);
    return hotelRooms.stream().map(HotelRoomDetailResponse::build).collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId) {
    logger.info("getRoomDetail called for hotel ID: {} and room ID: {}", hotelId, roomId);

    if (!hotelRoomRepository.validateHotelRoom(hotelId, roomId)) {
      logger.error("Room ID: {} does not belong to hotel ID: {}", roomId, hotelId);
      throw new IllegalArgumentException("Room does not belong to hotel");
    }

    var room = hotelRoomRepository.findById(roomId);

    logger.info("Returning details for room ID: {} in hotel ID: {}", roomId, hotelId);
    return HotelRoomDetailResponse.build(room);
  }

  @Override
  public Hotel getHotelById(Long id) {
    logger.info("getHotelById called for hotel ID: {}", id);

    var hotel = hotelRepository.findById(id);
    if (hotel == null) {
      logger.error("Hotel with ID: {} not found", id);
      throw new EntityNotFoundException("Hotel not found");
    }

    return hotel;
  }

  @Override
  public HotelRoom getHotelRoomById(Long hotelId, Long roomId) {
    logger.info("getHotelRoomById called for hotel ID: {} and room ID: {}", hotelId, roomId);

    hotelRoomRepository.validateHotelRoom(hotelId, roomId);

    var hotelRoom = hotelRoomRepository.findById(roomId);
    if (hotelRoom == null) {
      logger.error("Room with ID: {} not found in hotel ID: {}", roomId, hotelId);
      throw new EntityNotFoundException("Room not found");
    }

    return hotelRoom;
  }
}
