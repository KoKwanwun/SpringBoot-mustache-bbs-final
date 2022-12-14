package com.mustache.bbs.controller;

import com.mustache.bbs.domain.Article;
import com.mustache.bbs.domain.Comment;
import com.mustache.bbs.domain.dto.ArticleDto;
import com.mustache.bbs.domain.dto.CommentDto;
import com.mustache.bbs.repository.ArticleRepository;
import com.mustache.bbs.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
@Slf4j
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleController(ArticleRepository repository, CommentRepository commentRepository) {
        this.articleRepository = repository;
        this.commentRepository = commentRepository;
    }

    @GetMapping()
    public String list(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";
    }

    @GetMapping("/new")
    public String createArticle(){
        return "articles/new";
    }

    @GetMapping("/{id}")
    public String selectSingle(@PathVariable Long id, Model model) {
        Optional<Article> optArticle = articleRepository.findById(id);
        if(!optArticle.isEmpty()){
            model.addAttribute("article", optArticle.get());

            List<Comment> comments = commentRepository.findByArticleId(id);
            model.addAttribute("comments", comments);
            return "articles/show";
        } else{
            return "articles/error";
        }
    }

    @PostMapping("/post")
    public String articles(ArticleDto articleDto) {
        log.info(articleDto.toString());
        Article savedArticle = articleRepository.save(articleDto.toEntity());
        return String.format("redirect:/articles/%d", savedArticle.getId());
    }

    @GetMapping("/{id}/edit")
    public String editArticles(@PathVariable Long id, Model model) {
        Optional<Article> optArticle = articleRepository.findById(id);

        if(!optArticle.isEmpty()){
            model.addAttribute("article", optArticle.get());
            return "articles/edit";
        } else{
            return "articles/error";
        }
    }

    @PostMapping("/{id}/update")
    public String updateArticles(@PathVariable Long id, ArticleDto articleDto, Model model) {
        Article article = articleRepository.save(articleDto.toEntity());
        model.addAttribute("article", article);

        return String.format("redirect:/articles/%d", article.getId());
    }

    @GetMapping("/{id}/delete")
    public String deleteArticles(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/articles";
    }

    @PostMapping("/comment/post")
    public String comment(CommentDto commentDto) {
        Comment savedComment = commentRepository.save(commentDto.toEntity());
        return String.format("redirect:/articles/%d", savedComment.getArticleId());
    }
}
