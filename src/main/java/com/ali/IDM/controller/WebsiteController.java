package com.ali.IDM.controller;

import com.ali.IDM.Utility.APIResponse;
import com.ali.IDM.model.Website;
import com.ali.IDM.request.ReqUrl;
import com.ali.IDM.services.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
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
    public ResponseEntity<?> createWebsite(@Valid @RequestBody ReqUrl url) throws MalformedURLException {
        URL url1 = new URL(url.getUrl());
        return ResponseEntity.ok(website.create(url1));
    }

}
