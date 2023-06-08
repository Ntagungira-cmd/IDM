package com.ali.IDM.services;

import com.ali.IDM.model.Link;
import com.ali.IDM.repository.LinkRepository;
import com.ali.IDM.request.ReqLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    LinkRepository linkRepository;

    public void create(ReqLink linkDTO) throws IOException {
        Link link = new Link();

        String filePath= linkDTO.getPath() + "index.html";
        link.setLink_name(linkDTO.getUrl().getFile());
        link.setWebsite(linkDTO.getWebsite());

        LocalDateTime start = LocalDateTime.now();

        BufferedReader readr =
                new BufferedReader(new InputStreamReader(linkDTO.getUrl().openStream()));
        // Enter filename in which you want to download
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(filePath));
        // read each line from stream till end
        String line;
        while ((line = readr.readLine()) != null) {
            writer.write(line);
        }
        readr.close();
        writer.close();
        System.out.println("Done with "+ filePath);
        LocalDateTime end = LocalDateTime.now();
        link.setTotal_elapsed_time(Duration.between(start,end).toMillis());
        link.setTotal_downloaded_kilobytes(Files.size(Paths.get(filePath)) / 1024);
        link.setId(UUID.randomUUID());
        linkRepository.save(link);
    }

}
