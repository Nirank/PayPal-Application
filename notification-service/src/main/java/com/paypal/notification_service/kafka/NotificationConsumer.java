package com.paypal.notification_service.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.notification_service.entity.Notification;
import com.paypal.notification_service.entity.Transaction;
import com.paypal.notification_service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper mapper;

    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
        // Setup ObjectMapper with JavaTimeModule to handle LocalDateTime
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }



    @KafkaListener(topics = "txn-initiated", groupId = "notification-group",containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransaction(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);

        Notification notification = new Notification();
        notification.setUserId(transaction.getSenderId());
        notification.setMessage("₹ " + transaction.getAmount() + " received from user " + transaction.getSenderId());
        notification.setSentAt(LocalDateTime.now());

        notificationRepository.save(notification);
        System.out.println("Notification saved: " + notification);
    }

//    @KafkaListener(topics = "txn-initiated",groupId = "notification-group")
//    public void consumeTransaction(byte[] payload) throws Exception {
//
//        Transaction transaction = mapper.readValue(payload, Transaction.class);
//        System.out.println("Received transaction: " + transaction);
//        Notification notification = new Notification();
//        notification.setUserId(transaction.getSenderId());
//        notification.setMessage(
//                "₹ " + transaction.getAmount() +
//                        " received from user " + transaction.getSenderId()
//        );
//        notification.setSentAt(LocalDateTime.now());
//
//        notificationRepository.save(notification);
//        System.out.println("Notification saved: " + notification);
//    }

//    @KafkaListener(topics = "txn-initiated", groupId = "notification-group")
//    public void consumeTransaction(Transaction transaction) {
//        System.out.println("📥 Received transaction: " + transaction);
//
//        Notification notification = new Notification();
//        notification.setUserId(transaction.getSenderId());
//        notification.setMessage("💰 ₹" + transaction.getAmount() + " received from user " + transaction.getSenderId());
//        notification.setSentAt(LocalDateTime.now());
//
//        notificationRepository.save(notification);
//        System.out.println("✅ Notification saved: " + notification);
//    }

}