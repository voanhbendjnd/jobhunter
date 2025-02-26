package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Entity.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

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

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new Skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (skill.getName() != null && this.skillService.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill đâ tồn tại!!!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createNewASkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a new Skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (!this.skillService.existsById(skill)) {
            throw new IdInvalidException("Id không tồn tại!!!");
        }
        if (skill.getName() != null && this.skillService.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill đâ tồn tại!!!");

        }
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.updatedSkill(skill));
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch All Skill")
    public ResponseEntity<ResultPaginationDTO> fetchAllSkill(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(this.skillService.fetchAllSkill(pageable, spec));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete Skill")
    public ResponseEntity<Void> delete_Skill(@PathVariable("id") Long id) throws IdInvalidException {
        Skill sk = this.skillService.fetchSkillById(id);
        if (sk == null) {
            throw new IdInvalidException("Skill với id này không tồn tại!!!");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
