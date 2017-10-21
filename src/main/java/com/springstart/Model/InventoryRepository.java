package com.springstart.Model;

import com.springstart.Model.Entity.Inventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SpellcheckedPage;
import org.springframework.data.solr.repository.*;

import java.util.List;

public interface InventoryRepository extends SolrCrudRepository<Inventory, String> {

    List<Inventory> findByTitleAndManufacturer(String title, String maufacturer);

    List<Inventory> findByPriceBetween(Double from, Double to);

    @Query("instock:?0")
    List<Inventory> findByAvailable(Boolean isAvailable);

    @Query("title:?0")
    @Facet(fields = {"manufacturer"}, limit = 5, prefix = "?1")
    FacetPage<Inventory> findByTitleFacetOnManufacturer(String title, String prefix, Pageable page);

    @Query("title:?0")
    @Highlight(prefix = "<b>", postfix = "</b>")
    HighlightPage<Inventory> findByTitleAndHighlight(String title, Pageable page);

    @Query(requestHandler = "/select")
    @Spellcheck(count = 5, extendedResults = true)
    SpellcheckedPage<Inventory> findByManufacturer(String manufacturer, Pageable page);

}
