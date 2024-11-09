package com.jakub.students.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobStorageException;
import com.jakub.students.exception.StudentError;
import com.jakub.students.exception.StudentException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ImageStorageClientImpl implements ImageStorageClient{

    private final BlobServiceClient blobServiceClient;

    public ImageStorageClientImpl(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public String uploadImage(String containerName, String originalImageName, InputStream data, long length) throws IOException {
        try {
            //get the BloblContainerClient object to interract with the container
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            //rename the image file to unique name
            String newImageName = UUID.randomUUID().toString()+ originalImageName.substring(originalImageName.lastIndexOf("."));
            // Get the BlobClient object to ineteract with the specified blob
            BlobClient blobClient = blobContainerClient.getBlobClient(newImageName);
            //Upload the image file to the blob
            blobClient.upload(data, length, true);
            // Return Url to our image
            return blobClient.getBlobUrl();
        }catch (BlobStorageException e){
            throw new StudentException(StudentError.FAILED_TO_UPLOAD_IMAGE);
        }
    }
}
