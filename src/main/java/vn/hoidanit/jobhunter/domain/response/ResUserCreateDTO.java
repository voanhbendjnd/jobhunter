package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUserCreateDTO {
    private Long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createAt;
    private CompanyDTO company;
    private RoleDTO role;

    @Getter
    @Setter
    public static class CompanyDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    public static class RoleDTO {
        private Long id;
        private String name;
    }
}
