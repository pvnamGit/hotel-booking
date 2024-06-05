package com.hrs.infrastructure.persistence.reservation;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.user.User;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.infrastructure.persistence.shared.BaseRepositoryImpl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

@Repository
public class HotelReservationRepositoryImpl extends BaseRepositoryImpl<HotelReservation, Long>
    implements HotelReservationRepository {
  private final String ID = "id";
  private final String CHECK_IN_DATE = "checkInDate";
  private final String CHECK_OUT_DATE = "checkOutDate";
  private final String HOTEL_ROOM = "hotelRoom";
  @PersistenceContext EntityManager entityManager;

  @SuppressWarnings("unchecked")
  public HotelReservationRepositoryImpl(EntityManager entityManager) {
    super(HotelReservation.class);
  }

  @Override
  public boolean isConflictReservation(
      Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long excludeReservationId) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery cq = createCriteriaQuery();
    Root<HotelReservation> reservationRoot = cq.from(HotelReservation.class);
    Predicate roomPredicate = cb.equal(reservationRoot.get(HOTEL_ROOM).get(ID), roomId);
    Predicate dateConflictPredicate =
        cb.or(
            cb.and(
                cb.lessThanOrEqualTo(reservationRoot.get(CHECK_IN_DATE), checkOutDate),
                cb.greaterThanOrEqualTo(reservationRoot.get(CHECK_OUT_DATE), checkInDate)));
    if (excludeReservationId != null) {
      Predicate excludePredicate = cb.notEqual(reservationRoot.get(ID), excludeReservationId);
      cq.select(cb.count(reservationRoot))
          .where(cb.and(roomPredicate, dateConflictPredicate, excludePredicate));
    } else {
      cq.select(cb.count(reservationRoot)).where(cb.and(roomPredicate, dateConflictPredicate));
    }

    TypedQuery<Long> typedQuery = entityManager.createQuery(cq);
    Long conflictCount = typedQuery.getSingleResult();
    return conflictCount > 0;
  }

  @Override
  public Long getHotelIdByReservationId(Long reservationId) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);

    Root<HotelReservation> reservationRoot = cq.from(HotelReservation.class);
    Join<HotelReservation, Hotel> hotelJoin =
        reservationRoot.join(Hotel.class.getSimpleName().toLowerCase());

    cq.select(hotelJoin.get(ID)).where(cb.equal(reservationRoot.get(ID), reservationId));

    return entityManager.createQuery(cq).getSingleResult();
  }

  @Override
  public boolean authorizeUser(Long reservationId, Long userId) {
    // Initialize the CriteriaBuilder and CriteriaQuery
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);

    // Define the root entities (Hotel and HotelRoom) and create the join
    Root<HotelReservation> reservationRoot = cq.from(HotelReservation.class);
    Join<HotelReservation, User> userJoin =
        reservationRoot.join(User.class.getSimpleName().toLowerCase(), JoinType.INNER);

    // Add predicates to filter by hotel ID and room ID
    Predicate reservationIdPredicate = cb.equal(reservationRoot.get(ID), reservationId);
    Predicate userIdPredicate = cb.equal(userJoin.get(ID), userId);

    cq.select(cb.count(userJoin)).where(cb.and(reservationIdPredicate, userIdPredicate));

    TypedQuery<Long> typedQuery = entityManager.createQuery(cq);
    Long count = typedQuery.getSingleResult();
    return count == 1;
  }

  @Override
  public List<HotelReservation> findReservationsByUserId(Long userId) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<HotelReservation> cq = cb.createQuery(HotelReservation.class);


    Root<HotelReservation> reservationRoot = cq.from(HotelReservation.class);
    Join<HotelReservation, User> userJoin = reservationRoot.join("user", JoinType.INNER); // Assuming "user" is the name of the association field in HotelReservation entity
    Predicate predicate = cb.equal(userJoin.get("id"), userId); // Assuming "id" is the name of the primary key field in User entity
    cq.where(predicate);
    TypedQuery<HotelReservation> query = entityManager.createQuery(cq);
    return query.getResultList();
  }

}
