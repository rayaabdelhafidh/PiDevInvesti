package com.example.investiprojet.entities;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;



}
