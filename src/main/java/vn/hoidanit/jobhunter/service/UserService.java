package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
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

    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
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
