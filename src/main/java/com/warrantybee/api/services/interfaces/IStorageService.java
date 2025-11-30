package com.warrantybee.api.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

/**
 * Defines the contract for storage-related operations such as uploading
 * and deleting files.
 */
public interface IStorageService {

    /**
     * Uploads a file to the underlying storage provider.
     *
     * @param file the multipart file to be uploaded; must not be null
     * @return a unique identifier, URL, or path referencing the uploaded file
     */
    String upload(MultipartFile file);

    /**
     * Deletes a file from the storage provider using its identifier.
     *
     * @param id the unique identifier or public ID of the file to delete
     * @return {@code true} if the file was successfully deleted;
     *         {@code false} otherwise
     */
    Boolean delete(String id);
}
