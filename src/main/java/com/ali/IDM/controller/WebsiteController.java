package com.ali.IDM.controller;

import com.ali.IDM.Utility.APIResponse;
import com.ali.IDM.model.Website;
import com.ali.IDM.services.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/site")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteService website;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(website.all());
    }

    @GetMapping("/{websiteId}")
    public ResponseEntity<?> getById(@PathVariable UUID websiteId) {
        Website site =website.getById(websiteId);
        if (site!=null){
            return ResponseEntity.ok(site);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new APIResponse(false,"Website not found"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createWebsite(@Valid @RequestBody String url){

        if(url!=null)return ResponseEntity.ok(website.create(url));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse(false,"Invalid URL"));
    }

}
