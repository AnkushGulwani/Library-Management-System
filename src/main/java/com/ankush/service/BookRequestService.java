package com.ankush.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankush.model.Book;
import com.ankush.model.BookRequest;
import com.ankush.model.RequestStatus;
import com.ankush.model.User;
import com.ankush.repository.BookRepository;
import com.ankush.repository.BookRequestRepository;
import com.ankush.repository.UserRepository;

@Service
public class BookRequestService {

	@Autowired
	private BookRequestRepository requestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	public BookRequest createRequest(String bookTitle, String author, String description, String email) {

		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		BookRequest request = new BookRequest();
		request.setBookTitle(bookTitle);
		request.setAuthor(author);
		request.setDescription(description != null ? description : "");
		request.setRequestedBy(user);
		request.setRequestDate(LocalDate.now());
		request.setStatus(RequestStatus.PENDING);

		return requestRepository.save(request);
	}

	public List<BookRequest> getAllRequests() {
		return requestRepository.findAll();
	}

	public BookRequest updateStatus(Long requestId, RequestStatus status) {

		BookRequest request = requestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Request not found"));

		request.setStatus(status);

		if (status == RequestStatus.APPROVED) {

			boolean exists = bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCase(request.getBookTitle(),
					request.getAuthor());

			if (!exists) {

				Book book = new Book();
				book.setTitle(request.getBookTitle());
				book.setAuthor(request.getAuthor());
				book.setDescription(request.getDescription());
				book.setTotalCopies(1);
				book.setAvailableCopies(1);

				bookRepository.save(book);
			}
		}

		return requestRepository.save(request);
	}

	public List<BookRequest> getUserRequests(Long userId) {
		return requestRepository.findByRequestedById(userId);
	}

}