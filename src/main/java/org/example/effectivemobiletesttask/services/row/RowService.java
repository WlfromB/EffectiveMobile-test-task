package org.example.effectivemobiletesttask.services.row;

import org.example.effectivemobiletesttask.dto.row.*;
import org.example.effectivemobiletesttask.entities.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RowService {
    Row createRow(RowCreateRequest request) throws Exception;

    Row changeStatus(RowChangeStatusRequest request) throws Exception;

    Row changeRow(RowChangeRequest request) throws Exception;
    
    void deleteRow(RowDeleteRequest request) throws Exception;
    
    Page<Row> getRowsByUserLogin(String login, Pageable pageable) throws Exception;
    
    Row getByTitle(String title) throws Exception;
}
