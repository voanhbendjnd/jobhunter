package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Setter
@Getter
public class ResFetchResumeDTO {
    private Long id;
    private String email;
    private String url;
    private StatusEnum status;
    private Instant updatedAt;
    private Instant createdAt;
    private String updatedBy;
    private String createdBy;
    private String companyName;
    private UserDTO user;
    private JobDTO job;

    @Getter
    @Setter
    public static class UserDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    public static class JobDTO {
        private Long id;
        private String name;
    }
}
