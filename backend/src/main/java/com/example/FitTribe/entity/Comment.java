package com.example.FitTribe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.util.Date;

@Data
@Entity(name = "comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(nullable = false)
  private String description;

  private long likes = 0;

  @CreationTimestamp private Date createdAt;

  @UpdateTimestamp private Date updatedAt;
}
