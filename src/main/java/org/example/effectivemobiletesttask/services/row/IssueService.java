package org.example.effectivemobiletesttask.services.row;

import org.example.effectivemobiletesttask.dto.issue.*;
import org.example.effectivemobiletesttask.entities.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {
    Issue createIssue(IssueCreateRequest request) throws Exception;

    Issue changeStatus(IssueChangeStatusRequest request) throws Exception;

    Issue changeIssue(IssueChangeRequest request) throws Exception;
    
    void deleteIssue(IssueDeleteRequest request) throws Exception;
    
    Page<Issue> getIssuesByUserLogin(String login, Pageable pageable) throws Exception;
    
    Issue getByTitle(String title) throws Exception;
}
