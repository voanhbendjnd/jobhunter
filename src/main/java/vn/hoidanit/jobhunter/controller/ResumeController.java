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

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Entity.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
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

}
