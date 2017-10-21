package com.springstart.Model;

import com.springstart.Model.Entity.Inventory;
import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SpellcheckedPage;
import org.springframework.stereotype.Repository;

@Repository
public class CustomInventoryRepositoryImpl {


    @Autowired
    SolrOperations solrTemplate;

    public SpellcheckedPage<Inventory> getInventoryManufacturerWithSpellcheck(String manufacturer) {

        SimpleQuery q = new SimpleQuery(manufacturer);
        q.setSpellcheckOptions(SpellcheckOptions.spellcheck()
                .count(5)
                .extendedResults());
        q.setRequestHandler("/select");

        SpellcheckedPage<Inventory> found = solrTemplate.query(q, Inventory.class);

        return found;
    }

    public Iterable<FacetFieldEntry> getInventoryFacetRangeOnPrice(Double start, Double end, Integer gap) {

        FacetOptions facetOptions = new FacetOptions()
                .addFacetByRange(
                        new FacetOptions.FieldWithNumericRangeParameters("price", start, end, gap)
                                .setHardEnd(true)
                                .setInclude(FacetParams.FacetRangeInclude.ALL)
                );

        facetOptions.setFacetMinCount(1);

        Criteria criteria = new SimpleStringCriteria("*:*");
        SimpleFacetQuery facetQuery = new SimpleFacetQuery(criteria).setFacetOptions(facetOptions);
        FacetPage<Inventory> statResultPage = solrTemplate.queryForFacetPage(facetQuery, Inventory.class);

        Page<FacetFieldEntry> facetFieldEntries = statResultPage.getFacetResultPage("price");

        return facetFieldEntries;
    }


    public HighlightPage<Inventory> getInventoryHighlight(String queryString, Integer fragsieze) {

        SimpleHighlightQuery query = new SimpleHighlightQuery(new SimpleStringCriteria(queryString));
        query.setHighlightOptions(new HighlightOptions().addHighlightParameter("hl.fragsize", 10));
        HighlightPage<Inventory> page = solrTemplate.queryForHighlightPage(query, Inventory.class);


        return page;
    }

}
