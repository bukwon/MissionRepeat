package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepositoryCustom {
    Page<Post> search(boolean isPublished, String kw, Pageable pageable);
    Page<Post> search(Member author, Boolean isPublished, String kw, Pageable pageable);
}