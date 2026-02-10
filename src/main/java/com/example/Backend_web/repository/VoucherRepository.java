package com.example.Backend_web.repository;


import com.example.Backend_web.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    //Kiểm tra mã Voucher còn tồn tại hay không
    Optional<Voucher> findByCodeAndStatusTrue(String code);

    //Lấy Voucher còn hiệu lực (status=True)
    boolean existsByCode(String code);

    Optional<Voucher> findByCode(String code);

}
