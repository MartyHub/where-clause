package com.bnpparibas.eqd.prs.where;

import java.util.Collection;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;

public abstract class JoinAppender<P> {

	protected final Path<? extends Collection<P>> target;

	protected final Path<P> alias;

	JoinAppender(Path<? extends Collection<P>> target, Path<P> alias) {
		this.target = target;
		this.alias = alias;
	}

	abstract void join(JPAQuery query);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		JoinAppender<?> other = (JoinAppender<?>) obj;

		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;

		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;

		return true;
	}
}
