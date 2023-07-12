package com.myapp.repository;

import com.myapp.domain.ViewPerDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ViewPerDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViewPerDateRepository extends JpaRepository<ViewPerDate, Long> {}
