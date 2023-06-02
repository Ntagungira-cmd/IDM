package com.ali.IDM.request;

import com.ali.IDM.model.Website;
import lombok.Data;

import java.net.URL;
@Data
public class ReqLink {
    private URL url;
    private String path;
    private Website website;
}
