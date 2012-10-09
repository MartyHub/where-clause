package com.bnpparibas.eqd.prs.where.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "test_leg")
public class LegEntity extends BaseEntity {

	@ManyToOne(optional = false)
	private RootEntity parent;

	@Column
	@Enumerated(EnumType.STRING)
	private LegType type;

	public LegEntity() {
	}

	public LegEntity(RootEntity parent) {
		this.parent = parent;
	}

	public RootEntity getParent() {
		return parent;
	}

	void setParent(RootEntity parent) {
		this.parent = parent;
	}

	public LegType getType() {
		return type;
	}

	public void setType(LegType type) {
		this.type = type;
	}
}
