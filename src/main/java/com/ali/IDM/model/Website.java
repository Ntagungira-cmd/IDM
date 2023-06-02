package com.ali.IDM.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "websites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Website {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="uuid-char")
    @Column(columnDefinition = "VARCHAR(255)")
    private UUID id;

    private String website_name;

    URL url;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime download_start_date_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime download_end_date_time;

    @Column(nullable = true)
    private Long  total_elapsed_time;

    @Column(nullable = true)
    private Long  total_downloaded_kilobytes;
}
