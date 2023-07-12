package com.myapp.web.rest;

import com.myapp.domain.ViewPerDate;
import com.myapp.repository.ViewPerDateRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.ViewPerDate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ViewPerDateResource {

    private final Logger log = LoggerFactory.getLogger(ViewPerDateResource.class);

    private static final String ENTITY_NAME = "viewPerDate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewPerDateRepository viewPerDateRepository;

    public ViewPerDateResource(ViewPerDateRepository viewPerDateRepository) {
        this.viewPerDateRepository = viewPerDateRepository;
    }

    /**
     * {@code POST  /view-per-dates} : Create a new viewPerDate.
     *
     * @param viewPerDate the viewPerDate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewPerDate, or with status {@code 400 (Bad Request)} if the viewPerDate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/view-per-dates")
    public ResponseEntity<ViewPerDate> createViewPerDate(@Valid @RequestBody ViewPerDate viewPerDate) throws URISyntaxException {
        log.debug("REST request to save ViewPerDate : {}", viewPerDate);
        if (viewPerDate.getId() != null) {
            throw new BadRequestAlertException("A new viewPerDate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViewPerDate result = viewPerDateRepository.save(viewPerDate);
        return ResponseEntity
            .created(new URI("/api/view-per-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /view-per-dates/:id} : Updates an existing viewPerDate.
     *
     * @param id the id of the viewPerDate to save.
     * @param viewPerDate the viewPerDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPerDate,
     * or with status {@code 400 (Bad Request)} if the viewPerDate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPerDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/view-per-dates/{id}")
    public ResponseEntity<ViewPerDate> updateViewPerDate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ViewPerDate viewPerDate
    ) throws URISyntaxException {
        log.debug("REST request to update ViewPerDate : {}, {}", id, viewPerDate);
        if (viewPerDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPerDate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewPerDateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ViewPerDate result = viewPerDateRepository.save(viewPerDate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPerDate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /view-per-dates/:id} : Partial updates given fields of an existing viewPerDate, field will ignore if it is null
     *
     * @param id the id of the viewPerDate to save.
     * @param viewPerDate the viewPerDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPerDate,
     * or with status {@code 400 (Bad Request)} if the viewPerDate is not valid,
     * or with status {@code 404 (Not Found)} if the viewPerDate is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewPerDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/view-per-dates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViewPerDate> partialUpdateViewPerDate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ViewPerDate viewPerDate
    ) throws URISyntaxException {
        log.debug("REST request to partial update ViewPerDate partially : {}, {}", id, viewPerDate);
        if (viewPerDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPerDate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewPerDateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewPerDate> result = viewPerDateRepository
            .findById(viewPerDate.getId())
            .map(existingViewPerDate -> {
                if (viewPerDate.getDate() != null) {
                    existingViewPerDate.setDate(viewPerDate.getDate());
                }
                if (viewPerDate.getViews() != null) {
                    existingViewPerDate.setViews(viewPerDate.getViews());
                }

                return existingViewPerDate;
            })
            .map(viewPerDateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPerDate.getId().toString())
        );
    }

    /**
     * {@code GET  /view-per-dates} : get all the viewPerDates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPerDates in body.
     */
    @GetMapping("/view-per-dates")
    public List<ViewPerDate> getAllViewPerDates() {
        log.debug("REST request to get all ViewPerDates");
        return viewPerDateRepository.findAll();
    }

    /**
     * {@code GET  /view-per-dates/:id} : get the "id" viewPerDate.
     *
     * @param id the id of the viewPerDate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewPerDate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/view-per-dates/{id}")
    public ResponseEntity<ViewPerDate> getViewPerDate(@PathVariable Long id) {
        log.debug("REST request to get ViewPerDate : {}", id);
        Optional<ViewPerDate> viewPerDate = viewPerDateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(viewPerDate);
    }

    /**
     * {@code DELETE  /view-per-dates/:id} : delete the "id" viewPerDate.
     *
     * @param id the id of the viewPerDate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/view-per-dates/{id}")
    public ResponseEntity<Void> deleteViewPerDate(@PathVariable Long id) {
        log.debug("REST request to delete ViewPerDate : {}", id);
        viewPerDateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
