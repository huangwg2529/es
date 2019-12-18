package org.buaa.es.repository;

import org.buaa.es.entity.PaperEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PaperRepository extends ElasticsearchRepository<PaperEs, String> {

    Page<PaperEs> findDistinctPaperEsByTitle(String title, Pageable pageable);

    Page<PaperEs> findPaperEsByKeyword(String keyword, Pageable pageable);

    Page<PaperEs> findPaperEsByAbstracts(String abstracts, Pageable pageable);

    Page<PaperEs> findPaperEsByAuthors(String authors, Pageable pageable);
}
