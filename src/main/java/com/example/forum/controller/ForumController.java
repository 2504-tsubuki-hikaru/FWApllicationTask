package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        //25行目のような処理も必要になる。
        List<CommentForm> commentData = reportService.findAllComment();
        //コメントも取得してMavにいれてhtmlで使えるようにする。
        mav.addObject("comments", commentData);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm) {
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteById(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,
                                      @ModelAttribute("formModel") ReportForm report) {
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント投稿処理
     */
    @PostMapping("/commentAdd")
    public ModelAndView commentAddContent(@ModelAttribute("formModel") CommentForm commentForm) {
        // 投稿をテーブルに格納
        reportService.commentAddReport(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント編集画面表示処理
     */
    @GetMapping("/commentEdit/{id}")
    public ModelAndView commentEditComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集するコメントを取得
        CommentForm comment = reportService.editComment(id);
        // 編集するコメントをセット(htmlではformModelという値で渡す)
        mav.addObject("formModel", comment);
        // 画面遷移先を指定(commentEdit.html)
        mav.setViewName("/commentEdit");
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/commentUpdate/{id}")
    public ModelAndView commentUpdateComment(@PathVariable Integer id,
                                             @ModelAttribute("formModel") CommentForm comment) {
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集したコメントを更新
        reportService.editAddReport(comment);
        //　rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
