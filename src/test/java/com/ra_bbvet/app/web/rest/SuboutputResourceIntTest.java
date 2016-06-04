package com.ra_bbvet.app.web.rest;

import com.ra_bbvet.app.RaBbvetApp;
import com.ra_bbvet.app.domain.Suboutput;
import com.ra_bbvet.app.repository.SuboutputRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SuboutputResource REST controller.
 *
 * @see SuboutputResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaBbvetApp.class)
@WebAppConfiguration
@IntegrationTest
public class SuboutputResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_KODE = "AAAAA";
    private static final String UPDATED_KODE = "BBBBB";
    private static final String DEFAULT_JUDUL = "AAAAA";
    private static final String UPDATED_JUDUL = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);
    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_DATE_STR = dateTimeFormatter.format(DEFAULT_UPDATED_DATE);
    private static final String DEFAULT_UPDATED_BY = "AAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBB";

    @Inject
    private SuboutputRepository suboutputRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSuboutputMockMvc;

    private Suboutput suboutput;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuboutputResource suboutputResource = new SuboutputResource();
        ReflectionTestUtils.setField(suboutputResource, "suboutputRepository", suboutputRepository);
        this.restSuboutputMockMvc = MockMvcBuilders.standaloneSetup(suboutputResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        suboutput = new Suboutput();
        suboutput.setKode(DEFAULT_KODE);
        suboutput.setJudul(DEFAULT_JUDUL);
        suboutput.setCreatedDate(DEFAULT_CREATED_DATE);
        suboutput.setCreatedBy(DEFAULT_CREATED_BY);
        suboutput.setUpdatedDate(DEFAULT_UPDATED_DATE);
        suboutput.setUpdatedBy(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createSuboutput() throws Exception {
        int databaseSizeBeforeCreate = suboutputRepository.findAll().size();

        // Create the Suboutput

        restSuboutputMockMvc.perform(post("/api/suboutputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(suboutput)))
                .andExpect(status().isCreated());

        // Validate the Suboutput in the database
        List<Suboutput> suboutputs = suboutputRepository.findAll();
        assertThat(suboutputs).hasSize(databaseSizeBeforeCreate + 1);
        Suboutput testSuboutput = suboutputs.get(suboutputs.size() - 1);
        assertThat(testSuboutput.getKode()).isEqualTo(DEFAULT_KODE);
        assertThat(testSuboutput.getJudul()).isEqualTo(DEFAULT_JUDUL);
        assertThat(testSuboutput.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSuboutput.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSuboutput.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testSuboutput.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllSuboutputs() throws Exception {
        // Initialize the database
        suboutputRepository.saveAndFlush(suboutput);

        // Get all the suboutputs
        restSuboutputMockMvc.perform(get("/api/suboutputs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(suboutput.getId().intValue())))
                .andExpect(jsonPath("$.[*].kode").value(hasItem(DEFAULT_KODE.toString())))
                .andExpect(jsonPath("$.[*].judul").value(hasItem(DEFAULT_JUDUL.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getSuboutput() throws Exception {
        // Initialize the database
        suboutputRepository.saveAndFlush(suboutput);

        // Get the suboutput
        restSuboutputMockMvc.perform(get("/api/suboutputs/{id}", suboutput.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(suboutput.getId().intValue()))
            .andExpect(jsonPath("$.kode").value(DEFAULT_KODE.toString()))
            .andExpect(jsonPath("$.judul").value(DEFAULT_JUDUL.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSuboutput() throws Exception {
        // Get the suboutput
        restSuboutputMockMvc.perform(get("/api/suboutputs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuboutput() throws Exception {
        // Initialize the database
        suboutputRepository.saveAndFlush(suboutput);
        int databaseSizeBeforeUpdate = suboutputRepository.findAll().size();

        // Update the suboutput
        Suboutput updatedSuboutput = new Suboutput();
        updatedSuboutput.setId(suboutput.getId());
        updatedSuboutput.setKode(UPDATED_KODE);
        updatedSuboutput.setJudul(UPDATED_JUDUL);
        updatedSuboutput.setCreatedDate(UPDATED_CREATED_DATE);
        updatedSuboutput.setCreatedBy(UPDATED_CREATED_BY);
        updatedSuboutput.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedSuboutput.setUpdatedBy(UPDATED_UPDATED_BY);

        restSuboutputMockMvc.perform(put("/api/suboutputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSuboutput)))
                .andExpect(status().isOk());

        // Validate the Suboutput in the database
        List<Suboutput> suboutputs = suboutputRepository.findAll();
        assertThat(suboutputs).hasSize(databaseSizeBeforeUpdate);
        Suboutput testSuboutput = suboutputs.get(suboutputs.size() - 1);
        assertThat(testSuboutput.getKode()).isEqualTo(UPDATED_KODE);
        assertThat(testSuboutput.getJudul()).isEqualTo(UPDATED_JUDUL);
        assertThat(testSuboutput.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSuboutput.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSuboutput.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testSuboutput.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void deleteSuboutput() throws Exception {
        // Initialize the database
        suboutputRepository.saveAndFlush(suboutput);
        int databaseSizeBeforeDelete = suboutputRepository.findAll().size();

        // Get the suboutput
        restSuboutputMockMvc.perform(delete("/api/suboutputs/{id}", suboutput.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Suboutput> suboutputs = suboutputRepository.findAll();
        assertThat(suboutputs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
