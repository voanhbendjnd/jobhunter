package vn.hoidanit.jobhunter.controller;

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

import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResFetchJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a new Job")
    public ResponseEntity<ResCreateJobDTO> createANewJob(@Valid @RequestBody Job job) throws IdInvalidException {

        return ResponseEntity.ok(this.jobService.create(job));
    }

    @PutMapping("jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        if (!this.jobService.existsById(job)) {
            throw new IdInvalidException("Id khong ton tai");
        }
        return ResponseEntity.ok(this.jobService.update(job));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch Job by Id")
    public ResponseEntity<Job> fetchJob(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.jobService.existsById(id)) {
            throw new IdInvalidException("Id khong ton tai!!!!");
        }
        return ResponseEntity.ok(this.jobService.fetchById(id));
    }

    @GetMapping("/jobs")
    @ApiMessage("Fetch all Job")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.fetchAll(pageable, spec));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete Jop")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.jobService.existsById(id)) {
            throw new IdInvalidException("Id khong ton tai!!!");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
