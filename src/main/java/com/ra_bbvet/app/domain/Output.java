package com.ra_bbvet.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Output.
 */
@Entity
@Table(name = "output")
public class Output implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "kode")
    private String kode;

    @Column(name = "judul")
    private String judul;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "satuan")
    private String satuan;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    private Kegiatan kegiatan;

    @OneToMany(mappedBy = "output")
    @JsonIgnore
    private Set<Komponen> listKomponens = new HashSet<>();

    @OneToMany(mappedBy = "output")
    @JsonIgnore
    private Set<Suboutput> listSuboutputs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }

    public Set<Komponen> getListKomponens() {
        return listKomponens;
    }

    public void setListKomponens(Set<Komponen> komponens) {
        this.listKomponens = komponens;
    }

    public Set<Suboutput> getListSuboutputs() {
        return listSuboutputs;
    }

    public void setListSuboutputs(Set<Suboutput> suboutputs) {
        this.listSuboutputs = suboutputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Output output = (Output) o;
        if(output.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, output.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Output{" +
            "id=" + id +
            ", kode='" + kode + "'" +
            ", judul='" + judul + "'" +
            ", volume='" + volume + "'" +
            ", satuan='" + satuan + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            ", updatedDate='" + updatedDate + "'" +
            ", updatedBy='" + updatedBy + "'" +
            '}';
    }
}
