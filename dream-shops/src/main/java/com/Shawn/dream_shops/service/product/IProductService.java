package com.Shawn.dream_shops.service.product;

import com.Shawn.dream_shops.dto.ProductDto;
import com.Shawn.dream_shops.model.Product;
import com.Shawn.dream_shops.request.AddProductRequest;
import com.Shawn.dream_shops.request.ProductUpdateRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
