package com.myapp.repository;

import com.myapp.domain.ViewsToDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ViewsToDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViewsToDateRepository extends JpaRepository<ViewsToDate, Long> {}
