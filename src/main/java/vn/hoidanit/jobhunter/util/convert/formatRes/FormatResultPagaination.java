package vn.hoidanit.jobhunter.util.convert.FormatRes;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.hoidanit.jobhunter.domain.Entity.Job;
import vn.hoidanit.jobhunter.domain.Entity.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO.Meta;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.util.convert.ResumeConvert;

public class FormatResultPagaination {
    public static ResultPaginationDTO fetchAll(Page<Resume> page) {
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(page.getNumber() + 1);
        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        res.setMeta(mt);
        List<ResFetchResumeDTO> listResume = page.getContent()
                .stream()
                .map(ResumeConvert::convertToResFetchResume)
                .toList();
        res.setResult(listResume);
        return res;
    }

    public static ResultPaginationDTO fetchAllJob(Page<Job> page) {
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotal());
        mt.setTotal(page.setTotalElements());
        res.setMeta(mt);
        List<ResFetchJobDTO> listJob = page.getContent()
            .stream()
            .map()
        
    }
}
