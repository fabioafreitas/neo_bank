package com.example.backend_spring.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class PaginationUtils {
	public static void validatePaginationParams(
			int page,
			int size,
			String sort,
			List<String> allowedSortFields
	) {
		if (page < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 0");
		}
		if (size <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be greater than 0");
		}
		if (sort == null || sort.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort parameter is required");
		}
		String[] sortParts = sort.split(",");
		if (sortParts.length > 2) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort format should be 'field' or 'field,direction'");
		}
		if (sortParts.length == 2) {
			String direction = sortParts[1].trim().toLowerCase();
			if (!direction.equals("asc") && !direction.equals("desc")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort direction must be 'asc' or 'desc'");
			}
		}
		if (sortParts[0].trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort field cannot be empty");
		}
		String sortField = sortParts[0].trim();
		if (!allowedSortFields.contains(sortField)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Invalid sort field. Allowed fields are: " + String.join(", ", allowedSortFields)
			);
		}
	}

	public static Pageable generatePagable(
			int page,
			int size,
			String sort
	) {
		String[] sortParts = sort.split(",");
		String sortField = sortParts[0];
		Sort.Direction sortDirection = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
				? Sort.Direction.DESC
				: Sort.Direction.ASC;

		return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
	}
}
