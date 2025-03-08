package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Resume;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResFetchJobDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.convert.FormatRes.FormatResultPagaination;

@Service
public class ResumeService {

    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public boolean existsByIdUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.isPresent() ? true : false;
    }

    public boolean existsByIdJob(Long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        return jobOptional.isPresent() ? true : false;
    }

    public Resume createResume(Resume resume) {
        return this.resumeRepository.save(resume);
    }

    public boolean existsById(Long id) {
        return this.resumeRepository.existsById(id) ? true : false;
    }

    public ResCreateResumeDTO resCreateResume(Resume resume) {

        this.resumeRepository.save(resume);
        ResCreateResumeDTO dto = new ResCreateResumeDTO();
        dto.setId(resume.getId());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        return dto;

    }

    public Resume findById(Resume resume) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getId());
        return resumeOptional.isPresent() ? resumeOptional.get() : null;
    }

    public ResUpdateResumeDTO resUpdateResume(Resume resume) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getId());
        resumeOptional.get().setStatus(resume.getStatus());
        this.resumeRepository.save(resumeOptional.get());

        ResUpdateResumeDTO dto = new ResUpdateResumeDTO();
        dto.setUpdateBy(resumeOptional.get().getUpdatedBy());
        dto.setUpdatedAt(resumeOptional.get().getUpdatedAt());
        return dto;

    }

    public void deleteResume(Long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO resFetchResumeDTO(Long id) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        ResFetchResumeDTO dto = new ResFetchResumeDTO();
        dto.setId(resumeOptional.get().getId());
        dto.setEmail(resumeOptional.get().getEmail());
        dto.setUrl(resumeOptional.get().getUrl());
        dto.setStatus(resumeOptional.get().getStatus());
        dto.setCreatedAt(resumeOptional.get().getCreatedAt());
        dto.setUpdatedAt(resumeOptional.get().getUpdatedAt());
        dto.setCreatedBy(resumeOptional.get().getCreatedBy());
        dto.setUpdatedBy(resumeOptional.get().getUpdatedBy());
        if (resumeOptional.get().getJob() != null) {
            dto.setCompanyName(resumeOptional.get().getJob().getCompany().getName());
        }
        ResFetchResumeDTO.UserDTO user = new ResFetchResumeDTO.UserDTO();
        user.setId(resumeOptional.get().getUser().getId());
        user.setName(resumeOptional.get().getUser().getName());
        ResFetchResumeDTO.JobDTO job = new ResFetchResumeDTO.JobDTO();
        job.setId(resumeOptional.get().getJob().getId());
        job.setName(resumeOptional.get().getJob().getName());
        dto.setUser(user);
        dto.setJob(job);
        return dto;

    }

    public ResultPaginationDTO resultPaginationDTO(Pageable pageable, Specification<Resume> spec) {
        Page<Resume> resumes = this.resumeRepository.findAll(spec, pageable);
        return FormatResultPagaination.fetchAll(resumes);
    }

    // lấy tất cả cv của người dùng đang đăng nhập
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse(String.format("email='%s'", email)); // lọc cái email
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node); // chuyển từ node sang spec
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageResume.getContent());
        return rs;

    }
}
