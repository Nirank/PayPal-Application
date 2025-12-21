package com.paypal.wallet_service.service;

import com.paypal.wallet_service.dto.*;

public interface WalletService {
    HoldResponse releaseHold(String ref);

    HoldResponse placeHold(HoldRequest request);

    WalletResponse captureHold(CaptureRequest req);

    WalletResponse getWallet(Long userId);

    WalletResponse credit(CreditRequest request);

    WalletResponse debit(DebitRequest request);

    WalletResponse createWallet(CreateWalletRequest request);
}
