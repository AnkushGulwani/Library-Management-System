package com.ankush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ankush.model.BookRequest;
import com.ankush.model.RequestStatus;

import java.util.List;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    List<BookRequest> findByRequestedById(Long userId);

    List<BookRequest> findByStatus(RequestStatus status);

	
}