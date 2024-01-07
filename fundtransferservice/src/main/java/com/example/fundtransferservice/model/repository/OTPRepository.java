package com.example.fundtransferservice.model.repository;

import com.example.fundtransferservice.model.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

//    @Query("SELECT o FROM OTP o WHERE o.otpValue = :otpValue")
//    OTP findByOtpValue(@Param("otpValue") String otpValue);

    OTP findByOtpValue(String otpValue);
    void deleteByOtpValue(String otpValue);

}
