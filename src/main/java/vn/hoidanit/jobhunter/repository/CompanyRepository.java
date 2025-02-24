package vn.hoidanit.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Entity.Company;
import vn.hoidanit.jobhunter.domain.Entity.User;

import java.util.List;

@Repository
public interface CompanyRepository
                extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
        Company findNameById(Long id);

}
