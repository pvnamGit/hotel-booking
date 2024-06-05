package com.hrs.core.repository.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.repository.shared.BaseRepository;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;

import java.util.List;

public interface HotelRepository extends BaseRepository<Hotel, Long> {
    List<Hotel> findAvailableHotels(HotelSearchCriteria criteria);
}
