package com.hrs.core.service.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.service.hotel.request.*;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HotelService {
  List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria);

  HotelDetailResponse getHotelDetail(Long id);

  Hotel getHotelById(Long id);

  List<HotelRoomDetailResponse> findRooms(Long hotelId);

  HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId);
  HotelRoom getHotelRoomById(Long hotelId, Long roomId);
}
