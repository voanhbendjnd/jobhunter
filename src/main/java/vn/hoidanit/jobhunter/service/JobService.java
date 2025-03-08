package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Company;
import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResFetchJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public Job createNewCompany(Job job) {
        return this.jobRepository.save(job);
    }

    public ResFetchJobDTO fetch(Long id) {
        Job job = this.jobRepository.findById(id).get();
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        Job currentJob = this.jobRepository.save(job);
        ResFetchJobDTO dto = new ResFetchJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setDescription(currentJob.getDescription());

        dto.setActive(currentJob.isActive());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream()
                    .map(it -> it.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public Job fetchById(Long id) {
        return this.jobRepository.findById(id).get();

    }

    public ResCreateJobDTO create(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }
        Job currentJob = this.jobRepository.save(job);
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setDescription(currentJob.getDescription());

        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream()
                    .map(it -> it.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }

    public ResUpdateJobDTO update(Job job) {

        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        Job resJob = this.jobRepository.findById(job.getId()).get();
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                resJob.setCompany(companyOptional.get());
            }
        }

        resJob.setName(job.getName());
        resJob.setSalary(job.getSalary());
        resJob.setLocation(job.getLocation());
        resJob.setLevel(job.getLevel());
        resJob.setStartDate(job.getStartDate());
        resJob.setEndDate(job.getEndDate());
        resJob.setDescription(job.getDescription());
        resJob.setActive(job.isActive());
        resJob.setUpdatedAt(job.getUpdatedAt());
        resJob.setUpdatedBy(job.getUpdatedBy());
        this.jobRepository.save(resJob);
        // Job currentJob = this.jobRepository.save(job);

        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(resJob.getId());
        dto.setName(resJob.getName());
        dto.setSalary(resJob.getSalary());
        dto.setLocation(resJob.getLocation());
        dto.setLevel(resJob.getLevel());
        dto.setStartDate(resJob.getStartDate());
        dto.setEndDate(resJob.getEndDate());
        dto.setDescription(resJob.getDescription());
        dto.setActive(resJob.isActive());
        dto.setUpdatedAt(resJob.getUpdatedAt());
        dto.setUpdatedBy(resJob.getUpdatedBy());
        if (resJob.getSkills() != null) {
            List<String> skills = resJob.getSkills()
                    .stream()
                    .map(it -> it.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;

    }

    public boolean existsById(Job job) {
        return this.jobRepository.existsById(job.getId()) ? true : false;
    }

    public boolean existsById(Long id) {
        return this.jobRepository.existsById(id) ? true : false;
    }

    public ResultPaginationDTO fetchAll(Pageable pageable, Specification<Job> spec) {
        Page<Job> jobs = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rp = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(jobs.getTotalPages());
        mt.setTotal(jobs.getTotalElements());

        rp.setMeta(mt);
        rp.setResult(jobs.getContent());
        return rp;

    }
}
