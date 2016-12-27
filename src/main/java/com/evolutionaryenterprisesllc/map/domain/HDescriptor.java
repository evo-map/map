package com.evolutionaryenterprisesllc.map.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.evolutionaryenterprisesllc.map.domain.enumeration.HDescriptorType;

/**
 * A HDescriptor.
 */
@Entity
@Table(name = "h_descriptor")
public class HDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private HDescriptorType type;

    @Column(name = "decscriptor_body")
    private String decscriptorBody;

    @OneToMany(mappedBy = "hDescriptor")
    @JsonIgnore
    private Set<HType> descriptors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HDescriptorType getType() {
        return type;
    }

    public HDescriptor type(HDescriptorType type) {
        this.type = type;
        return this;
    }

    public void setType(HDescriptorType type) {
        this.type = type;
    }

    public String getDecscriptorBody() {
        return decscriptorBody;
    }

    public HDescriptor decscriptorBody(String decscriptorBody) {
        this.decscriptorBody = decscriptorBody;
        return this;
    }

    public void setDecscriptorBody(String decscriptorBody) {
        this.decscriptorBody = decscriptorBody;
    }

    public Set<HType> getDescriptors() {
        return descriptors;
    }

    public HDescriptor descriptors(Set<HType> hTypes) {
        this.descriptors = hTypes;
        return this;
    }

    public HDescriptor addDescriptors(HType hType) {
        descriptors.add(hType);
        hType.setHDescriptor(this);
        return this;
    }

    public HDescriptor removeDescriptors(HType hType) {
        descriptors.remove(hType);
        hType.setHDescriptor(null);
        return this;
    }

    public void setDescriptors(Set<HType> hTypes) {
        this.descriptors = hTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HDescriptor hDescriptor = (HDescriptor) o;
        if (hDescriptor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hDescriptor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HDescriptor{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", decscriptorBody='" + decscriptorBody + "'" +
            '}';
    }
}
