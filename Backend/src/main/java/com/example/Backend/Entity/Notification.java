package com.example.Backend.Entity;

import com.example.Backend.Entity.model.NotificationOfUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notification")
public class Notification {
    @Id
    private String id;
    private String userId;
    private List<NotificationOfUser> notificationList;
}
