package com.hrs.core.repository.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.repository.shared.BaseRepository;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends BaseRepository<Hotel, Long> {
    List<Hotel> findHotels(HotelSearchCriteria criteria);
}
