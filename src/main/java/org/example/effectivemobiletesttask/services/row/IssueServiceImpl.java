package org.example.effectivemobiletesttask.services.row;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dao.IssueRepository;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueCreateRequest;
import org.example.effectivemobiletesttask.dto.issue.IssueDeleteRequest;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.entities.Status;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@RequiredArgsConstructor
@Service
@Slf4j
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Issue createIssue(IssueCreateRequest request) throws Exception {
        if (issueRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Issue with title " + request.getTitle() + " already exists");
        }
        User supplier = userService.findByLogin(request.getSupplierLogin());
        User author = userService.findByLogin(request.getAuthorLogin());
        Issue issue = request.from();
        issue.setAuthor(author);
        issue.setSupplier(supplier);
        issue.setStatus(Status.valueOf("WAIT"));
        issueRepository.save(issue);
        return issue;
    }

    @Override
    @Transactional
    public Issue changeStatus(IssueChangeStatusRequest request) throws Exception {
        Issue issue = issueRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        if (issue.getAuthor().getLogin().equals(request.getLogin()) || issue.getSupplier().getLogin().equals(request.getLogin())) {
            issue.setStatus(Status.valueOf(request.getStatus()));
            issueRepository.save(issue);
            return issue;
        }
        throw new IllegalArgumentException("User not a author or supplier!");
    }

    @Override
    @Transactional
    public Issue changeIssue(IssueChangeRequest request) throws Exception {
        Issue issue = issueRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        if (issue.getAuthor().getLogin().equals(request.getLogin())) {
            issue.setDescription(request.getDescription());
            User supplier = userService.findByLogin(request.getSupplier());
            issue.setSupplier(supplier);
            issue.setTitle(request.getTitle());
            issue.setPriority(Priority.valueOf(request.getPriority()));
            issue.setStatus(Status.valueOf(request.getStatus()));
            issueRepository.save(issue);
            return issue;
        }
        throw new IllegalArgumentException("This user is not the author");
    }

    @Override
    @Transactional
    public void deleteIssue(IssueDeleteRequest request) throws Exception {
        Issue issue = issueRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        User author = userService.findByLogin(request.getAuthor());
        if (author.getId().equals(issue.getAuthor().getId())) {
            issueRepository.delete(issue);
            return;
        }
        throw new IllegalArgumentException("This user is not the author");
    }

    @Override
    @Transactional
    public Page<Issue> getIssuesByUserLogin(String login, Pageable pageable) throws Exception {
        log.debug("before finding {}", login);
        User user = userService.findByLogin(login);
        log.debug("after finding {}", login);
        return issueRepository.findAllByAuthorOrSupplier(user, user, pageable);
    }

    @Override
    @Transactional
    public Issue getByTitle(String title) throws Exception {
        return issueRepository.findByTitle(title).orElseThrow(() -> new NotFoundException("Row not found"));
    }
}
