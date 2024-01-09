package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.domain.post.postComment.entity.PostComment;
import com.ll.medium.global.exceptions.GlobalException;
import com.ll.medium.global.rq.Rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post/{id}/comment")
@RequiredArgsConstructor
public class PostCommentController {
    private final PostService postService;
    private final Rq rq;

    @Getter
    @Setter
    public static class WriteForm {
        @NotBlank
        private String body;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(
            @PathVariable long id,
            @Valid WriteForm form
    ) {
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        PostComment postComment = postService.writeComment(rq.getMember(), post, form.getBody());

        return rq.redirect("/post/" + id + "#postComment-" + postComment.getId(), "댓글이 작성되었습니다.");
    }
}