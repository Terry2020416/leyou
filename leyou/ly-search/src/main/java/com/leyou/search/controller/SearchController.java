package com.leyou.search.controller;

import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.search.utils.SearchRequest;
import com.leyou.search.utils.SearchResult;
import com.leyou.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    //http://api.leyou.com/api/search/page
//    @PostMapping("page")
//    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
//        PageResult<Goods> result = searchService.search(searchRequest);
//        if(result != null && result.getItems().size() != 0)
//            return ResponseEntity.ok(result);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        SearchResult searchResult = searchService.search(searchRequest);
        if(searchResult != null && searchResult.getItems().size() != 0)
            return ResponseEntity.ok(searchResult);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


