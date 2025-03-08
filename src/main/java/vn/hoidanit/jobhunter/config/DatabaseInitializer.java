package vn.hoidanit.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.Entity.Role;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    // private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>START INTI DATABASE<<<");
        Long countPermissions = this.permissionRepository.count();
        Long countRoles = this.roleRepository.count();
        Long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
            arr.add(new Permission("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
            arr.add(new Permission("Delete a company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            arr.add(new Permission("Fetch company by Id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            arr.add(new Permission("Fetch companies with pagination", "/api/v1/companies", "GET", "COMPANIES"));
            // job
            arr.add(new Permission("Create a job", "/api/v1/jobs", "POST", "JOBS"));
            arr.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
            arr.add(new Permission("Delete a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            arr.add(new Permission("Fetch jobs by Id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            arr.add(new Permission("Fetch jobs with pagination", "/api/v1/jobs", "GET", "JOBS"));
            // permission
            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Fetch permission by Id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Fetch permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));
            // resume
            arr.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            arr.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            arr.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            arr.add(new Permission("Fetch resume by Id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            arr.add(new Permission("Fetch resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));
            // user
            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Fetch user by Id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Fetch users with pagination", "/api/v1/users", "GET", "USERS"));
            // role
            arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Fetch role by Id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("Fetch roles with pagination", "/api/v1/roles", "GET", "ROLES"));
            // skill
            arr.add(new Permission("Create a skill", "/api/v1/skills", "POST", "SKILLS"));
            arr.add(new Permission("Update a skill", "/api/v1/skills", "PUT", "SKILLS"));
            arr.add(new Permission("Delete a skill", "/api/v1/skills/{id}", "DELETE", "SKILLS"));
            arr.add(new Permission("Fetch skill by Id", "/api/v1/skills/{id}", "GET", "SKILLS"));
            arr.add(new Permission("Fetch skills with pagination", "/api/v1/skills", "GET", "SKILLS"));
            // file
            arr.add(new Permission("Create a file", "/api/v1/files", "POST", "FILES"));
            arr.add(new Permission("Download file", "/api/v1/files", "GET", "FILES"));
            // auth
            arr.add(new Permission("Login", "/api/v1/auth/login", "POST", "AUTH"));
            arr.add(new Permission("Get account", "/api/v1/auth/account", "GET", "AUTH"));
            arr.add(new Permission("Refresh", "/api/v1/auth/refresh", "GET", "AUTH"));
            arr.add(new Permission("Logout", "/api/v1/auth/logout", "POST", "AUTH"));
            // subscriber
            arr.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
            arr.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
            arr.add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
            arr.add(new Permission("Fetch subscriber by Id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
            arr.add(new Permission("Fetch subscribers with pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));
            this.permissionRepository.saveAll(arr);

        }
        // if (true) {

        // User adminUser = new User();
        // adminUser.setEmail("user2@gmail.com");
        // adminUser.setAddress("Can Tho");
        // adminUser.setAge(18);
        // adminUser.setGender(GenderEnum.MALE);
        // adminUser.setName("I'm super user2");
        // adminUser.setPassword(this.passwordEncoder.encode("123456"));

        // this.userRepository.save(adminUser);
        // }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin has full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);
            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("Can Tho");
            adminUser.setAge(18);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            this.userRepository.save(adminUser);
        }
        if (countPermissions != 0 & countRoles != 0 && countUsers != 0) {
            System.out.println(">>> SKIP <<<");
        } else {
            System.out.println(">>> END INIT DATABASE");
        }

    }

}
