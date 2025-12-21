package com.paypal.wallet_service.service;

import com.paypal.wallet_service.dto.*;
import com.paypal.wallet_service.entity.Transaction;
import com.paypal.wallet_service.entity.Wallet;
import com.paypal.wallet_service.entity.WalletHold;
import com.paypal.wallet_service.exeption.InsufficientFundsException;
import com.paypal.wallet_service.exeption.NotFoundException;
import com.paypal.wallet_service.repository.TransactionRepository;
import com.paypal.wallet_service.repository.WalletHoldRepository;
import com.paypal.wallet_service.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final WalletHoldRepository walletHoldRepository;
    private final TransactionRepository transactionRepository ;

    public WalletServiceImpl(WalletRepository walletRepository, WalletHoldRepository walletHoldRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.walletHoldRepository = walletHoldRepository;
        this.transactionRepository = transactionRepository;
    }


    @Transactional
    @Override
    public WalletResponse createWallet(CreateWalletRequest request){
        Wallet wallet = new Wallet(request.getUserId(),request.getCurrency());
        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletResponse(savedWallet.getId(), savedWallet.getUserId(),savedWallet.getCurrency(),
                savedWallet.getBalance(), savedWallet.getAvailableBalance());
    }

    @Transactional
    @Override
    public WalletResponse credit(CreditRequest request){
        System.out.println("Credit request received:"+request);
        Wallet wallet = walletRepository.findByUserIdAndCurrency(request.getUserId(),"INR")
                .orElseThrow(()->new RuntimeException("Wallet not found for user:"+request.getUserId()));

        wallet.setBalance(wallet.getBalance()+request.getAmount());
        wallet.setAvailableBalance(wallet.getAvailableBalance()+request.getAmount());

        Wallet savedWallet = walletRepository.save(wallet);

        Long amount = request.getAmount();

        transactionRepository.save(new Transaction(savedWallet.getId(),"CREDIT",amount,"SUCCESS"));

        System.out.println("Credited amount:"+amount+" to wallet:"+savedWallet.getId());

        return new WalletResponse(savedWallet.getId(), savedWallet.getUserId(), savedWallet.getCurrency(),
                savedWallet.getBalance(), savedWallet.getAvailableBalance());
    }


    @Transactional
    @Override
    public WalletResponse debit(DebitRequest request){
        System.out.println("Debit request received: "+request);
        Wallet wallet = walletRepository.findByUserIdAndCurrency(request.getUserId(),"INR")
                .orElseThrow(()->new RuntimeException("Wallet not found for user:"+request.getUserId()));

        if(wallet.getAvailableBalance()<request.getAmount()){
            throw new InsufficientFundsException("Insufficient balance in wallet");
        }

        wallet.setBalance(wallet.getBalance()-request.getAmount());
        wallet.setAvailableBalance(wallet.getAvailableBalance()-request.getAmount());

        Wallet savedWallet = walletRepository.save(wallet);

        transactionRepository.save(new Transaction(savedWallet.getId(),"DEBIT", request.getAmount(), "SUCCESS"));

        System.out.println("Debited amount: "+request.getAmount()+" to wallet: "+savedWallet.getId());

        return new WalletResponse(savedWallet.getId(), savedWallet.getUserId(), savedWallet.getCurrency(),
                savedWallet.getBalance(), savedWallet.getAvailableBalance());
    }

    @Override
    public WalletResponse getWallet(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(()->new NotFoundException("Wallet not found for user:"+userId));

        return new WalletResponse(wallet.getId(), wallet.getUserId(),wallet.getCurrency(),
                wallet.getBalance(), wallet.getAvailableBalance());
    }

    @Override
    @Transactional
    public HoldResponse placeHold(HoldRequest request ){

        Wallet wallet = walletRepository.findByUserIdAndCurrency(request.getUserId(), request.getCurrency())
                .orElseThrow(()->new NotFoundException("Wallet not found for user:"+request.getUserId()));

        if(wallet.getAvailableBalance()<request.getAmount()){
            throw new InsufficientFundsException("Not enough balance in wallet to place hold.");
        }

        wallet.setAvailableBalance(wallet.getAvailableBalance()-request.getAmount());

        WalletHold walletHold = new WalletHold();
        walletHold.setWallet(wallet);
        walletHold.setAmount(request.getAmount());
        walletHold.setHoldReference("HOLD_"+System.currentTimeMillis());
        walletHold.setStatus("ACTIVE");

        walletHoldRepository.save(walletHold);
        walletRepository.save(wallet);

        return new HoldResponse(walletHold.getHoldReference(), walletHold.getStatus(), walletHold.getAmount());
    }

    @Override
    @Transactional
    public WalletResponse captureHold(CaptureRequest request ){

        WalletHold hold = walletHoldRepository.findByHoldReference(request.getHoldReference())
                .orElseThrow(()->new NotFoundException("Wallet not found for user:"+request.getHoldReference()));

        if(!"ACTIVE".equalsIgnoreCase (hold.getStatus())){
            throw new IllegalStateException("Hold is not active.");
        }

        Wallet wallet = hold.getWallet();

        wallet.setBalance(wallet.getBalance()-hold.getAmount());
        hold.setStatus("CAPTURED");

        walletHoldRepository.save(hold);
        walletRepository.save(wallet);

        return new WalletResponse(wallet.getId(), wallet.getUserId(), wallet.getCurrency(),
                wallet.getBalance(), wallet.getAvailableBalance());
    }

    @Override
    @Transactional
    public HoldResponse releaseHold(String ref) {
        WalletHold hold = walletHoldRepository.findByHoldReference(ref).orElseThrow(()->new NotFoundException("Hold not found for reference:"+ref));
        if(!"ACTIVE".equalsIgnoreCase (hold.getStatus())){
            throw new IllegalStateException("Hold is not active.");
        }

        Wallet wallet = hold.getWallet();

        wallet.setBalance(wallet.getBalance()+hold.getAmount());
        hold.setStatus("RELEASED");

        walletHoldRepository.save(hold);
        walletRepository.save(wallet);

        return new HoldResponse(hold.getHoldReference(), hold.getStatus(), hold.getAmount());
    }
}
