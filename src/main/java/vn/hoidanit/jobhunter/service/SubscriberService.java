package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Skill;
import vn.hoidanit.jobhunter.domain.Entity.Subscriber;
import vn.hoidanit.jobhunter.domain.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SkillRepository skillRepository;
    private final SubscriberRepository subscriberRepository;
    private final JobRepository jobRepository;

    @Autowired
    private EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
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

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.ComapnyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream() // chuyển skill thành kiểu ResSkill
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public void sendSubcribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkill = sub.getSkills();
                if (listSkill != null && listSkill.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkill);
                    if (listJobs != null && listJobs.size() > 0) {
                        List<ResEmailJob> arr = listJobs.stream()
                                .map(job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(sub.getEmail(), "Cơ hội cưới vợ đang chờ bạn",
                                "job", sub.getName(), arr);
                    }
                }
            }
        }
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    // @Scheduled(cron = "*/10 * * * * *")
    // public void testCron() {
    // System.out.println(">>>>>>TEST CRON");
    // }
}
