package com.example.Backend.Entity;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private List<Comment> replyComment;

    private String idPost;

    private User user;

    private String content;

    private Date createAt;
    private User userReply;

    public String generateId() {
        // Tạo một id ngẫu nhiên cho đối tượng Comment
        return UUID.randomUUID().toString();
    }

}
