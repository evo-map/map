package com.evolutionaryenterprisesllc.map.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A HType.
 */
@Entity
@Table(name = "h_type")
public class HType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "fromHolon")
    @JsonIgnore
    private Set<HRel> fromOfs = new HashSet<>();

    @OneToMany(mappedBy = "toHolon")
    @JsonIgnore
    private Set<HRel> toOfs = new HashSet<>();

    @OneToMany(mappedBy = "hType")
    @JsonIgnore
    private Set<HVisualizer> visualizers = new HashSet<>();

    @ManyToOne
    private HDescriptor hDescriptor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HRel> getFromOfs() {
        return fromOfs;
    }

    public HType fromOfs(Set<HRel> hRels) {
        this.fromOfs = hRels;
        return this;
    }

    public HType addFromOf(HRel hRel) {
        fromOfs.add(hRel);
        hRel.setFromHolon(this);
        return this;
    }

    public HType removeFromOf(HRel hRel) {
        fromOfs.remove(hRel);
        hRel.setFromHolon(null);
        return this;
    }

    public void setFromOfs(Set<HRel> hRels) {
        this.fromOfs = hRels;
    }

    public Set<HRel> getToOfs() {
        return toOfs;
    }

    public HType toOfs(Set<HRel> hRels) {
        this.toOfs = hRels;
        return this;
    }

    public HType addToOf(HRel hRel) {
        toOfs.add(hRel);
        hRel.setToHolon(this);
        return this;
    }

    public HType removeToOf(HRel hRel) {
        toOfs.remove(hRel);
        hRel.setToHolon(null);
        return this;
    }

    public void setToOfs(Set<HRel> hRels) {
        this.toOfs = hRels;
    }

    public Set<HVisualizer> getVisualizers() {
        return visualizers;
    }

    public HType visualizers(Set<HVisualizer> hVisualizers) {
        this.visualizers = hVisualizers;
        return this;
    }

    public HType addVisualizers(HVisualizer hVisualizer) {
        visualizers.add(hVisualizer);
        hVisualizer.setHType(this);
        return this;
    }

    public HType removeVisualizers(HVisualizer hVisualizer) {
        visualizers.remove(hVisualizer);
        hVisualizer.setHType(null);
        return this;
    }

    public void setVisualizers(Set<HVisualizer> hVisualizers) {
        this.visualizers = hVisualizers;
    }

    public HDescriptor getHDescriptor() {
        return hDescriptor;
    }

    public HType hDescriptor(HDescriptor hDescriptor) {
        this.hDescriptor = hDescriptor;
        return this;
    }

    public void setHDescriptor(HDescriptor hDescriptor) {
        this.hDescriptor = hDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HType hType = (HType) o;
        if (hType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HType{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
