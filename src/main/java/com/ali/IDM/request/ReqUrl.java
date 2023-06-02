package com.ali.IDM.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class ReqUrl {
    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}" +
            "(:[0-9]{1,5})?(\\/.*)?$", message = "Invalid URL")
    private String url;
}
