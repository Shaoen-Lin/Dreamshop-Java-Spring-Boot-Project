package com.Shawn.dream_shops.controller;

import com.Shawn.dream_shops.dto.ImageDto;
import com.Shawn.dream_shops.exceptions.ResourceNotFoundException;
import com.Shawn.dream_shops.model.Image;
import com.Shawn.dream_shops.reponse.ApiResponse;
import com.Shawn.dream_shops.service.image.IImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
//只產生 所有 final 欄位 和 @NonNull 欄位 的建構子
@RestController
@RequestMapping("${api.prefix}/images")
// = 去設定檔裡找 api.prefix = /api/v1
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productID)
    {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productID);
            return ResponseEntity.ok(new ApiResponse("Upload success!",imageDtos));
        }
        catch(Exception e)
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage() ) );
        }
    }
    // ApiResponse 通常是一個自訂的傳遞到 前端 的資料類別，ex以上就會回傳:
    // {
    //  "message": "Upload success!",
    //  "data": [
    //    {
    //      "imageID": 1,
    //      "imageName": "cat.jpg",
    //      "downloadURL": "/api/v1/images/image/download1"
    //    },
    //  ]
    //}

    @Transactional
    //@Transactional 是 Spring 框架提供的事務管理註解，幫你處理Blob的問題
    @GetMapping("image/download/{imageID}")
    public ResponseEntity<Resource> downloadImages(@PathVariable Long imageID) throws SQLException {
        Image image = imageService.getImageById(imageID);

        if (image.getImage() == null) {
            throw new ResourceNotFoundException("Image data is empty for id=" + imageID);
        } // 先測試是不是這個問題

        ByteArrayResource resource
                = new ByteArrayResource( image.getImage().getBytes(1,(int) image.getImage().length() ) );
        // 1 是說從 第一個 Bits 開始讀(因為JDBC 的 Blob 是從 1 開始，不是 0) + 長度

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);

        // 發 request / response 時，需要在 header 中設定 Content Type ，告訴對方說“ 嘿！ 我送給你的資料是這種類型
        // 需要依照 MIME 規範 (MIME 是一種標準，用來表示文件、檔案、各種位元組)
        // 1
        // discrete 單一家族：
        // ex. 主類別、子類別、參數 (type/subtype;parameter=value)
        // ex. application 適用多數 二進制資料 (binary data) ex. application/pdf
        // audio 適用音訊類資料 ex. audio/mpeg
        // font 適用各類文字型檔案 ex. font/ttf
        // image 適用各種圖片行檔案 ex. image/jpg
        // 2
        // multipart 家族：
        // 當你要一次送多種 MIME Type 就可以使用這家族的主類別
        // message 適用寄信時、大量文字情境 ex. message/partial
        // multipart 適用填表單情境 ex. multipart/form-data


        // 備註1：
        // return ResponseEntity.ok().body(data);
        // = return new ResponseEntity<>(data, HttpStatus.OK);

        // 備註2：
        // File 只能表示"本地"檔案系統的檔案， 但 Resource 可以表示各種資料來源 ex. byte array、網路上的資源、檔案 等
    }

    @PostMapping("/image/{imageID}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageID, @RequestPart MultipartFile file)
    {
        try
        {
            Image image = imageService.getImageById(imageID);
            if(image != null)
            {
                imageService.updateImage(file,imageID);
                return ResponseEntity.ok(new ApiResponse("Update Success!",null));
            }
            return null;
        }
        catch (ResourceNotFoundException e) // 因為有兩種 exception
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null ));
        }
        catch (Exception e) //另一種
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/image/{imageID}/update")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageID)
    {
        try
        {
            Image image = imageService.getImageById(imageID);
            if(image != null)
            {
                imageService.deleteImageById(imageID);
                return ResponseEntity.ok(new ApiResponse("Delete Success!",null));
            }
            return null;
        }
        catch (ResourceNotFoundException e) // 因為有兩種 exception
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null ));
        }
        catch (Exception e) //另一種
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

}
