package com.hrs.application.service.reservation;

import com.hrs.application.dto.reservation.request.HotelReservationCreateRequest;
import com.hrs.application.dto.reservation.request.HotelReservationUpdateRequest;
import com.hrs.application.dto.reservation.response.HotelReservationDetailResponse;
import com.hrs.application.helper.reservation.ReservationValidator;
import com.hrs.core.domain.account.SecurityCurrentUser;
import com.hrs.core.domain.hotel.Hotel;
import com.hrs.core.domain.hotel.HotelRoom;
import com.hrs.core.domain.reservation.HotelReservation;
import com.hrs.core.domain.user.User;
import com.hrs.core.exception.reservation.HotelReservationErrorMessages;
import com.hrs.core.repository.hotel.HotelRoomRepository;
import com.hrs.core.repository.reservation.HotelReservationRepository;
import com.hrs.core.service.reservation.HotelReservationService;
import com.hrs.shared.enums.DateFormat;
import com.hrs.shared.helper.GenericPatcher;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelReservationServiceImpl implements HotelReservationService {
  private static final Logger logger = LoggerFactory.getLogger(HotelReservationServiceImpl.class);
  private final HotelReservationRepository hotelReservationRepository;
  private final HotelRoomRepository hotelRoomRepository;
  private final SecurityCurrentUser currentUser;
  private final ReservationValidator reservationValidator;
  @PersistenceContext private EntityManager entityManager;

  @Override
  public HotelReservationDetailResponse createReservation(HotelReservationCreateRequest request)
          throws BadRequestException {
    logger.info("Creating reservation for hotel room ID: {}", request.getHotelRoomId());
    LocalDate checkInDate =
            DateFormat.parseDate(request.getCheckInDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);
    LocalDate checkOutDate =
            DateFormat.parseDate(request.getCheckOutDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);

    boolean isConflictReservation =
            hotelReservationRepository.isConflictReservation(
                    request.getHotelRoomId(), checkInDate, checkOutDate, null);
    reservationValidator.validateConflict(isConflictReservation);

    reservationValidator.validateCheckInAndCheckOut(checkInDate, checkOutDate);
    boolean isValidRoom =
            hotelRoomRepository.validateHotelRoom(request.getHotelId(), request.getHotelRoomId());
    reservationValidator.validateHotelRoom(isValidRoom);

    Hotel hotel = entityManager.getReference(Hotel.class, request.getHotelId());
    HotelRoom hotelRoom = hotelRoomRepository.findById(request.getHotelRoomId());
    User user = entityManager.getReference(User.class, currentUser.getCurrentUser().getUserId());
    HotelReservation hotelReservation =
            HotelReservation.builder()
                    .checkInDate(checkInDate)
                    .checkOutDate(checkOutDate)
                    .user(user)
                    .hotel(hotel)
                    .hotelRoom(hotelRoom)
                    .noOfGuests(request.getNoOfGuests())
                    .totalPrice(calculateTotalPrice(checkInDate, checkOutDate, hotelRoom.getPrice()))
                    .build();
    hotelReservationRepository.persistAndFlush(hotelReservation);

    logger.info("Reservation created successfully for user: {}", currentUser.getCurrentUser().getUserId());
    return HotelReservationDetailResponse.build(hotelReservation);
  }

  @Override
  public HotelReservationDetailResponse updateReservation(
          Long id, HotelReservationUpdateRequest request) throws BadRequestException {
    logger.info("Updating reservation with ID: {}", id);
    authorized(id);

    LocalDate checkInDate =
            DateFormat.parseDate(request.getCheckInDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);
    LocalDate checkOutDate =
            DateFormat.parseDate(request.getCheckOutDate(), DateFormat.DATE_FORMAT_DD_MM_YYYY);
    reservationValidator.validateCheckInAndCheckOut(checkInDate, checkOutDate);

    boolean isConflictReservation =
            hotelReservationRepository.isConflictReservation(
                    request.getHotelRoomId(), checkInDate, checkOutDate, id);
    reservationValidator.validateConflict(isConflictReservation);

    if (request.getHotelRoomId() != null) {
      Long hotelId = hotelReservationRepository.getHotelIdByReservationId(id);
      boolean isValidRoom =
              hotelRoomRepository.validateHotelRoom(hotelId, request.getHotelRoomId());
      reservationValidator.validateHotelRoom(isValidRoom);
    }

    HotelReservation hotelReservation = getReservationById(id);
    if (hotelReservation.getCancelledAt() != null)
      throw new BadRequestException(
              HotelReservationErrorMessages.CAN_NOT_UPDATE_CANCELLED_RESERVATION.getMessage());

    GenericPatcher<HotelReservation> patcher = new GenericPatcher<>();
    HotelReservation hotelReservationUpdate =
            HotelReservation.builder()
                    .checkInDate(checkInDate)
                    .checkOutDate(checkOutDate)
                    .noOfGuests(request.getNoOfGuests())
                    .build();
    if (request.getHotelRoomId() != null) {
      HotelRoom hotelRoom = hotelRoomRepository.findById(request.getHotelRoomId());
      hotelReservationUpdate.setTotalPrice(
              (calculateTotalPrice(checkInDate, checkOutDate, hotelRoom.getPrice())));
      hotelReservation.setHotelRoom(hotelRoom);
    }
    patcher.patch(hotelReservation, hotelReservationUpdate);
    hotelReservation.setUpdatedAt(Instant.now().toEpochMilli());
    hotelReservationRepository.merge(hotelReservation);

    logger.info("Reservation with ID: {} updated successfully", id);
    return HotelReservationDetailResponse.build(hotelReservation);
  }

  @Override
  public List<HotelReservationDetailResponse> getReservations() {
    logger.info("Fetching reservations for user: {}", currentUser.getCurrentUser().getUserId());
    List<HotelReservation> hotelReservations =
            hotelReservationRepository.findReservationsByUserId(
                    currentUser.getCurrentUser().getUserId());
    return hotelReservations.stream()
            .map(HotelReservationDetailResponse::build)
            .collect(Collectors.toList());
  }

  @Override
  public HotelReservationDetailResponse getReservationDetail(Long id)
          throws EntityNotFoundException, AccessDeniedException {
    logger.info("Fetching details for reservation ID: {}", id);
    authorized(id);
    HotelReservation hotelReservation = getReservationById(id);
    return HotelReservationDetailResponse.build(hotelReservation);
  }

  @Override
  public HotelReservationDetailResponse cancelReservation(Long id) throws EntityNotFoundException {
    logger.info("Canceling reservation with ID: {}", id);
    authorized(id);
    HotelReservation hotelReservation = getReservationById(id);
    hotelReservation.setCancelledAt(Instant.now().toEpochMilli());
    hotelReservation.setUpdatedAt(Instant.now().toEpochMilli());
    hotelReservationRepository.merge(hotelReservation);

    logger.info("Reservation with ID: {} canceled successfully", id);
    return HotelReservationDetailResponse.build(hotelReservation);
  }

  private Double calculateTotalPrice(
          LocalDate checkInDate, LocalDate checkOutDate, @Nullable Double price) {
    var noOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    if (price == null) price = 0.0;
    var totalPrice = Math.round(noOfDays * price * 100.0) / 100.0;
    DecimalFormat df = new DecimalFormat("#.##");
    return totalPrice;
  }

  private void authorized(Long id) throws EntityNotFoundException, AccessDeniedException {
    logger.debug("Checking if user is authorized for reservation ID: {}", id);
    if (!hotelReservationRepository.authorizeUser(id, currentUser.getCurrentUser().getUserId()))
      throw new AccessDeniedException(HotelReservationErrorMessages.UNAUTHORIZED.getMessage());
  }

  private HotelReservation getReservationById(Long id) {
    logger.debug("Fetching reservation with ID: {}", id);
    HotelReservation reservation = hotelReservationRepository.findById(id);
    if (reservation == null) throw new EntityNotFoundException();
    return reservation;
  }
}
