package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean checkApiMethodAndModule(Permission permission) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(),
                permission.getMethod(), permission.getModule()) ? true : false;
    }

    public Permission createPermission(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permission) {
        Optional<Permission> pOptional = this.permissionRepository.findById(permission.getId());
        pOptional.get().setName(permission.getName());
        pOptional.get().setApiPath(permission.getApiPath());
        pOptional.get().setMethod(permission.getMethod());
        pOptional.get().setModule(permission.getModule());
        return this.permissionRepository.save(pOptional.get());

    }

    public ResultPaginationDTO fetchAll(Pageable pageable, Specification<Permission> spec) {
        Page<Permission> permissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rp = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(permissions.getTotalPages());
        mt.setTotal(permissions.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(permissions.getContent());
        return rp;

    }

}
