package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;

    // dependence injection
    public SkillService(SkillRepository skillRepository, JobRepository jobRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    // create
    public Skill createNewASkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    // update
    public Skill updatedSkill(Skill skill) {
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        if (skillOptional.isPresent()) {
            skillOptional.get().setName(skill.getName());
            return this.skillRepository.save(skillOptional.get());

        }

        return null;
    }

    public boolean existsById(Skill skill) {
        return (this.skillRepository.existsById(skill.getId())) ? true : false;
    }

    public ResultPaginationDTO fetchAllSkill(Pageable pageable, Specification<Skill> spec) {
        Page<Skill> jobs = this.skillRepository.findAll(spec, pageable);
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

    public boolean existsByName(String name) {
        return this.skillRepository.existsByName(name) ? true : false;
    }

    public Skill fetchSkillById(Long id) {

        Optional<Skill> skOptional = this.skillRepository.findById(id);
        if (skOptional.isPresent()) {
            return skOptional.get();
        } else {
            return null;
        }
    }

    public void deleteSkill(Long id) {
        // this.skillRepository.deleteById(id);
        Optional<Skill> skOptional = this.skillRepository.findById(id);
        Skill skr = skOptional.get();
        skr.getSubscribers().forEach(sub -> sub.getSkills().remove(skr));
        skr.getJobs().forEach(job -> job.getSkills().remove(skr)); // job nào có dính skill này trong job_skill thì sẽ
                                                                   // bị xóa

        // for (Job x : skr.getJobs()) {
        // x.getSkills().remove(skr);
        // }

        this.skillRepository.delete(skr);
    }

}
