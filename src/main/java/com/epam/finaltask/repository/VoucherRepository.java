package com.epam.finaltask.repository;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID>, JpaSpecificationExecutor<Voucher> {

    @Query("select v from Voucher v order by v.isHot desc")
    List<Voucher> findAllOrderByIsHotDesc();
    List<Voucher> findAllByUserId(UUID userId);
    List<Voucher> findAllByTourTypeOrderByIsHotDesc(TourType tourType);
    List<Voucher> findAllByTransferTypeOrderByIsHotDesc(TransferType transferType);
    List<Voucher> findAllByHotelTypeOrderByIsHotDesc(HotelType hotelType);
    List<Voucher> findAllByPriceLessThanEqualOrderByIsHotDesc(Double price);


    Page<Voucher> findAllByUserId(UUID userId, Pageable pageable);
}
