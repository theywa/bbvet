package com.ra_bbvet.app.web.rest;

import com.ra_bbvet.app.RaBbvetApp;
import com.ra_bbvet.app.domain.Kegiatan;
import com.ra_bbvet.app.repository.KegiatanRepository;

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
 * Test class for the KegiatanResource REST controller.
 *
 * @see KegiatanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RaBbvetApp.class)
@WebAppConfiguration
@IntegrationTest
public class KegiatanResourceIntTest {

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
    private KegiatanRepository kegiatanRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restKegiatanMockMvc;

    private Kegiatan kegiatan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KegiatanResource kegiatanResource = new KegiatanResource();
        ReflectionTestUtils.setField(kegiatanResource, "kegiatanRepository", kegiatanRepository);
        this.restKegiatanMockMvc = MockMvcBuilders.standaloneSetup(kegiatanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        kegiatan = new Kegiatan();
        kegiatan.setKode(DEFAULT_KODE);
        kegiatan.setJudul(DEFAULT_JUDUL);
        kegiatan.setCreatedDate(DEFAULT_CREATED_DATE);
        kegiatan.setCreatedBy(DEFAULT_CREATED_BY);
        kegiatan.setUpdatedDate(DEFAULT_UPDATED_DATE);
        kegiatan.setUpdatedBy(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createKegiatan() throws Exception {
        int databaseSizeBeforeCreate = kegiatanRepository.findAll().size();

        // Create the Kegiatan

        restKegiatanMockMvc.perform(post("/api/kegiatans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(kegiatan)))
                .andExpect(status().isCreated());

        // Validate the Kegiatan in the database
        List<Kegiatan> kegiatans = kegiatanRepository.findAll();
        assertThat(kegiatans).hasSize(databaseSizeBeforeCreate + 1);
        Kegiatan testKegiatan = kegiatans.get(kegiatans.size() - 1);
        assertThat(testKegiatan.getKode()).isEqualTo(DEFAULT_KODE);
        assertThat(testKegiatan.getJudul()).isEqualTo(DEFAULT_JUDUL);
        assertThat(testKegiatan.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testKegiatan.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testKegiatan.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testKegiatan.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllKegiatans() throws Exception {
        // Initialize the database
        kegiatanRepository.saveAndFlush(kegiatan);

        // Get all the kegiatans
        restKegiatanMockMvc.perform(get("/api/kegiatans?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(kegiatan.getId().intValue())))
                .andExpect(jsonPath("$.[*].kode").value(hasItem(DEFAULT_KODE.toString())))
                .andExpect(jsonPath("$.[*].judul").value(hasItem(DEFAULT_JUDUL.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getKegiatan() throws Exception {
        // Initialize the database
        kegiatanRepository.saveAndFlush(kegiatan);

        // Get the kegiatan
        restKegiatanMockMvc.perform(get("/api/kegiatans/{id}", kegiatan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(kegiatan.getId().intValue()))
            .andExpect(jsonPath("$.kode").value(DEFAULT_KODE.toString()))
            .andExpect(jsonPath("$.judul").value(DEFAULT_JUDUL.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKegiatan() throws Exception {
        // Get the kegiatan
        restKegiatanMockMvc.perform(get("/api/kegiatans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKegiatan() throws Exception {
        // Initialize the database
        kegiatanRepository.saveAndFlush(kegiatan);
        int databaseSizeBeforeUpdate = kegiatanRepository.findAll().size();

        // Update the kegiatan
        Kegiatan updatedKegiatan = new Kegiatan();
        updatedKegiatan.setId(kegiatan.getId());
        updatedKegiatan.setKode(UPDATED_KODE);
        updatedKegiatan.setJudul(UPDATED_JUDUL);
        updatedKegiatan.setCreatedDate(UPDATED_CREATED_DATE);
        updatedKegiatan.setCreatedBy(UPDATED_CREATED_BY);
        updatedKegiatan.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedKegiatan.setUpdatedBy(UPDATED_UPDATED_BY);

        restKegiatanMockMvc.perform(put("/api/kegiatans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedKegiatan)))
                .andExpect(status().isOk());

        // Validate the Kegiatan in the database
        List<Kegiatan> kegiatans = kegiatanRepository.findAll();
        assertThat(kegiatans).hasSize(databaseSizeBeforeUpdate);
        Kegiatan testKegiatan = kegiatans.get(kegiatans.size() - 1);
        assertThat(testKegiatan.getKode()).isEqualTo(UPDATED_KODE);
        assertThat(testKegiatan.getJudul()).isEqualTo(UPDATED_JUDUL);
        assertThat(testKegiatan.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testKegiatan.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testKegiatan.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testKegiatan.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void deleteKegiatan() throws Exception {
        // Initialize the database
        kegiatanRepository.saveAndFlush(kegiatan);
        int databaseSizeBeforeDelete = kegiatanRepository.findAll().size();

        // Get the kegiatan
        restKegiatanMockMvc.perform(delete("/api/kegiatans/{id}", kegiatan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Kegiatan> kegiatans = kegiatanRepository.findAll();
        assertThat(kegiatans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
