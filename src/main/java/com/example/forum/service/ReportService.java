package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    public List<ReportForm> findAllReport(String start, String end) throws ParseException {

        String strStartDate;
        String strEndDate;

            if (!StringUtils.isBlank(start)) {
                strStartDate = start + " 00:00:00";
            } else {
                strStartDate = "2020-01-01 00:00:00";
            }

            if (!StringUtils.isBlank(end)) {
                strEndDate = end + " 23:59:59";
            } else {
                //endの中身が空の場合は現在時刻を取得。デフォルト値の設定
                Date nowDate = new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                strEndDate = sdFormat.format(nowDate) + " 23:59:59";
            }

        //Date型に型変換をして引数でわたす。(string型でDB実行をするとエラーになる為）
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdFormat.parse(strStartDate);
        Date endDate = sdFormat.parse(strEndDate);

        List<Report> results = reportRepository.findByUpdatedDateBetweenOrderByUpdatedDateDesc(startDate,endDate);
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
            report.setUpdatedDate(result.getUpdatedDate());
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
        report.setUpdatedDate(new Date());
        return report;
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        //キーに該当するレコードの取得をする。該当するキーが無ければnullを返す。
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

    /*
     * コメントレコード1件取得
     */
    public CommentForm editComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        results.add((Comment) commentsRepository.findById(id).orElse(null));
        List<CommentForm> Comments = setCommentForm(results);
        return Comments.get(0);
    }

    public void editAddReport(CommentForm editComment) {
        Comment comments = setEditEntity(editComment);
        commentsRepository.save(comments);
    }

    private Comment setEditEntity(CommentForm editComment) {
        Comment comment = new Comment();
        comment.setId(editComment.getId());
        comment.setText(editComment.getText());
        comment.setReportId(editComment.getReportId());
        comment.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
        comment.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
        return comment;
    }

    //投稿を削除
    public void commentDeleteById (Integer id) {
        commentsRepository.deleteById(id);
    }
}
