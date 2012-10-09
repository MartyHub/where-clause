package com.bnpparibas.eqd.prs.where.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "test_root")
public class RootEntity extends BaseEntity {

	@Column
	private String stringColumn;

	@Column
	private Boolean booleanColumn;

	@Column
	@Temporal(TemporalType.DATE)
	private Date dateColumn;

	@Column
	private Double doubleColumn;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Collection<LegEntity> legs;

	public String getStringColumn() {
		return stringColumn;
	}

	public void setStringColumn(String stringColumn) {
		this.stringColumn = stringColumn;
	}

	public Boolean getBooleanColumn() {
		return booleanColumn;
	}

	public void setBooleanColumn(Boolean booleanColumn) {
		this.booleanColumn = booleanColumn;
	}

	public Date getDateColumn() {
		return dateColumn;
	}

	public void setDateColumn(Date dateColumn) {
		this.dateColumn = dateColumn;
	}

	public void addLeg(LegEntity leg) {
		if (legs == null) {
			legs = new ArrayList<LegEntity>();
		}

		leg.setParent(this);
		legs.add(leg);
	}

	public LegEntity[] getLegs() {
		if (legs == null) {
			return new LegEntity[0];
		} else {
			return legs.toArray(new LegEntity[legs.size()]);
		}
	}
}
