package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

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
    private CompanyDTO company;

    @Getter
    @Setter
    public static class CompanyDTO {
        private Long id;
        private String name;
    }

    // @PreUpdate
    // public void handleBeforeUpdate() {

    // this.updateAt = Instant.now();
    // }
}
