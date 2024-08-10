package org.example.effectivemobiletesttask.services.row;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dao.RowRepository;
import org.example.effectivemobiletesttask.dto.row.RowChangeRequest;
import org.example.effectivemobiletesttask.dto.row.RowChangeStatusRequest;
import org.example.effectivemobiletesttask.dto.row.RowCreateRequest;
import org.example.effectivemobiletesttask.dto.row.RowDeleteRequest;
import org.example.effectivemobiletesttask.entities.Priority;
import org.example.effectivemobiletesttask.entities.Row;
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
public class RowServiceImpl implements RowService {
    private final RowRepository rowRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Row createRow(RowCreateRequest request) throws Exception {
        if (rowRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Row with title " + request.getTitle() + " already exists");
        }
        User supplier = userService.findByLogin(request.getSupplierLogin());
        User author = userService.findByLogin(request.getAuthorLogin());
        Row row = request.from();
        row.setAuthor(author);
        row.setSupplier(supplier);
        row.setStatus(Status.valueOf("WAIT"));
        rowRepository.save(row);
        return row;
    }

    @Override
    @Transactional
    public Row changeStatus(RowChangeStatusRequest request) throws Exception {
        Row row = rowRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        if (row.getAuthor().getLogin().equals(request.getLogin()) || row.getSupplier().getLogin().equals(request.getLogin())) {
            row.setStatus(Status.valueOf(request.getStatus()));
            rowRepository.save(row);
            return row;
        }
        throw new IllegalArgumentException("User not a author or supplier!");
    }

    @Override
    @Transactional
    public Row changeRow(RowChangeRequest request) throws Exception {
        Row row = rowRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        if (row.getAuthor().getLogin().equals(request.getLogin())) {
            row.setDescription(request.getDescription());
            User supplier = userService.findByLogin(request.getSupplier());
            row.setSupplier(supplier);
            row.setTitle(request.getTitle());
            row.setPriority(Priority.valueOf(request.getPriority()));
            row.setStatus(Status.valueOf(request.getStatus()));
            rowRepository.save(row);
            return row;
        }
        throw new IllegalArgumentException("This user is not the author");
    }

    @Override
    @Transactional
    public void deleteRow(RowDeleteRequest request) throws Exception {
        Row row = rowRepository.findByTitle(request.getTitle()).orElseThrow(() -> new NotFoundException("Row not found"));
        User author = userService.findByLogin(request.getAuthor());
        if (author.getId().equals(row.getAuthor().getId())) {
            rowRepository.delete(row);
            return;
        }
        throw new IllegalArgumentException("This user is not the author");
    }

    @Override
    @Transactional
    public Page<Row> getRowsByUserLogin(String login, Pageable pageable) throws Exception {
        log.info("before finding {}", login);
        User user = userService.findByLogin(login);
        log.info("after finding {}", login);
        return rowRepository.findAllByAuthorOrSupplier(user, user, pageable);
    }

    @Override
    @Transactional
    public Row getByTitle(String title) throws Exception {
        return rowRepository.findByTitle(title).orElseThrow(() -> new NotFoundException("Row not found"));
    }
}
