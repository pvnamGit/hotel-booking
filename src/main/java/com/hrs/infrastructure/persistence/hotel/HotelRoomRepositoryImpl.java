package com.hrs.infrastructure.persistence.hotel;

import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.infrastructure.persistence.shared.BaseRepositoryImpl;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

@Repository
public class HotelRoomRepositoryImpl extends BaseRepositoryImpl<HotelRoom, Long> implements HotelRoomRepository {
    private final String ID = "id";
    private final String HOTEL_ROOM = "hotelRooms";
    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public HotelRoomRepositoryImpl(EntityManager entityManager) {
        super(HotelRoom.class);
    }

    @Override
    public List<HotelRoom> findByHotelId(Long hotelId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery cq = createCriteriaQuery();
        Root<HotelRoom> root = getRoot(cq);
        Join<HotelRoom, Hotel> hotel = root.join(Hotel.class.getSimpleName().toLowerCase(), JoinType.LEFT);
        Predicate predicate = cb.equal(hotel.get(ID), hotelId);
        cq.where(predicate);
        TypedQuery<HotelRoom> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public boolean validateHotelRoom(Long hotelId, Long roomId) {
        // Initialize the CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        // Define the root entities (Hotel and HotelRoom) and create the join
        Root<Hotel> hotelRoot = cq.from(Hotel.class);
        Join<Hotel, HotelRoom> hotelRoomJoin = hotelRoot.join(HOTEL_ROOM, JoinType.INNER);

        // Add predicates to filter by hotel ID and room ID
        Predicate hotelIdPredicate = cb.equal(hotelRoot.get(ID), hotelId);
        Predicate roomIdPredicate = cb.equal(hotelRoomJoin.get(ID), roomId);

        cq.select(cb.count(hotelRoomJoin))
                .where(cb.and(hotelIdPredicate, roomIdPredicate));

        TypedQuery<Long> typedQuery = entityManager.createQuery(cq);
        Long count = typedQuery.getSingleResult();
        return count == 1;
    }
}
