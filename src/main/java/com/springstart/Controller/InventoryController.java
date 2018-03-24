package com.springstart.Controller;


import com.springstart.Model.Entity.Inventory;
import com.springstart.Service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SpellcheckedPage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    InventoryService inventoryService;

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Inventory> getAllInventory() {
        logger.info("GET all inventory invoked");
        return inventoryService.getAllInventory();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Inventory getInventoryById(@PathVariable("id") String id) {
        logger.info("GET inventory id: " + id + " invoked");
        return inventoryService.findById(id);
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Inventory> getInventoryByTitleAndManufacture(@RequestParam("title") String title, @RequestParam("manufacturer") String manufacturer) {
        logger.info("GET inventory by title: " + title + " and manufacturer: " + manufacturer + " invoked");
        return inventoryService.getInventoryByTitleAndManufacturer(title, manufacturer);
    }


    @RequestMapping(value = "/getbyprice", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Inventory> getInventoryByPriceBeetwen(@RequestParam("from") Double from, @RequestParam("to") Double to) {
        logger.info("GET inventory by price beetwen: " + from + " to " + to + " invoked");
        return inventoryService.getInventoryByPriceBeetwen(from, to);
    }

    @RequestMapping(value = "/getbyavailable", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Inventory> getInventoryByPriceBeetwen(@RequestParam("available") Boolean isAvailable) {
        logger.info("GET inventory by available: " + isAvailable);
        return inventoryService.getInventoryByAvailable(isAvailable);
    }

    @RequestMapping(value = "/getbytitlefacetonmanufacturer", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public FacetPage<Inventory> getInventoryByTitleFacetOnManufacturer(@RequestParam("title") String title, @RequestParam("prefix") String prefix,Pageable pagable) {
        logger.info("GET inventory by title: " + title + " facet on manufacturer and prefix: " + prefix);
        return inventoryService.getInventoryByTitleFacetOnManufacturer(title,prefix,pagable);
    }

    @RequestMapping(value = "/getfacetrangeonprice", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<FacetFieldEntry> getInventoryFacetRangeOnPrice(@RequestParam("start") Double start, @RequestParam("end") Double end, @RequestParam("gap") Integer gap) {
        logger.info("GET inventory facet range on price, start: " + start + " end: " + end + " gap: "+ gap);
        return inventoryService.getInventoryFacetRangeOnPrice(start,end,gap);
    }

    @RequestMapping(value = "/getbytitleandhighlight", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public HighlightPage<Inventory> getInventoryByTitleAndHighlight(@RequestParam("title") String title, Pageable pagable) {
        logger.info("GET inventory by title: " + title + " and highlight" );
        return inventoryService.getInventoryByTitleAndHighlight(title, pagable);
    }

    @RequestMapping(value = "/getbycustomqueryandhighlight", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public HighlightPage<Inventory> getInventoryByCustomQueryAndHighLight(@RequestParam("q") String queryString, @RequestParam("fragsize") Integer fragsize) {
        logger.info("GET inventory and highlight, query: " + queryString + " and fragsize: " + fragsize );
        return inventoryService.getInventoryHighlight(queryString, fragsize);
    }

    @RequestMapping(value = "/getbymanufacturerandspellcheck", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public SpellcheckedPage<Inventory> getInventoryByManufacturerAndSpellcheck(@RequestParam("manufacturer") String manufacturer, Pageable pageable) {
        logger.info("GET inventory by manufacturer: " + manufacturer + " and spellcheck");
        return inventoryService.getInventoryByManufacturerAndSpellcheck(manufacturer, pageable);
    }

    @RequestMapping(value = "/getbymanufacturerwithspellcheck", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public SpellcheckedPage<Inventory> getInventoryByTitleAndSpellcheck(@RequestParam("manufacturer") String manufacturer) {
        logger.info("GET inventory by manufacturer: " + manufacturer + " with  spellcheck");
        return inventoryService.getInventoryByManufacturerWithSpellcheck(manufacturer);
    }

}
