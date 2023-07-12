package com.myapp.web.rest;

import com.myapp.domain.ViewsToDate;
import com.myapp.repository.ViewsToDateRepository;
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
 * REST controller for managing {@link com.myapp.domain.ViewsToDate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ViewsToDateResource {

    private final Logger log = LoggerFactory.getLogger(ViewsToDateResource.class);

    private static final String ENTITY_NAME = "viewsToDate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewsToDateRepository viewsToDateRepository;

    public ViewsToDateResource(ViewsToDateRepository viewsToDateRepository) {
        this.viewsToDateRepository = viewsToDateRepository;
    }

    /**
     * {@code POST  /views-to-dates} : Create a new viewsToDate.
     *
     * @param viewsToDate the viewsToDate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewsToDate, or with status {@code 400 (Bad Request)} if the viewsToDate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/views-to-dates")
    public ResponseEntity<ViewsToDate> createViewsToDate(@Valid @RequestBody ViewsToDate viewsToDate) throws URISyntaxException {
        log.debug("REST request to save ViewsToDate : {}", viewsToDate);
        if (viewsToDate.getId() != null) {
            throw new BadRequestAlertException("A new viewsToDate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViewsToDate result = viewsToDateRepository.save(viewsToDate);
        return ResponseEntity
            .created(new URI("/api/views-to-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /views-to-dates/:id} : Updates an existing viewsToDate.
     *
     * @param id the id of the viewsToDate to save.
     * @param viewsToDate the viewsToDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewsToDate,
     * or with status {@code 400 (Bad Request)} if the viewsToDate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewsToDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/views-to-dates/{id}")
    public ResponseEntity<ViewsToDate> updateViewsToDate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ViewsToDate viewsToDate
    ) throws URISyntaxException {
        log.debug("REST request to update ViewsToDate : {}, {}", id, viewsToDate);
        if (viewsToDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewsToDate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewsToDateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ViewsToDate result = viewsToDateRepository.save(viewsToDate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewsToDate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /views-to-dates/:id} : Partial updates given fields of an existing viewsToDate, field will ignore if it is null
     *
     * @param id the id of the viewsToDate to save.
     * @param viewsToDate the viewsToDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewsToDate,
     * or with status {@code 400 (Bad Request)} if the viewsToDate is not valid,
     * or with status {@code 404 (Not Found)} if the viewsToDate is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewsToDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/views-to-dates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViewsToDate> partialUpdateViewsToDate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ViewsToDate viewsToDate
    ) throws URISyntaxException {
        log.debug("REST request to partial update ViewsToDate partially : {}, {}", id, viewsToDate);
        if (viewsToDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewsToDate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewsToDateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewsToDate> result = viewsToDateRepository
            .findById(viewsToDate.getId())
            .map(existingViewsToDate -> {
                if (viewsToDate.getDate() != null) {
                    existingViewsToDate.setDate(viewsToDate.getDate());
                }
                if (viewsToDate.getViews() != null) {
                    existingViewsToDate.setViews(viewsToDate.getViews());
                }

                return existingViewsToDate;
            })
            .map(viewsToDateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewsToDate.getId().toString())
        );
    }

    /**
     * {@code GET  /views-to-dates} : get all the viewsToDates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewsToDates in body.
     */
    @GetMapping("/views-to-dates")
    public List<ViewsToDate> getAllViewsToDates() {
        log.debug("REST request to get all ViewsToDates");
        return viewsToDateRepository.findAll();
    }

    /**
     * {@code GET  /views-to-dates/:id} : get the "id" viewsToDate.
     *
     * @param id the id of the viewsToDate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewsToDate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/views-to-dates/{id}")
    public ResponseEntity<ViewsToDate> getViewsToDate(@PathVariable Long id) {
        log.debug("REST request to get ViewsToDate : {}", id);
        Optional<ViewsToDate> viewsToDate = viewsToDateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(viewsToDate);
    }

    /**
     * {@code DELETE  /views-to-dates/:id} : delete the "id" viewsToDate.
     *
     * @param id the id of the viewsToDate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/views-to-dates/{id}")
    public ResponseEntity<Void> deleteViewsToDate(@PathVariable Long id) {
        log.debug("REST request to delete ViewsToDate : {}", id);
        viewsToDateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
