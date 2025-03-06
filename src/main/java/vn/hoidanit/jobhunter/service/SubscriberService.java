package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Skill;
import vn.hoidanit.jobhunter.domain.Entity.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SkillRepository skillRepository;
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean checkExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email) ? true : false;
    }

    public boolean checkExistsById(Long id) {
        return this.subscriberRepository.existsById(id) ? true : false;
    }

    public Subscriber create(Subscriber sub) {
        if (sub.getSkills() != null) {
            List<Long> idSkills = sub.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> listSkill = this.skillRepository.findByIdIn(idSkills);
            sub.setSkills(listSkill);
        }
        return this.subscriberRepository.save(sub);
    }

    public Subscriber update(Subscriber sub) {
        Optional<Subscriber> subOptional = this.subscriberRepository.findById(sub.getId());
        if (subOptional.isPresent()) {
            if (sub.getSkills() != null) {
                List<Long> idSkills = sub.getSkills().stream()
                        .map(it -> it.getId())
                        .collect(Collectors.toList());
                List<Skill> listSkill = this.skillRepository.findByIdIn(idSkills);
                subOptional.get().setSkills(listSkill);
            }
        }

        return this.subscriberRepository.save(subOptional.get());
    }
}
