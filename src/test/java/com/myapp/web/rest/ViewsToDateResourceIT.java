package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.ViewsToDate;
import com.myapp.repository.ViewsToDateRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ViewsToDateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ViewsToDateResourceIT {

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VIEWS = 1;
    private static final Integer UPDATED_VIEWS = 2;

    private static final String ENTITY_API_URL = "/api/views-to-dates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ViewsToDateRepository viewsToDateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViewsToDateMockMvc;

    private ViewsToDate viewsToDate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewsToDate createEntity(EntityManager em) {
        ViewsToDate viewsToDate = new ViewsToDate().date(DEFAULT_DATE).views(DEFAULT_VIEWS);
        return viewsToDate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewsToDate createUpdatedEntity(EntityManager em) {
        ViewsToDate viewsToDate = new ViewsToDate().date(UPDATED_DATE).views(UPDATED_VIEWS);
        return viewsToDate;
    }

    @BeforeEach
    public void initTest() {
        viewsToDate = createEntity(em);
    }

    @Test
    @Transactional
    void createViewsToDate() throws Exception {
        int databaseSizeBeforeCreate = viewsToDateRepository.findAll().size();
        // Create the ViewsToDate
        restViewsToDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewsToDate)))
            .andExpect(status().isCreated());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeCreate + 1);
        ViewsToDate testViewsToDate = viewsToDateList.get(viewsToDateList.size() - 1);
        assertThat(testViewsToDate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testViewsToDate.getViews()).isEqualTo(DEFAULT_VIEWS);
    }

    @Test
    @Transactional
    void createViewsToDateWithExistingId() throws Exception {
        // Create the ViewsToDate with an existing ID
        viewsToDate.setId(1L);

        int databaseSizeBeforeCreate = viewsToDateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewsToDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewsToDate)))
            .andExpect(status().isBadRequest());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewsToDateRepository.findAll().size();
        // set the field null
        viewsToDate.setDate(null);

        // Create the ViewsToDate, which fails.

        restViewsToDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewsToDate)))
            .andExpect(status().isBadRequest());

        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewsIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewsToDateRepository.findAll().size();
        // set the field null
        viewsToDate.setViews(null);

        // Create the ViewsToDate, which fails.

        restViewsToDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewsToDate)))
            .andExpect(status().isBadRequest());

        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewsToDates() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        // Get all the viewsToDateList
        restViewsToDateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewsToDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS)));
    }

    @Test
    @Transactional
    void getViewsToDate() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        // Get the viewsToDate
        restViewsToDateMockMvc
            .perform(get(ENTITY_API_URL_ID, viewsToDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewsToDate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS));
    }

    @Test
    @Transactional
    void getNonExistingViewsToDate() throws Exception {
        // Get the viewsToDate
        restViewsToDateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingViewsToDate() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();

        // Update the viewsToDate
        ViewsToDate updatedViewsToDate = viewsToDateRepository.findById(viewsToDate.getId()).get();
        // Disconnect from session so that the updates on updatedViewsToDate are not directly saved in db
        em.detach(updatedViewsToDate);
        updatedViewsToDate.date(UPDATED_DATE).views(UPDATED_VIEWS);

        restViewsToDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedViewsToDate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedViewsToDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
        ViewsToDate testViewsToDate = viewsToDateList.get(viewsToDateList.size() - 1);
        assertThat(testViewsToDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testViewsToDate.getViews()).isEqualTo(UPDATED_VIEWS);
    }

    @Test
    @Transactional
    void putNonExistingViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewsToDate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewsToDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewsToDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewsToDate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewsToDateWithPatch() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();

        // Update the viewsToDate using partial update
        ViewsToDate partialUpdatedViewsToDate = new ViewsToDate();
        partialUpdatedViewsToDate.setId(viewsToDate.getId());

        partialUpdatedViewsToDate.date(UPDATED_DATE).views(UPDATED_VIEWS);

        restViewsToDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewsToDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewsToDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
        ViewsToDate testViewsToDate = viewsToDateList.get(viewsToDateList.size() - 1);
        assertThat(testViewsToDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testViewsToDate.getViews()).isEqualTo(UPDATED_VIEWS);
    }

    @Test
    @Transactional
    void fullUpdateViewsToDateWithPatch() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();

        // Update the viewsToDate using partial update
        ViewsToDate partialUpdatedViewsToDate = new ViewsToDate();
        partialUpdatedViewsToDate.setId(viewsToDate.getId());

        partialUpdatedViewsToDate.date(UPDATED_DATE).views(UPDATED_VIEWS);

        restViewsToDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewsToDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewsToDate))
            )
            .andExpect(status().isOk());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
        ViewsToDate testViewsToDate = viewsToDateList.get(viewsToDateList.size() - 1);
        assertThat(testViewsToDate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testViewsToDate.getViews()).isEqualTo(UPDATED_VIEWS);
    }

    @Test
    @Transactional
    void patchNonExistingViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewsToDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewsToDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewsToDate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewsToDate() throws Exception {
        int databaseSizeBeforeUpdate = viewsToDateRepository.findAll().size();
        viewsToDate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewsToDateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(viewsToDate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewsToDate in the database
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewsToDate() throws Exception {
        // Initialize the database
        viewsToDateRepository.saveAndFlush(viewsToDate);

        int databaseSizeBeforeDelete = viewsToDateRepository.findAll().size();

        // Delete the viewsToDate
        restViewsToDateMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewsToDate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViewsToDate> viewsToDateList = viewsToDateRepository.findAll();
        assertThat(viewsToDateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
