package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.ViewPerDate;
import com.myapp.repository.ViewPerDateRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ViewPerDateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ViewPerDateResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VIEWS = 1;
    private static final Integer UPDATED_VIEWS = 2;

    private static final String ENTITY_API_URL = "/api/view-per-dates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ViewPerDateRepository viewPerDateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViewPerDateMockMvc;

    private ViewPerDate viewPerDate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPerDate createEntity(EntityManager em) {
        ViewPerDate viewPerDate = new ViewPerDate().date(DEFAULT_DATE).views(DEFAULT_VIEWS);
        return viewPerDate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPerDate createUpdatedEntity(EntityManager em) {
        ViewPerDate viewPerDate = new ViewPerDate().date(UPDATED_DATE).views(UPDATED_VIEWS);
        return viewPerDate;
    }

    @BeforeEach
    public void initTest() {
        viewPerDate = createEntity(em);
    }

    @Test
    @Transactional
    void createViewPerDate() throws Exception {
        int databaseSizeBeforeCreate = viewPerDateRepository.findAll().size();
        // Create the ViewPerDate
        restViewPerDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPerDate)))
            .andExpect(status().isCreated());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeCreate + 1);
        ViewPerDate testViewPerDate = viewPerDateList.get(viewPerDateList.size() - 1);
        assertThat(testViewPerDate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testViewPerDate.getViews()).isEqualTo(DEFAULT_VIEWS);
    }

    @Test
    @Transactional
    void createViewPerDateWithExistingId() throws Exception {
        // Create the ViewPerDate with an existing ID
        viewPerDate.setId(1L);

        int databaseSizeBeforeCreate = viewPerDateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewPerDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPerDate)))
            .andExpect(status().isBadRequest());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewPerDateRepository.findAll().size();
        // set the field null
        viewPerDate.setDate(null);

        // Create the ViewPerDate, which fails.

        restViewPerDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPerDate)))
            .andExpect(status().isBadRequest());

        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewsIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewPerDateRepository.findAll().size();
        // set the field null
        viewPerDate.setViews(null);

        // Create the ViewPerDate, which fails.

        restViewPerDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPerDate)))
            .andExpect(status().isBadRequest());

        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewPerDates() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        // Get all the viewPerDateList
        restViewPerDateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPerDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS)));
    }

    @Test
    @Transactional
    void getViewPerDate() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        // Get the viewPerDate
        restViewPerDateMockMvc
            .perform(get(ENTITY_API_URL_ID, viewPerDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewPerDate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS));
    }

    @Test
    @Transactional
    void getNonExistingViewPerDate() throws Exception {
        // Get the viewPerDate
        restViewPerDateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingViewPerDate() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();

        // Update the viewPerDate
        ViewPerDate updatedViewPerDate = viewPerDateRepository.findById(viewPerDate.getId()).get();
        // Disconnect from session so that the updates on updatedViewPerDate are not directly saved in db
        em.detach(updatedViewPerDate);
        updatedViewPerDate.date(UPDATED_DATE).views(UPDATED_VIEWS);

        restViewPerDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedViewPerDate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedViewPerDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
        ViewPerDate testViewPerDate = viewPerDateList.get(viewPerDateList.size() - 1);
        assertThat(testViewPerDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testViewPerDate.getViews()).isEqualTo(UPDATED_VIEWS);
    }

    @Test
    @Transactional
    void putNonExistingViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPerDate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewPerDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewPerDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPerDate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewPerDateWithPatch() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();

        // Update the viewPerDate using partial update
        ViewPerDate partialUpdatedViewPerDate = new ViewPerDate();
        partialUpdatedViewPerDate.setId(viewPerDate.getId());

        restViewPerDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPerDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewPerDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
        ViewPerDate testViewPerDate = viewPerDateList.get(viewPerDateList.size() - 1);
        assertThat(testViewPerDate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testViewPerDate.getViews()).isEqualTo(DEFAULT_VIEWS);
    }

    @Test
    @Transactional
    void fullUpdateViewPerDateWithPatch() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();

        // Update the viewPerDate using partial update
        ViewPerDate partialUpdatedViewPerDate = new ViewPerDate();
        partialUpdatedViewPerDate.setId(viewPerDate.getId());

        partialUpdatedViewPerDate.date(UPDATED_DATE).views(UPDATED_VIEWS);

        restViewPerDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPerDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewPerDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
        ViewPerDate testViewPerDate = viewPerDateList.get(viewPerDateList.size() - 1);
        assertThat(testViewPerDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testViewPerDate.getViews()).isEqualTo(UPDATED_VIEWS);
    }

    @Test
    @Transactional
    void patchNonExistingViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewPerDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewPerDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewPerDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewPerDate() throws Exception {
        int databaseSizeBeforeUpdate = viewPerDateRepository.findAll().size();
        viewPerDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPerDateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(viewPerDate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPerDate in the database
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewPerDate() throws Exception {
        // Initialize the database
        viewPerDateRepository.saveAndFlush(viewPerDate);

        int databaseSizeBeforeDelete = viewPerDateRepository.findAll().size();

        // Delete the viewPerDate
        restViewPerDateMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewPerDate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViewPerDate> viewPerDateList = viewPerDateRepository.findAll();
        assertThat(viewPerDateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
