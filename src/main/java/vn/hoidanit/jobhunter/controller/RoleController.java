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
import vn.hoidanit.jobhunter.domain.Entity.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new Role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (this.roleService.existsByName(role.getName())) {
            throw new IdInvalidException("Tên đã tồn tại!!!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        // if (this.roleService.existsByName(role.getName())) {
        // throw new IdInvalidException("Tên đã tồn tại!!!");
        // }
        if (this.roleService.findById(role) == null) {
            throw new IdInvalidException("Id không chưa được tồn tại!!!");
        }
        return ResponseEntity.ok(this.roleService.updateRole(role));

    }

    @GetMapping("/roles")
    @ApiMessage("Fetch all roles")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok(this.roleService.fetchAll(pageable, spec));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.roleService.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại");
        }
        this.roleService.deleteRole(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.roleService.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại");
        }
        return ResponseEntity.ok(this.roleService.findById(id));
    }
}
