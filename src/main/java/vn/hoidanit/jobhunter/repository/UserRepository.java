package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Entity.Company;
import vn.hoidanit.jobhunter.domain.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String name);

    boolean existsByEmail(String email);

    // Page<User> findAll(Pageable pageable);
    boolean existsById(Long id);

    User findByRefreshTokenAndEmail(String token, String email);

    List<User> findByCompany(Company company);

}
