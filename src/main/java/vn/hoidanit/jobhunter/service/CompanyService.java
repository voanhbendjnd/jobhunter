package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO fetchAllCompany(Pageable pageable, Specification<Company> spec) {
        Page<Company> companyReturn = this.companyRepository.findAll(spec, pageable);
        // return this.companyRepository.findAll();
        ResultPaginationDTO rs = new ResultPaginationDTO();
         ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1); // front end
        mt.setPageSize(pageable.getPageSize()); // front end

        mt.setPages(companyReturn.getTotalPages());
        mt.setTotal(companyReturn.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(companyReturn.getContent());
        return rs;
    }

    public Company updateCompanyById(Company company) {
        Optional<Company> comOptional = this.companyRepository.findById(company.getId());
        if (comOptional.isPresent()) {
            comOptional.get().setAddress(company.getAddress());
            comOptional.get().setDescription(company.getDescription());
            comOptional.get().setName(company.getName());
            comOptional.get().setLogo(company.getLogo());
            comOptional.get().setDescription(company.getDescription());
            return this.companyRepository.save(comOptional.get());
        } else {
            return null;
        }
    }

    public void deleteCompanyById(Long id) {

        this.companyRepository.deleteById(id);
    }

    public boolean checkIdCompany(Long id) {
        return this.companyRepository.existsById(id) ? true : false;
    }

    public Company findNameById(Long id) {
        return this.companyRepository.findNameById(id);
    }
}
