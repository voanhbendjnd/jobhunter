package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(
            @Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.createCompany(company));
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch All Company")
    public ResponseEntity<ResultPaginationDTO> fecthAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {

        // return ResponseEntity.ok(this.companyService.fetchAllCompany(pageable,
        // spec));
        return ResponseEntity.ok(this.companyService.fetchAllCompany(pageable, spec));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompanyById(
            @RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.updateCompanyById(company));
    }

    @DeleteMapping("companies/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable("id") Long id) throws IdInvalidException {
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.ok().body(null);

    }

}
