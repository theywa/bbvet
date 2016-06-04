package com.ra_bbvet.app.web.rest;

import com.ra_bbvet.app.RaBbvetApp;
import com.ra_bbvet.app.domain.Output;
import com.ra_bbvet.app.repository.OutputRepository;

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
 * Test class for the OutputResource REST controller.
 *
 * @see OutputResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaBbvetApp.class)
@WebAppConfiguration
@IntegrationTest
public class OutputResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_KODE = "AAAAA";
    private static final String UPDATED_KODE = "BBBBB";
    private static final String DEFAULT_JUDUL = "AAAAA";
    private static final String UPDATED_JUDUL = "BBBBB";

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;
    private static final String DEFAULT_SATUAN = "AAAAA";
    private static final String UPDATED_SATUAN = "BBBBB";

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
    private OutputRepository outputRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOutputMockMvc;

    private Output output;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutputResource outputResource = new OutputResource();
        ReflectionTestUtils.setField(outputResource, "outputRepository", outputRepository);
        this.restOutputMockMvc = MockMvcBuilders.standaloneSetup(outputResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        output = new Output();
        output.setKode(DEFAULT_KODE);
        output.setJudul(DEFAULT_JUDUL);
        output.setVolume(DEFAULT_VOLUME);
        output.setSatuan(DEFAULT_SATUAN);
        output.setCreatedDate(DEFAULT_CREATED_DATE);
        output.setCreatedBy(DEFAULT_CREATED_BY);
        output.setUpdatedDate(DEFAULT_UPDATED_DATE);
        output.setUpdatedBy(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createOutput() throws Exception {
        int databaseSizeBeforeCreate = outputRepository.findAll().size();

        // Create the Output

        restOutputMockMvc.perform(post("/api/outputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(output)))
                .andExpect(status().isCreated());

        // Validate the Output in the database
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeCreate + 1);
        Output testOutput = outputs.get(outputs.size() - 1);
        assertThat(testOutput.getKode()).isEqualTo(DEFAULT_KODE);
        assertThat(testOutput.getJudul()).isEqualTo(DEFAULT_JUDUL);
        assertThat(testOutput.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testOutput.getSatuan()).isEqualTo(DEFAULT_SATUAN);
        assertThat(testOutput.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOutput.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOutput.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testOutput.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllOutputs() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputs
        restOutputMockMvc.perform(get("/api/outputs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(output.getId().intValue())))
                .andExpect(jsonPath("$.[*].kode").value(hasItem(DEFAULT_KODE.toString())))
                .andExpect(jsonPath("$.[*].judul").value(hasItem(DEFAULT_JUDUL.toString())))
                .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
                .andExpect(jsonPath("$.[*].satuan").value(hasItem(DEFAULT_SATUAN.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", output.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(output.getId().intValue()))
            .andExpect(jsonPath("$.kode").value(DEFAULT_KODE.toString()))
            .andExpect(jsonPath("$.judul").value(DEFAULT_JUDUL.toString()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME))
            .andExpect(jsonPath("$.satuan").value(DEFAULT_SATUAN.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOutput() throws Exception {
        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output
        Output updatedOutput = new Output();
        updatedOutput.setId(output.getId());
        updatedOutput.setKode(UPDATED_KODE);
        updatedOutput.setJudul(UPDATED_JUDUL);
        updatedOutput.setVolume(UPDATED_VOLUME);
        updatedOutput.setSatuan(UPDATED_SATUAN);
        updatedOutput.setCreatedDate(UPDATED_CREATED_DATE);
        updatedOutput.setCreatedBy(UPDATED_CREATED_BY);
        updatedOutput.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedOutput.setUpdatedBy(UPDATED_UPDATED_BY);

        restOutputMockMvc.perform(put("/api/outputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOutput)))
                .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputs.get(outputs.size() - 1);
        assertThat(testOutput.getKode()).isEqualTo(UPDATED_KODE);
        assertThat(testOutput.getJudul()).isEqualTo(UPDATED_JUDUL);
        assertThat(testOutput.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testOutput.getSatuan()).isEqualTo(UPDATED_SATUAN);
        assertThat(testOutput.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOutput.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOutput.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testOutput.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void deleteOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);
        int databaseSizeBeforeDelete = outputRepository.findAll().size();

        // Get the output
        restOutputMockMvc.perform(delete("/api/outputs/{id}", output.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
