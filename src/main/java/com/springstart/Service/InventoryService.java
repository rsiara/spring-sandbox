package com.springstart.Service;

import com.springstart.Model.CustomInventoryRepositoryImpl;
import com.springstart.Model.Entity.Inventory;
import com.springstart.Model.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SpellcheckedPage;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    CustomInventoryRepositoryImpl customInventoryRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    public Inventory findById(String id) {
        return inventoryRepository.findOne(id);
    }

    public Iterable<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Iterable<Inventory> getInventoryByTitleAndManufacturer(String title, String manufacturer) {
        return inventoryRepository.findByTitleAndManufacturer(title, manufacturer);
    }

    public Iterable<Inventory> getInventoryByPriceBeetwen(Double from, Double to) {
        return inventoryRepository.findByPriceBetween(from, to);
    }

    public Iterable<Inventory> getInventoryByAvailable(Boolean isAvailable) {
        return inventoryRepository.findByAvailable(isAvailable);
    }

    public FacetPage<Inventory> getInventoryByTitleFacetOnManufacturer(String title, String prefix, Pageable pageable) {
        return inventoryRepository.findByTitleFacetOnManufacturer(title, prefix, pageable);
    }

    public Iterable<FacetFieldEntry> getInventoryFacetRangeOnPrice(Double start, Double end, Integer gap ){
        return  customInventoryRepository.getInventoryFacetRangeOnPrice(start, end, gap);
    }

    public HighlightPage<Inventory> getInventoryByTitleAndHighlight(String title, Pageable pagable) {
        return  inventoryRepository.findByTitleAndHighlight(title, pagable);
    }

    public HighlightPage<Inventory> getInventoryHighlight(String queryString, Integer fragsieze) {
        return  customInventoryRepository.getInventoryHighlight(queryString, fragsieze);
    }

    public SpellcheckedPage<Inventory>getInventoryByManufacturerAndSpellcheck(String manufacturer, Pageable pageable){
        return inventoryRepository.findByManufacturer(manufacturer, pageable);
    }

    public SpellcheckedPage<Inventory> getInventoryByManufacturerWithSpellcheck(String manufacturer) {
        return customInventoryRepository.getInventoryManufacturerWithSpellcheck(manufacturer);
    }
}
