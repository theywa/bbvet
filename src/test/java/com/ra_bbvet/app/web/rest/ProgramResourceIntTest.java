package com.ra_bbvet.app.web.rest;

import com.ra_bbvet.app.RaBbvetApp;
import com.ra_bbvet.app.domain.Program;
import com.ra_bbvet.app.repository.ProgramRepository;

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
 * Test class for the ProgramResource REST controller.
 *
 * @see ProgramResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaBbvetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProgramResourceIntTest {

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
    private ProgramRepository programRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProgramMockMvc;

    private Program program;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProgramResource programResource = new ProgramResource();
        ReflectionTestUtils.setField(programResource, "programRepository", programRepository);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        program = new Program();
        program.setKode(DEFAULT_KODE);
        program.setJudul(DEFAULT_JUDUL);
        program.setCreatedDate(DEFAULT_CREATED_DATE);
        program.setCreatedBy(DEFAULT_CREATED_BY);
        program.setUpdatedDate(DEFAULT_UPDATED_DATE);
        program.setUpdatedBy(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program

        restProgramMockMvc.perform(post("/api/programs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(program)))
                .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programs = programRepository.findAll();
        assertThat(programs).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programs.get(programs.size() - 1);
        assertThat(testProgram.getKode()).isEqualTo(DEFAULT_KODE);
        assertThat(testProgram.getJudul()).isEqualTo(DEFAULT_JUDUL);
        assertThat(testProgram.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgram.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProgram.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testProgram.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programs
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
                .andExpect(jsonPath("$.[*].kode").value(hasItem(DEFAULT_KODE.toString())))
                .andExpect(jsonPath("$.[*].judul").value(hasItem(DEFAULT_JUDUL.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.kode").value(DEFAULT_KODE.toString()))
            .andExpect(jsonPath("$.judul").value(DEFAULT_JUDUL.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = new Program();
        updatedProgram.setId(program.getId());
        updatedProgram.setKode(UPDATED_KODE);
        updatedProgram.setJudul(UPDATED_JUDUL);
        updatedProgram.setCreatedDate(UPDATED_CREATED_DATE);
        updatedProgram.setCreatedBy(UPDATED_CREATED_BY);
        updatedProgram.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedProgram.setUpdatedBy(UPDATED_UPDATED_BY);

        restProgramMockMvc.perform(put("/api/programs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
                .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programs = programRepository.findAll();
        assertThat(programs).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programs.get(programs.size() - 1);
        assertThat(testProgram.getKode()).isEqualTo(UPDATED_KODE);
        assertThat(testProgram.getJudul()).isEqualTo(UPDATED_JUDUL);
        assertThat(testProgram.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgram.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProgram.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testProgram.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Get the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Program> programs = programRepository.findAll();
        assertThat(programs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
