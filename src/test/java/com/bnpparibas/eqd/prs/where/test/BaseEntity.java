package com.bnpparibas.eqd.prs.where.test;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	@Id
	private Long id;

	public Long getId() {
		return id;
	}
}
