package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    CommentRepository commentsRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        List<Report> results = reportRepository.findAllByOrderByIdDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        //setReportEntityメソッドでFormからEntityに詰め替えている
        //FormのままだとDBに登録できないので。
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    //リクエストから取得した情報をEntityに設定
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        return report;
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }

    //投稿を削除
    public void deleteById(Integer id) {
        reportRepository.deleteById(id);
    }

    //コメントの登録
    public void commentAddReport(CommentForm commentForm) {
        //引く数をCommentsFormに変える必要がある。
        //htmlで入力された値がcontent（Controllerで指定）に入る。
        //CommentFormで受け取った値をcommentsEntityに詰め替える。
        //IDはFormに自動で入るけど、Entityには詰め替える必要があるので詰め替えられているか確認する。
        Comment comments = setCommentEntity(commentForm);
        commentsRepository.save(comments);
    }

    private Comment setCommentEntity(CommentForm commentForm) {
        Comment comment = new Comment();
        comment.setId(commentForm.getId());
        comment.setText(commentForm.getText());
        comment.setReportId(commentForm.getReportId());
        //formのなかには現在時刻が入ってないので、ここで直接commentにsetする。
        comment.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
        comment.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
        //LocalDateTime現在時刻使いかたなどで調べてみる
        //LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
        return comment;
    }

    /*
     * コメントを全件取得
     */
    public List<CommentForm> findAllComment() {
        List<Comment> comResults = commentsRepository.findAll();
        List<CommentForm> comments = setCommentForm(comResults);
        return comments;
    }

    /*
     * DBから取得したデータをFormに設定(comment版）
     */
    private List<CommentForm> setCommentForm(List<Comment> comResults) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < comResults.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = comResults.get(i);
            comment.setId(result.getId());
            comment.setText(result.getText());
            comment.setReportId(result.getReportId());
            comment.setCreatedDate(result.getCreatedDate());
            comment.setUpdatedDate(result.getUpdatedDate());
            comments.add(comment);
        }
        return comments;
    }
}
