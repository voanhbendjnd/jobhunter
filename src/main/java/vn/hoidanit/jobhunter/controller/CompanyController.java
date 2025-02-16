package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.RestResponse;
import vn.hoidanit.jobhunter.service.CompanyService;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<RestResponse<Company>> createCompany(
            @RequestBody @Valid Company company) {
        Company createdCompany = companyService.createCompany(company);
        RestResponse<Company> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Company created successfully");
        response.setData(createdCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
