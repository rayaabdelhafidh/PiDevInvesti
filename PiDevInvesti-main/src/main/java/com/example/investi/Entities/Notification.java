package com.example.investi.Entities;



import jakarta.persistence.*;
import lombok.*;

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
