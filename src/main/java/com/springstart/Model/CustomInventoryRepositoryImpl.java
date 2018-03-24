package com.springstart.Model;

import com.springstart.Model.Entity.Inventory;
import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.SpellcheckOptions;
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

        return solrTemplate.query(q, Inventory.class);
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

        return statResultPage.getFacetResultPage("price");
    }


    public HighlightPage<Inventory> getInventoryHighlight(String queryString, Integer fragsieze) {

        SimpleHighlightQuery query = new SimpleHighlightQuery(new SimpleStringCriteria(queryString));
        query.setHighlightOptions(new HighlightOptions().addHighlightParameter("hl.fragsize", 10));


        return solrTemplate.queryForHighlightPage(query, Inventory.class);
    }

}
