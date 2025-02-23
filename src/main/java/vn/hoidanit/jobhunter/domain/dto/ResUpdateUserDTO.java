package vn.hoidanit.jobhunter.domain.dto;

import java.time.Instant;

import org.hibernate.annotations.SecondaryRow;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private Long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updateAt;

    // @PreUpdate
    // public void handleBeforeUpdate() {

    // this.updateAt = Instant.now();
    // }
}
