package vn.hoidanit.jobhunter.controller;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Entity.Company;
import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Resume;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    public ResumeController(ResumeService resumeService, UserService userService, UserRepository userRepository,
            FilterSpecificationConverter filterSpecificationConverter, FilterBuilder filterBuilder) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new Resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume)
            throws IdInvalidException {
        if (!this.resumeService.existsByIdUser(resume.getUser().getId())
                || !this.resumeService.existsByIdJob(resume.getJob().getId())) {
            throw new IdInvalidException("User hoặc Job chưa tồn tại!!!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.resCreateResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResumeById(@RequestBody Resume resume) throws IdInvalidException {
        if (this.resumeService.findById(resume) == null) {
            throw new IdInvalidException("Resume không tồn tại!!!");
        }
        return ResponseEntity.ok(this.resumeService.resUpdateResume(resume));

    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResumeById(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.resumeService.existsById(id)) {
            throw new IdInvalidException("Resume không tồn tại!!!");
        }
        this.resumeService.deleteResume(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch By Id")
    public ResponseEntity<ResFetchResumeDTO> fetchResume(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.resumeService.existsById(id)) {
            throw new IdInvalidException("Không tìm thấy CV này!!!");
        }
        return ResponseEntity.ok(this.resumeService.resFetchResumeDTO(id));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all Resume")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok(this.resumeService.resultPaginationDTO(pageable, spec));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        List<Long> arrJobIds = null; // job id
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userRepository.findByEmail(email);

        // return ResponseEntity.ok(this.resumeService.fetchResumeByUser(pageable));
        if (currentUser != null) {
            Company companyUser = currentUser.getCompany();
            if (companyUser != null) {
                List<Job> companyJobs = companyUser.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }
        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job") // entity
                // resume job
                // field chọn cột job trong resume
                .in(filterBuilder.input(arrJobIds)).get());
        Specification<Resume> finalSpec = jobInSpec.and(spec);

        // select * from resumes where job in(1,2,3)
        return ResponseEntity.ok().body(this.resumeService.resultPaginationDTO(pageable, finalSpec));

    }

}
