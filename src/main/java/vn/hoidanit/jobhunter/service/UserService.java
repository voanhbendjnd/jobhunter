package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveAll(User user) {
        this.userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        if (this.userRepository.existsByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> page = this.userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(page.getNumber() + 1); // dang o trang bao nhieu
        mt.setPageSize(page.getSize()); // bao nhieu phan tu
        mt.setPages(page.getTotalPages()); // tong so trang
        mt.setTotal(page.getTotalElements()); // tong phan tu
        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;

    }

    public ResultPaginationDTO fetchAllUser2(Specification<User> pageable, Pageable pageable2) {
        Page<User> page = this.userRepository.findAll(pageable, pageable2);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageable2.getPageNumber() + 1);
        mt.setPageSize(pageable2.getPageSize());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;

    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public void DeleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
        // return this.userRepository.findById(id);
    }

    public User updateUserById(User user) {
        Optional<User> userGe = this.userRepository.findById(user.getId());
        if (userGe.isPresent()) {
            userGe.get().setEmail(user.getEmail());
            userGe.get().setPassword(user.getPassword());
            userGe.get().setName(user.getName());
            return this.userRepository.save(userGe.get());
        } else {
            return null;
        }
    }

    public User fecthUserByUserName(String name) {
        return this.userRepository.findByEmail(name);
    }
}
