package net.thrymr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
@Getter
@Audited
@Document
public class BaseDocument {

    @Id
    private String id;

    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdOn = LocalDateTime.now(ZoneId.of("IST", ZoneId.SHORT_IDS));

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedOn = LocalDateTime.now(ZoneId.of("IST", ZoneId.SHORT_IDS));

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    private Boolean isDeleted = Boolean.FALSE;

    private Boolean isActive = Boolean.TRUE;

    private String searchKey;
}