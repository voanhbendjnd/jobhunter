package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.Entity.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role role) {
        if (role.getPermissions() != null) {
            List<Long> idPermission = role.getPermissions()
                    .stream()
                    .map(it -> it.getId())
                    .collect(Collectors.toList());
            List<Permission> gPermission = this.permissionRepository.findByIdIn(idPermission);
            role.setPermissions(gPermission);
        }

        return this.roleRepository.save(role);
    }

    public boolean existsById(Long id) {
        return this.roleRepository.existsById(id) ? true : false;
    }

    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name) ? true : false;
    }

    public Role findById(Role role) {
        Optional<Role> optionalRole = this.roleRepository.findById(role.getId());
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        return null;
    }

    public Role updateRole(Role role) {
        Role newRole = this.roleRepository.findById(role.getId()).get();

        if (role.getPermissions() != null) {
            List<Long> permissionId = role.getPermissions()
                    .stream()
                    .map(it -> it.getId())
                    .collect(Collectors.toList());
            List<Permission> permissions = this.permissionRepository.findByIdIn(permissionId);
            newRole.setPermissions(permissions);
        }
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setActive(role.isActive());
        return this.roleRepository.save(newRole);
    }

    public ResultPaginationDTO fetchAll(Pageable pageable, Specification<Role> spec) {
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rp = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(roles.getTotalPages());
        mt.setTotal(roles.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(roles.getContent());
        return rp;
    }

    public void deleteRole(Long id) {
        this.roleRepository.deleteById(id);
    }

    public Role findById(Long id) {
        return this.roleRepository.findById(id).get();
    }
}
