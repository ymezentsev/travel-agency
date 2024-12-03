package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource
public class VoucherServiceImplTest {
    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private VoucherMapper voucherMapper;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Test
    void createVoucher_Success() {
        // Given
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("Summer Escape");
        voucherDTO.setDescription("Enjoy a summer escape.");
        voucherDTO.setPrice(299.99);
        voucherDTO.setTourType(TourType.ECO.name());
        voucherDTO.setTransferType(TransferType.PRIVATE_CAR.name());
        voucherDTO.setHotelType(HotelType.FOUR_STARS.name());
        voucherDTO.setStatus(VoucherStatus.CANCELED.name());
        voucherDTO.setArrivalDate(LocalDate.of(2023, 7, 15));
        voucherDTO.setEvictionDate(LocalDate.of(2023, 7, 20));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot("false");

        Voucher voucher = Voucher.builder()
                .title(voucherDTO.getTitle())
                .description(voucherDTO.getDescription())
                .price(voucherDTO.getPrice())
                .tourType(TourType.ECO)
                .transferType(TransferType.PRIVATE_CAR)
                .status(VoucherStatus.CANCELED)
                .arrivalDate(voucherDTO.getArrivalDate())
                .evictionDate(voucherDTO.getEvictionDate())
                .user(null)
                .isHot(false)
                .build();


        when(voucherMapper.toVoucher(any(VoucherDTO.class))).thenReturn(voucher);
        when(voucherRepository.save(any(Voucher.class))).thenReturn(voucher);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);

        // When
        VoucherDTO createdVoucherDTO = voucherService.create(voucherDTO);

        // Then
        assertNotNull(createdVoucherDTO, "The created VoucherDTO should not be null");
        assertEquals(voucherDTO.getTitle(), createdVoucherDTO.getTitle(), "The title should match the input");

