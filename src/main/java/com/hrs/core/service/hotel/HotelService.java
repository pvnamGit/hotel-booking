package com.hrs.core.service.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.service.hotel.request.*;
import com.hrs.core.service.hotel.response.HotelDetailResponse;
import com.hrs.core.service.hotel.response.HotelRoomDetailResponse;
import java.util.List;

public interface HotelService {
    HotelDetailResponse createHotel(HotelCreateRequest request);
    HotelDetailResponse updateHotel(Long id, HotelUpdateRequest request);
    List<HotelDetailResponse> findHotels(HotelSearchCriteria searchCriteria);
    HotelDetailResponse getHotelDetail(Long id);
    void deleteHotel(Long id);
    Hotel getHotelById(Long id);

    HotelRoomDetailResponse createRoom(Long hotelId, HotelRoomCreateRequest request);
    HotelRoomDetailResponse updateRoom(Long hotelId, Long roomId, HotelRoomUpdateRequest request);
    List<HotelRoomDetailResponse> findRooms(Long hotelId);
    HotelRoomDetailResponse getRoomDetail(Long hotelId, Long roomId);
    void deleteRoom(Long hotelId, Long roomId);
    HotelRoom getHotelRoomById(Long hotelId, Long roomId);


}
