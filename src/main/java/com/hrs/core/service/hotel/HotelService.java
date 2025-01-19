package com.hrs.core.service.hotel;

import com.hrs.application.dto.hotel.request.HotelSearchCriteria;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.application.dto.hotel.response.HotelDetailResponse;
import com.hrs.application.dto.hotel.response.HotelRoomDetailResponse;

import java.util.List;

public interface HotelService {
  List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria);

  HotelDetailResponse getHotelDetail(Long id);

  Hotel getHotelById(Long id);

  List<HotelRoomDetailResponse> findRooms(Long hotelId);

  HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId);
  HotelRoom getHotelRoomById(Long hotelId, Long roomId);
}
