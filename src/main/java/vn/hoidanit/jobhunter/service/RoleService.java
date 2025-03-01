package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.Entity.Role;
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

    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name) ? true : false;
    }
}
