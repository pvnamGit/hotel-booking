package com.hrs.core.repository.hotel;

import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.repository.shared.BaseRepository;

import java.util.List;

public interface HotelRoomRepository extends BaseRepository<HotelRoom, Long> {
    List<HotelRoom> findByHotelId(Long hotelId);
    boolean validateHotelRoom(Long hotelId, Long roomId);
}
