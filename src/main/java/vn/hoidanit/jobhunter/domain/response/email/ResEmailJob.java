package vn.hoidanit.jobhunter.domain.response.email;

import java.util.List;

import org.springframework.core.annotation.AliasFor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResEmailJob {
    private String name;
    private double salary;
    private ComapnyEmail company;
    private List<SkillEmail> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ComapnyEmail { // job.company.name
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillEmail {
        private String name;
    }
}
