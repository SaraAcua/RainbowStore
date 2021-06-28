/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Sara.TiendaArcoiris.Controller;

import com.Sara.TiendaArcoiris.Entity.ImageProduct;
import com.Sara.TiendaArcoiris.Repository.ImageProductRepository;
import com.Sara.TiendaArcoiris.Repository.ProductRepository;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

/**
 *
 * @author SARA
 */
@Controller
public class ImageProductController {

    @Autowired
    private ImageProductRepository imageProductRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AmazonS3 s3client;

    @Value("${gkz.s3.bucket}")
    private String bucketName;

    @PostMapping("/product/image/upload")
    @ResponseBody
    public ResponseEntity productImageUpload(@RequestParam("product_id") int product_id, @RequestParam("uploadfile") MultipartFile file) {
        Map<String, Object> resp = new HashMap<>();

        try {
            String keyname = UUID.randomUUID().toString();
            int response = uploadFile(keyname, file);
            if (response == 1) {

                ImageProduct image = new ImageProduct();
                image.setKeyname(keyname);
                image.setContentType(file.getContentType());
                image.setSize((int) file.getSize());
                image.setProduct(productRepository.findById(product_id).get());
                imageProductRepository.save(image);
                resp.put("status", "success");
            } else {
                resp.put("status", "error");
            }
        } catch (Exception ex) {
            resp.put("status", "error");
        }
        return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.OK);
    }

    @RequestMapping("/product/image/show/{id}")
    public ResponseEntity<InputStreamResource> DownloadProjectDocument(@PathVariable("id") String id) {

        ImageProduct image = imageProductRepository.findById(id).get();

        if (image != null) {

            S3Object s3object = s3client.getObject(bucketName, id);
            S3ObjectInputStream inputStream = s3object.getObjectContent();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(s3object.getObjectMetadata().getContentType()))
                    .body(new InputStreamResource(inputStream));
        }

        return null;
    }

    public int uploadFile(String keyName, MultipartFile file) {
        int response = 0;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            s3client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));
            response = 1;
        } catch (AmazonServiceException ex) {
            response = 0;
            throw ex;
        } catch (AmazonClientException ex) {
            response = 0;
            throw ex;
        } catch (IOException ex) {
            response = 0;
        }
        return response;
    }

    public int deleteFile(String keyName) {
        int response = 0;
        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            response = 1;
        } catch (AmazonServiceException ex) {
            response = 0;
        } catch (AmazonClientException ex) {
            response = 0;
        }
        return response;
    }

    public ByteArrayOutputStream downloadFile(String keyName) {
        try {

            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }

            return baos;
        } catch (Exception ex) {
            return null;
        }
    }

    public int DeleteObject(String keyName) {
        int resp = 0;
        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            resp = 1;
        } catch (SdkClientException ex) {
            resp = 0;
        }
        return resp;
    }

}
