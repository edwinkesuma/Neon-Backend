package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.ResponseCloudinaryUploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    ResponseCloudinaryUploadDTO uploadFile(MultipartFile file, String folder);
    void deleteFile(String publicId);
}
