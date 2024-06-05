package com.hrs.infrastructure.persistence.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.repository.hotel.HotelRepository;
import com.hrs.core.service.hotel.request.HotelSearchCriteria;
import com.hrs.infrastructure.persistence.shared.BaseRepositoryImpl;
import com.hrs.shared.enums.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

@Repository
public class HotelRepositoryImpl extends BaseRepositoryImpl<Hotel, Long>
    implements HotelRepository {
  private final String ID = "id";

  private final String ADDRESS = "address";
  private final String CITY = "city";
  private final String COUNTRY = "country";
  private final String NO_OF_GUESTS = "noOfGuests";
  private final String CHECK_IN_DATE = "checkInDate";
  private final String CHECK_OUT_DATE = "checkOutDate";
  private final String NO_OF_AVAILABLE_ROOMS = "noOfAvailableRooms";
  private final String HOTEL_ROOM = "hotelRooms";

  @PersistenceContext EntityManager entityManager;

  @SuppressWarnings("unchecked")
  public HotelRepositoryImpl(EntityManager entityManager) {
    super(Hotel.class);
  }

  @Override
  public List<Hotel> findAvailableHotels(HotelSearchCriteria criteria) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery cq = createCriteriaQuery();
    Root<Hotel> hotelRoot = cq.from(Hotel.class);

    // Filtering based on search criteria
    List<Predicate> predicates = new ArrayList<>();

    // Location filtering
    if (criteria.getLocation() != null && criteria.getLocation().length() > 0) {
      Predicate addressPredicate =
          cb.like(hotelRoot.get(ADDRESS), "%" + criteria.getLocation() + "%");
      Predicate cityPredicate = cb.like(hotelRoot.get(CITY), "%" + criteria.getLocation() + "%");
      Predicate countryPredicate =
          cb.like(hotelRoot.get(COUNTRY), "%" + criteria.getLocation() + "%");
      predicates.add(cb.or(addressPredicate, cityPredicate, countryPredicate));
    }

    // Date and availability filtering
    if (criteria.getCheckInDate() != null && criteria.getCheckOutDate() != null) {

      LocalDate checkInDate =
          DateFormat.parseDate(criteria.getCheckInDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);
      LocalDate checkOutDate =
          DateFormat.parseDate(criteria.getCheckOutDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);

      Subquery<Long> availableRoomsSubquery = cq.subquery(Long.class);
      Root<HotelReservation> reservationRoot = availableRoomsSubquery.from(HotelReservation.class);
      availableRoomsSubquery.select(cb.count(reservationRoot.get(ID)));
      availableRoomsSubquery.where(
          cb.and(
              cb.equal(reservationRoot.get(Hotel.class.getSimpleName().toLowerCase()), hotelRoot),
              cb.lessThanOrEqualTo(reservationRoot.get(CHECK_IN_DATE), checkInDate),
              cb.greaterThanOrEqualTo(reservationRoot.get(CHECK_OUT_DATE), checkOutDate)));
      predicates.add(cb.greaterThan(hotelRoot.get(NO_OF_AVAILABLE_ROOMS), availableRoomsSubquery));
    }
    // Number of guests filtering
    if (criteria.getNoOfGuests() != null) {
      Join<Hotel, HotelRoom> roomJoin = hotelRoot.join(HOTEL_ROOM, JoinType.LEFT);
      predicates.add(cb.greaterThanOrEqualTo(roomJoin.get(NO_OF_GUESTS), criteria.getNoOfGuests()));
    }

    cq.select(hotelRoot).where(predicates.toArray(new Predicate[0])).distinct(true);
    TypedQuery<Hotel> query = entityManager.createQuery(cq);
    return query.getResultList();
  }
}
