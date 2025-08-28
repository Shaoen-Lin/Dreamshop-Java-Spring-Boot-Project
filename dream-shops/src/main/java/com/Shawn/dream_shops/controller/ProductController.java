package com.Shawn.dream_shops.controller;


import com.Shawn.dream_shops.dto.ProductDto;
import com.Shawn.dream_shops.exceptions.AlreadyExistsException;
import com.Shawn.dream_shops.exceptions.ProductNotFoundException;
import com.Shawn.dream_shops.exceptions.ResourceNotFoundException;
import com.Shawn.dream_shops.model.Product;
import com.Shawn.dream_shops.reponse.ApiResponse;
import com.Shawn.dream_shops.request.AddProductRequest;
import com.Shawn.dream_shops.request.ProductUpdateRequest;
import com.Shawn.dream_shops.service.product.IProductService;
import com.Shawn.dream_shops.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
//只產生 所有 final 欄位 和 @NonNull 欄位 的建構子
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts()
    {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        // 不然有 product Image 會噴錯
        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id)
    {
        try
        {
            Product product = productService.getProductById(id);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product)
    {
        try
        {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
        }
        catch(AlreadyExistsException e)
        {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long id)
    {
        try
        {
            Product theProduct = productService.updateProduct(request, id);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Update product success!", productDto));
        }
        catch (ProductNotFoundException e)
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id)
    {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Delete product success!", id));
        }
        catch (ResourceNotFoundException e)
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName)
    {
        try
        {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found",null));
            }
            // 因為 Service 中沒有寫任何的 Exception 所以改成 if 來抓錯

            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            return ResponseEntity.ok(new ApiResponse("success!", convertedProducts));
        }
        catch (Exception e) // or 出現其他錯誤 => INTERNAL_SERVER_ERROR
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("No product found",null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand)
    {
        try
        {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found",null));
            }
            // 因為 Service 中沒有寫任何的 Exception 所以改成 if 來抓錯

            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            return ResponseEntity.ok(new ApiResponse("success!", convertedProducts));
        }
        catch (Exception e) // or 出現其他錯誤 => INTERNAL_SERVER_ERROR
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("No product found",null));
        }
    }

    @GetMapping("/product/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name)
    {
        try
        {
            List<Product> products = productService.getProductsByName(name);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found",null));
            }
            // 因為 Service 中沒有寫任何的 Exception 所以改成 if 來抓錯

            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            return ResponseEntity.ok(new ApiResponse("success!", convertedProducts));
        }
        catch (Exception e) // or 出現其他錯誤 => INTERNAL_SERVER_ERROR
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("No product found",null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand)
    {
        try
        {
            List<Product> products = productService.getProductsByBrand(brand);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found",null));
            }
            // 因為 Service 中沒有寫任何的 Exception 所以改成 if 來抓錯

            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            return ResponseEntity.ok(new ApiResponse("success!", convertedProducts));
        }
        catch (Exception e) // or 出現其他錯誤 => INTERNAL_SERVER_ERROR
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("No product found",null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category)
    {
        try
        {
            List<Product> products = productService.getProductsByCategory(category);

            if(products.isEmpty())
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found",null));
            }
            // 因為 Service 中沒有寫任何的 Exception 所以改成 if 來抓錯

            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            return ResponseEntity.ok(new ApiResponse("success!", convertedProducts));
        }
        catch (Exception e) // or 出現其他錯誤 => INTERNAL_SERVER_ERROR
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("No product found",null));
        }
    }

    @GetMapping("/products/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsBrandAndName(@RequestParam String brand, @RequestParam String name)
    {
        try
        {
            var productCount = productService.countProductsBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
