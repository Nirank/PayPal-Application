package com.paypal.wallet_service.repository;

import com.paypal.wallet_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndCurrency(Long userId, String inr);

    Optional<Wallet> findByUserId(Long userId);
}
