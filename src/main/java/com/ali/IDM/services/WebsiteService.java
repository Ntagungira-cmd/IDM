package com.ali.IDM.services;

import com.ali.IDM.model.Website;
import com.ali.IDM.repository.WebsiteRepository;
import com.ali.IDM.request.ReqLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ali.IDM.Utility.Utility.createFolder;

@Service
public class WebsiteService {

    final
    WebsiteRepository websiteRepository;

    final
    LinkService linkService;

    @Value("${upload.directory}")
    private String savingPath;

    public WebsiteService(WebsiteRepository websiteRepository, LinkService linkService) {
        this.websiteRepository = websiteRepository;
        this.linkService = linkService;
    }

    public List<Website> all() {
        return websiteRepository.findAll();
    }

    public Website getById(UUID id) {
        return websiteRepository.findById(id).orElse(null);
    }

    public static Set<String> findLinks(String url) throws IOException {
        Set<String> links = new HashSet<>();
        Document doc = Jsoup.connect(url).get();
        Elements elements  = doc.select("a[href]");
        for (Element e : elements) {
            links.add(e.attr("href"));
        }
        return links;
    }
    public Website create(URL url) {

        try {
            Website website = new Website();
            website.setWebsite_name(url.getHost());
            website.setDownload_start_date_time(LocalDateTime.now());
            website.setUrl(url);

            String fileName = url.getFile();
            // use index.html on urls without filename
            if (fileName.length() < 3) {
                fileName = "index.html";
            }
            String filePath = savingPath + "/" + website.getWebsite_name() + "/";
            String linksPath = filePath + "links";

            // create folder if it does not exist
            createFolder(linksPath);

            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            // Enter filename in which you want to download
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(filePath + fileName));

            // read each line from stream till end
            String line;
            while ((line = readr.readLine()) != null) {
                writer.write(line);
            }

            readr.close();
            writer.close();
            website.setDownload_end_date_time(LocalDateTime.now());

            website.setTotal_downloaded_kilobytes(Files.size(Paths.get(filePath + fileName)) / 1024);

            website.setTotal_elapsed_time(Duration.between(website.getDownload_start_date_time(), website.getDownload_end_date_time()).toMillis());

            website.setId(UUID.randomUUID());
            Website saved = websiteRepository.save(website);

            Set<String> links = findLinks(url.toExternalForm());

            for (String link : links) {
              //Do all checks to ensure that the link is valid and not a duplicate and its source file can be downloaded
              if (link == null || link.isEmpty() || link.contains("#") || link.contains("javascript") || link.contains("mailto") || link.contains("http")) {
                  continue;
              }
              ReqLink reqLink = new ReqLink();
              reqLink.setWebsite(saved);
              URL _url = new URL(url.toExternalForm()+"/"+link);
              reqLink.setUrl(_url);
              createFolder(linksPath+"/"+_url.getPath());
              reqLink.setPath(linksPath+"/"+_url.getPath());
              linkService.create(reqLink);
            }
            return saved;

        } catch (IOException e) {
           System.out.println(e.getMessage());
           return null;
        }
    }
}
