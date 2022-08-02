package com.assignment.recipes.repository;

import java.util.function.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private Specification<T> specification;

    public SpecificationBuilder<T> and(final Specification<T> spec) {
        specification = Specification.where(spec).and(specification);
        return this;
    }

    public SpecificationBuilder<T> andOnCondition(final boolean condition, final Supplier<Specification<T>> spec) {
        return condition ? and(spec.get()) : this;
    }

    public Specification<T> build() {
        return Specification.where(specification);
    }

}
