package com.edwin_kesuma.Neon.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.edwin_kesuma.Neon.domain.dtos.ResponseCloudinaryUploadDTO;
import com.edwin_kesuma.Neon.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public ResponseCloudinaryUploadDTO uploadFile(MultipartFile file, String folder) {
        try {
            Map options =
                    ObjectUtils.asMap(
                            "folder", folder
                    );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String imageUrl = "";

            if (Objects.equals(folder, "categories")) {
                imageUrl =
                        uploadResult.get("secure_url").toString().replace("/upload/", "/upload/w_400,f_auto,q_auto/");
            } else {
                imageUrl = uploadResult.get("secure_url").toString().replace("/upload/", "/upload/f_auto,q_auto/");
            }

            String publicId = uploadResult.get("public_id").toString();

            return new ResponseCloudinaryUploadDTO(imageUrl, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image");
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String status = (String) result.get("result");

            if (!"ok".equals(status) && !"not found".equals(status)) {
                throw new RuntimeException("Failed to delete image from Cloudinary");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }
}
