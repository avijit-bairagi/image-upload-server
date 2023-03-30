package com.ovi.EnkaizenTest.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "IMAGES")
@Data
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "THUMBNAIL_FILE_NAME")
    private String thumbnailFileName;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "INSERTED_AT")
    private LocalDateTime insertedAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
