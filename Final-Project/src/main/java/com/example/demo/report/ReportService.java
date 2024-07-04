package com.example.demo.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportDao dao;

    public ReportDto save(ReportDto dto) {
        Report r = dao.save(Report.create(dto));
        return ReportDto.create(r);
    }

    public ReportDto findById(int num) {
        Report r = dao.findById(num).orElse(null);
        return ReportDto.create(r);
    }
    public ArrayList<ReportDto> findAll() {
        List<Report> l = dao.findAll();
        ArrayList<ReportDto> r = new ArrayList<>();
        for (Report r1 : l) {
            r.add(ReportDto.create(r1));
        }
        return r;
    }

    public ArrayList<ReportDto> findByType(int type) {
        ArrayList<Report> l = dao.findByTypeOrderByWdate(type);
        ArrayList<ReportDto> r = new ArrayList<>();
        for (Report r1 : l) {
            r.add(ReportDto.create(r1));
        }
        return r;
    }
    public ArrayList<ReportDto> findByCheck(int check){
        ArrayList<Report> l = dao.findByReadOrderByWdate(check);
        ArrayList<ReportDto> r = new ArrayList<>();
        for (Report r1 : l) {
            r.add(ReportDto.create(r1));
        }
        return r;
    }

    public void delete(ReportDto dto) {
        dao.delete(Report.create(dto));
    }

    public void update(ReportDto dto) {
        dao.save(Report.create(dto));
    }
}
