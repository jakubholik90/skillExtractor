// ProjectUploadRequest.java
package com.skillextractor.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProjectUploadRequest {
    private String projectName;
    private String description;
    private List<FileData> files;

    @Data
    public static class FileData {
        private String filename;
        private String content; // Base64 encoded or plain text
        private String extension;
    }
}

// ============================================

