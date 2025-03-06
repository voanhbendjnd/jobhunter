package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.domain.Entity.Subscriber;

@RestController
@RequestMapping("/api/v1")
public class SubController {
    private final SubscriberService subService;

    public SubController(SubscriberService subService) {
        this.subService = subService;
    }

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        if (this.subService.checkExistsByEmail(sub.getEmail())) {
            throw new IdInvalidException("Email đã bị trùng");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subService.create(sub));
    }

    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber sub) throws IdInvalidException {
        if (this.subService.checkExistsById(sub.getId())) {
            return ResponseEntity.ok(this.subService.update(sub));
        }
        throw new IdInvalidException("Id khong ton tai!!!");
    }

}
