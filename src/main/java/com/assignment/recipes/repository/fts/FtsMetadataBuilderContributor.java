package com.assignment.recipes.repository.fts;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public class FtsMetadataBuilderContributor implements MetadataBuilderContributor {

    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction("fts",
            new SQLFunctionTemplate(BooleanType.INSTANCE,
                "to_tsvector(instructions) @@ plainto_tsquery(?1)"));
    }
}
