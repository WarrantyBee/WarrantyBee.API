package com.warrantybee.api.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.warrantybee.api.services.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Implementation of {@link IStorageService} that uses Cloudinary
 * as the underlying storage provider.
 * <p>
 * This service handles file uploads and deletions by interacting
 * with Cloudinary's Java SDK. Uploaded files return a secure URL,
 * while deletions use the public ID provided during upload.
 * </p>
 */
@Service
public class CloudinaryStorageService implements IStorageService {

    private final Cloudinary _cloudinary;

    /**
     * Creates a new instance of {@code CloudinaryStorageService}.
     *
     * @param cloudinary the Cloudinary client instance injected by Spring
     */
    @Autowired
    public CloudinaryStorageService(Cloudinary cloudinary) {
        this._cloudinary = cloudinary;
    }

    /**
     * Uploads a file to Cloudinary and returns the secure URL of the uploaded file.
     *
     * @param file the multipart file to upload; must not be null
     * @return the secure URL of the uploaded file, or {@code null} if the upload fails
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            Map<?, ?> options = ObjectUtils.emptyMap();
            Map<?, ?> uploadResult = _cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes a file from Cloudinary using its public identifier.
     *
     * @param id the Cloudinary public ID of the file to delete
     * @return {@code true} if deletion is successful; {@code false} otherwise
     */
    @Override
    public Boolean delete(String id) {
        try {
            Map<?, ?> options = ObjectUtils.emptyMap();
            Map<?, ?> result = _cloudinary.uploader().destroy(id, options);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the Cloudinary {@code public_id} from a given Cloudinary resource URL.
     *
     * @param url the full Cloudinary file URL; may be null or empty
     * @return the extracted {@code public_id}, or {@code null} if the URL is invalid
     */
    public static String getPublicId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            String path = parts[1];

            if (path.startsWith("v")) {
                path = path.substring(path.indexOf("/") + 1);
            }

            int dotIndex = path.lastIndexOf('.');
            if (dotIndex != -1) {
                path = path.substring(0, dotIndex);
            }

            return path;
        }
        catch (Exception e) {
            return null;
        }
    }
}
