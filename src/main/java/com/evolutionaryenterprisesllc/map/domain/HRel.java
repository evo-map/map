package com.evolutionaryenterprisesllc.map.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.evolutionaryenterprisesllc.map.domain.enumeration.RelationshipSemantic;

import com.evolutionaryenterprisesllc.map.domain.enumeration.DeletionSemantic;

/**
 * A HRel.
 */
@Entity
@Table(name = "h_rel")
public class HRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_semantic")
    private RelationshipSemantic relationshipSemantic;

    @Enumerated(EnumType.STRING)
    @Column(name = "deletion_semantic")
    private DeletionSemantic deletionSemantic;

    @Column(name = "max_to_cardinality")
    private Long maxToCardinality;

    @Column(name = "max_from_cardinality")
    private Long maxFromCardinality;

    @Column(name = "requires_to_holon")
    private Boolean requiresToHolon;

    @Column(name = "requires_from_holon")
    private Boolean requiresFromHolon;

    @ManyToOne
    private HType fromHolon;

    @ManyToOne
    private HType toHolon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelationshipSemantic getRelationshipSemantic() {
        return relationshipSemantic;
    }

    public HRel relationshipSemantic(RelationshipSemantic relationshipSemantic) {
        this.relationshipSemantic = relationshipSemantic;
        return this;
    }

    public void setRelationshipSemantic(RelationshipSemantic relationshipSemantic) {
        this.relationshipSemantic = relationshipSemantic;
    }

    public DeletionSemantic getDeletionSemantic() {
        return deletionSemantic;
    }

    public HRel deletionSemantic(DeletionSemantic deletionSemantic) {
        this.deletionSemantic = deletionSemantic;
        return this;
    }

    public void setDeletionSemantic(DeletionSemantic deletionSemantic) {
        this.deletionSemantic = deletionSemantic;
    }

    public Long getMaxToCardinality() {
        return maxToCardinality;
    }

    public HRel maxToCardinality(Long maxToCardinality) {
        this.maxToCardinality = maxToCardinality;
        return this;
    }

    public void setMaxToCardinality(Long maxToCardinality) {
        this.maxToCardinality = maxToCardinality;
    }

    public Long getMaxFromCardinality() {
        return maxFromCardinality;
    }

    public HRel maxFromCardinality(Long maxFromCardinality) {
        this.maxFromCardinality = maxFromCardinality;
        return this;
    }

    public void setMaxFromCardinality(Long maxFromCardinality) {
        this.maxFromCardinality = maxFromCardinality;
    }

    public Boolean isRequiresToHolon() {
        return requiresToHolon;
    }

    public HRel requiresToHolon(Boolean requiresToHolon) {
        this.requiresToHolon = requiresToHolon;
        return this;
    }

    public void setRequiresToHolon(Boolean requiresToHolon) {
        this.requiresToHolon = requiresToHolon;
    }

    public Boolean isRequiresFromHolon() {
        return requiresFromHolon;
    }

    public HRel requiresFromHolon(Boolean requiresFromHolon) {
        this.requiresFromHolon = requiresFromHolon;
        return this;
    }

    public void setRequiresFromHolon(Boolean requiresFromHolon) {
        this.requiresFromHolon = requiresFromHolon;
    }

    public HType getFromHolon() {
        return fromHolon;
    }

    public HRel fromHolon(HType hType) {
        this.fromHolon = hType;
        return this;
    }

    public void setFromHolon(HType hType) {
        this.fromHolon = hType;
    }

    public HType getToHolon() {
        return toHolon;
    }

    public HRel toHolon(HType hType) {
        this.toHolon = hType;
        return this;
    }

    public void setToHolon(HType hType) {
        this.toHolon = hType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HRel hRel = (HRel) o;
        if (hRel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hRel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HRel{" +
            "id=" + id +
            ", relationshipSemantic='" + relationshipSemantic + "'" +
            ", deletionSemantic='" + deletionSemantic + "'" +
            ", maxToCardinality='" + maxToCardinality + "'" +
            ", maxFromCardinality='" + maxFromCardinality + "'" +
            ", requiresToHolon='" + requiresToHolon + "'" +
            ", requiresFromHolon='" + requiresFromHolon + "'" +
            '}';
    }
}
