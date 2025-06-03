package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    //findAllByOrderByIdDesc()=idを降順で並び変えてくれる。
    public List<Report> findByUpdatedDateBetweenOrderByUpdatedDateDesc(Date startDate, Date endDate);
}



