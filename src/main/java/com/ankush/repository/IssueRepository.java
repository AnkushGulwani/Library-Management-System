package com.ankush.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ankush.model.Book;
import com.ankush.model.Issue;
import com.ankush.model.IssueStatus;
import com.ankush.model.User;

@Transactional
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByUser(User user);

    void deleteByBook(Book book);

    List<Issue> findByUser_IdAndReturned(Long userId, boolean returned);

    List<Issue> findByUser_IdAndFineGreaterThan(Long userId, double fine);
}