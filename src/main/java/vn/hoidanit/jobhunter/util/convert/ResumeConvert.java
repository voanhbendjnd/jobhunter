package vn.hoidanit.jobhunter.util.convert;

import vn.hoidanit.jobhunter.domain.Entity.Resume;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;

public class ResumeConvert {

    public static ResFetchResumeDTO convertToResFetchResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedBy(resume.getCreatedBy());
        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }
        res.setUser(new ResFetchResumeDTO.UserDTO(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobDTO(resume.getJob().getId(), resume.getJob().getName()));
        return res;
    }

}