        verify(voucherRepository, times(1)).save(any(Voucher.class));
        verify(voucherMapper, times(1)).toVoucher(any(VoucherDTO.class));
        verify(voucherMapper, times(1)).toVoucherDTO(any(Voucher.class));
    }

    @Test
    void updateVoucher_VoucherExists_Success() {
        // Given
        String id = UUID.randomUUID().toString();

        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("Updated Title");
        voucherDTO.setDescription("Updated Description");
        voucherDTO.setPrice(399.99);
        voucherDTO.setTourType(TourType.SAFARI.name());
        voucherDTO.setTransferType(TransferType.PRIVATE_CAR.name());
        voucherDTO.setHotelType(HotelType.FOUR_STARS.name());
        voucherDTO.setStatus(VoucherStatus.PAID.name());
        voucherDTO.setArrivalDate(LocalDate.now());
        voucherDTO.setEvictionDate(LocalDate.now().plusDays(5));
        voucherDTO.setIsHot("true");

        Voucher existingVoucher = new Voucher();
        existingVoucher.setTitle("Existing Title");
        existingVoucher.setDescription("Existing Description");
        existingVoucher.setPrice(199.99);
        existingVoucher.setTourType(TourType.ECO);
        existingVoucher.setTransferType(TransferType.JEEPS);
        existingVoucher.setHotelType(HotelType.FOUR_STARS);
        existingVoucher.setStatus(VoucherStatus.REGISTERED);
        existingVoucher.setArrivalDate(LocalDate.now());
        existingVoucher.setEvictionDate(LocalDate.now().plusDays(5));
        existingVoucher.setHot(true);


        Voucher updatedVoucher = new Voucher();
        updatedVoucher.setTitle(voucherDTO.getTitle());
        updatedVoucher.setDescription(voucherDTO.getDescription());
        updatedVoucher.setTourType(TourType.valueOf(voucherDTO.getTourType()));
        updatedVoucher.setTransferType(TransferType.valueOf(voucherDTO.getTransferType()));
        updatedVoucher.setHotelType(HotelType.valueOf(voucherDTO.getHotelType()));
        updatedVoucher.setStatus(VoucherStatus.valueOf(voucherDTO.getStatus()));
        updatedVoucher.setArrivalDate(voucherDTO.getArrivalDate());
        updatedVoucher.setEvictionDate(voucherDTO.getEvictionDate());
        updatedVoucher.setHot(Boolean.parseBoolean(voucherDTO.getIsHot()));

        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(existingVoucher));
        when(voucherMapper.toVoucher(any(VoucherDTO.class))).thenReturn(updatedVoucher);
        when(voucherRepository.save(any(Voucher.class))).thenReturn(updatedVoucher);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);

        // When
        VoucherDTO resultDTO = voucherService.update(id, voucherDTO);

        // Then
        assertNotNull(resultDTO, "The returned VoucherDTO should not be null");
        assertEquals(voucherDTO.getTitle(), resultDTO.getTitle(), "The title should match the updated value");

        verify(voucherRepository, times(1)).findById(UUID.fromString(id));
        verify(voucherRepository, times(1)).save(any(Voucher.class));
        verify(voucherMapper, times(1)).toVoucher(any(VoucherDTO.class));
        verify(voucherMapper, times(1)).toVoucherDTO(any(Voucher.class));
    }

    @Test
    void updateVoucher_VoucherDoesNotExist_ThrowEntityNotFoundException() {
        // Given
        String id = UUID.randomUUID().toString();
        VoucherDTO voucherDTO = new VoucherDTO();

        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> voucherService.update(id, voucherDTO),
                "Expected EntityNotFoundException to be thrown if the voucher is not found");

        verify(voucherRepository, times(1)).findById(UUID.fromString(id));
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void deleteVoucher_VoucherExists_Success() {
        // Given
        String voucherId = UUID.randomUUID().toString();
        Voucher existingVoucher = new Voucher();
        when(voucherRepository.findById(UUID.fromString(voucherId))).thenReturn(Optional.of(existingVoucher));

        // When
        voucherService.delete(voucherId);

        // Then
        verify(voucherRepository, times(1)).findById(UUID.fromString(voucherId));
        verify(voucherRepository, times(1)).deleteById(UUID.fromString(voucherId));
    }

    @Test
    void deleteVoucher_VoucherDoesNotExist_ThrowEntityNotFoundException() {
        // Given
        String voucherId = UUID.randomUUID().toString();
        when(voucherRepository.findById(UUID.fromString(voucherId))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> voucherService.delete(voucherId),
                "Expected EntityNotFoundException to be thrown if the voucher is not found");

        verify(voucherRepository, times(1)).findById(UUID.fromString(voucherId));
        verify(voucherRepository, never()).deleteById(UUID.fromString(voucherId));
    }

    @Test
    void changeHotStatus_VoucherExists_Success() {
        // Given
        String id = UUID.randomUUID().toString();
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setIsHot("true");
        voucherDTO.setStatus("REGISTERED");

        Voucher existingVoucher = new Voucher();
        existingVoucher.setHot(false);
        existingVoucher.setStatus(VoucherStatus.REGISTERED);

        Voucher updatedVoucher = new Voucher();
        updatedVoucher.setHot(Boolean.parseBoolean(voucherDTO.getIsHot()));
        updatedVoucher.setStatus(VoucherStatus.valueOf(voucherDTO.getStatus()));

        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(existingVoucher));
        when(voucherMapper.toVoucher(any(VoucherDTO.class))).thenReturn(updatedVoucher);
        when(voucherRepository.save(any(Voucher.class))).thenReturn(updatedVoucher);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);

        // When
        VoucherDTO resultDTO = voucherService.changeHotStatus(id, voucherDTO);

        // Then
        assertNotNull(resultDTO, "The returned VoucherDTO should not be null");
        assertTrue(Boolean.parseBoolean(resultDTO.getIsHot()), "The 'isHot' status should be true");
        assertEquals("REGISTERED", resultDTO.getStatus(), "The status should be updated to AVAILABLE");

        verify(voucherRepository, times(1)).findById(UUID.fromString(id));
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void changeHotStatus_VoucherDoesNotExist_ThrowEntityNotFoundException() {
        // Given
        String id = UUID.randomUUID().toString();
        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> voucherService.changeHotStatus(id, new VoucherDTO()),
                "Expected EntityNotFoundException to be thrown if the voucher is not found");

        verify(voucherRepository, times(1)).findById(UUID.fromString(id));
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void findAllByUserId_Success() {
        // Given
        String userId = UUID.randomUUID().toString();

        Voucher voucher1 = new Voucher();
        voucher1.setId(UUID.randomUUID());
        voucher1.setTitle("Adventure Trip");
        voucher1.setDescription("An exciting adventure trip.");
        voucher1.setPrice(500.0);
        voucher1.setTourType(TourType.ADVENTURE);
        voucher1.setTransferType(TransferType.BUS);
        voucher1.setHotelType(HotelType.THREE_STARS);
        voucher1.setStatus(VoucherStatus.REGISTERED);
        voucher1.setArrivalDate(LocalDate.of(2023, 5, 10));
        voucher1.setEvictionDate(LocalDate.of(2023, 5, 15));
        voucher1.setHot(true);

        Voucher voucher2 = new Voucher();
        voucher2.setId(UUID.randomUUID());
        voucher2.setTitle("Beach Holiday");
        voucher2.setDescription("A relaxing beach holiday.");
        voucher2.setPrice(750.0);
        voucher2.setTourType(TourType.LEISURE);
        voucher2.setTransferType(TransferType.PLANE);
        voucher2.setHotelType(HotelType.FIVE_STARS);
        voucher2.setStatus(VoucherStatus.PAID);
        voucher2.setArrivalDate(LocalDate.of(2023, 6, 20));
        voucher2.setEvictionDate(LocalDate.of(2023, 6, 25));
        voucher2.setHot(false);

        List<Voucher> vouchers = Arrays.asList(voucher1, voucher2);
        List<VoucherDTO> expectedVoucherDTOs = vouchers.stream()
                .map(voucher -> {
                    VoucherDTO dto = new VoucherDTO();
                    dto.setId(voucher.getId().toString());
                    dto.setTitle(voucher.getTitle());
                    dto.setDescription(voucher.getDescription());
                    dto.setPrice(voucher.getPrice());
                    dto.setTourType(String.valueOf(voucher.getTourType()));
                    dto.setTransferType(String.valueOf(voucher.getTransferType()));
                    dto.setHotelType(String.valueOf(voucher.getHotelType()));
                    dto.setStatus(String.valueOf(voucher.getStatus()));
                    dto.setArrivalDate(voucher.getArrivalDate());
                    dto.setEvictionDate(voucher.getEvictionDate());
                    dto.setIsHot(String.valueOf(voucher.isHot()));
                    return dto;
                })
                .toList();

        when(voucherRepository.findAllByUserId(UUID.fromString(userId))).thenReturn(vouchers);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenAnswer(invocation -> {
            Voucher voucher = invocation.getArgument(0);
            VoucherDTO dto = new VoucherDTO();
            dto.setId(voucher.getId().toString());
            dto.setTitle(voucher.getTitle());
            dto.setDescription(voucher.getDescription());
            dto.setPrice(voucher.getPrice());
            dto.setTourType(String.valueOf(voucher.getTourType()));
            dto.setTransferType(String.valueOf(voucher.getTransferType()));
            dto.setHotelType(String.valueOf(voucher.getHotelType()));
            dto.setStatus(String.valueOf(voucher.getStatus()));
            dto.setArrivalDate(voucher.getArrivalDate());
            dto.setEvictionDate(voucher.getEvictionDate());
            dto.setIsHot(String.valueOf(voucher.isHot()));
            return dto;
        });

        // When
        List<VoucherDTO> resultDTOs = voucherService.findAllByUserId(userId);

        // Then
        assertNotNull(resultDTOs, "The returned list of VoucherDTO should not be null");
        assertEquals(expectedVoucherDTOs.size(), resultDTOs.size(), "The size of the returned list should match the expected list");

        verify(voucherRepository, times(1)).findAllByUserId(UUID.fromString(userId));
        verify(voucherMapper, times(vouchers.size())).toVoucherDTO(any(Voucher.class));
    }

    @Test
    void findAllVouchers_Success() {
        // Given
        Voucher voucher1 = new Voucher();
        voucher1.setId(UUID.randomUUID());
        voucher1.setTitle("Summer Getaway");
        voucher1.setDescription("A summer trip to the beach");
        voucher1.setPrice(300.00);
        voucher1.setTourType(TourType.LEISURE);
        voucher1.setTransferType(TransferType.JEEPS);
        voucher1.setHotelType(HotelType.FOUR_STARS);
        voucher1.setStatus(VoucherStatus.REGISTERED);
        voucher1.setArrivalDate(LocalDate.now());
        voucher1.setEvictionDate(LocalDate.now().plusDays(7));
        voucher1.setHot(false);

        Voucher voucher2 = new Voucher();
        voucher2.setId(UUID.randomUUID());
        voucher2.setTitle("Winter Ski Adventure");
        voucher2.setDescription("Skiing in the mountains");
        voucher2.setPrice(500.00);
        voucher2.setTourType(TourType.SAFARI);
        voucher2.setTransferType(TransferType.BUS);
        voucher2.setHotelType(HotelType.FIVE_STARS);
        voucher2.setStatus(VoucherStatus.PAID);
        voucher2.setArrivalDate(LocalDate.now().plusMonths(1));
        voucher2.setEvictionDate(LocalDate.now().plusMonths(1).plusDays(7));
        voucher2.setHot(true);

        List<Voucher> vouchers = Arrays.asList(voucher1, voucher2);

        when(voucherRepository.findAll()).thenReturn(vouchers);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenAnswer(invocation -> {
            Voucher voucher = invocation.getArgument(0);
            VoucherDTO dto = new VoucherDTO();
            dto.setId(voucher.getId().toString());
            dto.setTitle(voucher.getTitle());
            dto.setDescription(voucher.getDescription());
            dto.setPrice(voucher.getPrice());
            dto.setTourType(String.valueOf(voucher.getTourType()));
            dto.setTransferType(String.valueOf(voucher.getTransferType()));
            dto.setHotelType(String.valueOf(voucher.getHotelType()));
            dto.setStatus(String.valueOf(voucher.getStatus()));
            dto.setArrivalDate(voucher.getArrivalDate());
            dto.setEvictionDate(voucher.getEvictionDate());
            dto.setIsHot(voucher.isHot() ? "true" : "false");
            return dto;
        });

        // When
        List<VoucherDTO> resultDTOs = voucherService.findAll();

        // Then
        assertNotNull(resultDTOs, "The returned list of VoucherDTO should not be null");
        assertEquals(vouchers.size(), resultDTOs.size(), "The size of the returned list should match the expected list");

        verify(voucherRepository, times(1)).findAll();
        verify(voucherMapper, times(vouchers.size())).toVoucherDTO(any(Voucher.class));
    }
}
