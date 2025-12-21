package com.paypal.reward_service.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double points;
    private LocalDateTime sentAt;

    @Column(unique = true)
    private Long transactionId;

}
