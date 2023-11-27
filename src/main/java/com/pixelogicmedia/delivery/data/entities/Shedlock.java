package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Shedlock {
    @Id
    private String name;
    private LocalDateTime lockUntil;
    private LocalDateTime lockedAt;
    private String lockedBy;
}
