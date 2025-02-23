package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.UserFetchToDTO;
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
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

    public ResUpdateUserDTO updateUserById(User user) {
        Optional<User> userGe = this.userRepository.findById(user.getId());
        if (userGe.isPresent()) {
            ResUpdateUserDTO dto = new ResUpdateUserDTO();
            dto.setAddress(user.getAddress());
            dto.setAge(user.getAge());
            dto.setGender(user.getGender());
            dto.setId(user.getId());
            dto.setName(user.getName());
            this.userRepository.save(userGe.get());
            // dto.setUpdateAt(user.getUpdateAt());

            return dto;

        } else {
            return null;
        }
    }

    public ResUpdateUserDTO convertUpdate(User user) {
        ResUpdateUserDTO dto = new ResUpdateUserDTO();
        dto.setId(user.getId());
        dto.setAddress(user.getAddress());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        dto.setUpdateAt(user.getUpdatedAt());
        dto.setName(user.getName());
        return dto;
    }

    public User handleUpdateUser(User user) {
        User crUser = this.fetchUserById(user.getId());
        if (crUser != null) {
            crUser.setAddress(user.getAddress());
            crUser.setAge(user.getAge());
            crUser.setGender(user.getGender());
            crUser.setName(user.getName());
            crUser = this.userRepository.save(crUser);
        }
        return crUser;
    }

    // create
    public ResUserCreateDTO convertUserToDTO(User user) {
        ResUserCreateDTO res = new ResUserCreateDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreateAt(user.getCreateAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    // fetch by id

    public UserFetchToDTO convertUserFetchToDTO(User user) {
        UserFetchToDTO dto = new UserFetchToDTO();
        dto.setAddress(user.getAddress());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setCreateAt(user.getCreateAt());
        dto.setUpdateAt(user.getUpdatedAt());
        dto.setGender(user.getGender());
        dto.setId(user.getId());
        return dto;
    }

    public ResultPaginationDTO fetchAllUserPlus(Pageable pageable, Specification spec) {
        Page<User> res = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(res.getTotalPages());
        mt.setTotal(res.getTotalElements());
        rs.setMeta(mt);
        List<UserFetchToDTO> listUser = res.getContent().stream()
                .map(it -> new UserFetchToDTO(it.getId(), it.getEmail(), it.getName(), it.getGender(), it.getAddress(),
                        it.getAge(), it.getUpdatedAt(), it.getCreateAt()))
                .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public boolean checkId(Long id) {
        return this.userRepository.existsById(id) ? true : false;
    }

    public User fecthUserByUserName(String name) {
        return this.userRepository.findByEmail(name);
    }

    // token
    public void updateUserToken(String token, String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
