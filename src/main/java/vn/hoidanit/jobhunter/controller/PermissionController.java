package vn.hoidanit.jobhunter.controller;

import java.lang.annotation.Repeatable;
import java.net.http.HttpResponse.ResponseInfo;

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
import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        if (this.permissionService.checkApiMethodAndModule(permission)) {
            throw new IdInvalidException("apiPath hoặc method hoặc module đã tồn tại!!!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermisson(@RequestBody Permission permission) throws IdInvalidException {
        if (this.permissionService.checkApiMethodAndModule(permission)) {
            throw new IdInvalidException("apiPath hoặc method hoặc module đã tồn tại!!!");
        }
        if (this.permissionService.existsByName(permission.getName())) {
            throw new IdInvalidException("Tên đã tồn tại");
        }
        return ResponseEntity.ok(this.permissionService.updatePermission(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all permission")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok(this.permissionService.fetchAll(pageable, spec));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.permissionService.existsById(id)) {
            throw new IdInvalidException("Id chưa tồn tại");
        }
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok(null);
    }

}
