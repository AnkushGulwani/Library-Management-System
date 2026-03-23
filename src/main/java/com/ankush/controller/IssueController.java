package com.ankush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ankush.model.Issue;
import com.ankush.service.IssueService;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin
public class IssueController {

    @Autowired
    private IssueService issueService;

    // ISSUE BOOK (by bookName + userName)
    @PostMapping
    public Issue issueBook(
            @RequestParam String bookTitle,
            @RequestParam String userName,
            @RequestParam(required = false) String dueDate
    ) {
        return issueService.issueBook(bookTitle, userName, dueDate);
    }

    // RETURN BOOK
    @PostMapping("/return/{issueId}")
    public Issue returnBook(@PathVariable Long issueId,
                            @RequestParam(required = false) Boolean damaged) {
        return issueService.returnBook(issueId, damaged);
    }

    // GET ALL ISSUES
    @GetMapping
    public List<Issue> getAllIssues() {
        return issueService.getAllIssues();
    }

    // MEMBER HISTORY
    @GetMapping("/member/{userName}")
    public List<Issue> getMemberHistory(@PathVariable String userName) {
        return issueService.getMemberHistory(userName);
    }
}