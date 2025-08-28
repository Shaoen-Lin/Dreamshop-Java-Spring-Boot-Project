package com.Shawn.dream_shops.service.product;

import com.Shawn.dream_shops.dto.ImageDto;
import com.Shawn.dream_shops.dto.ProductDto;
import com.Shawn.dream_shops.exceptions.AlreadyExistsException;
import com.Shawn.dream_shops.exceptions.ProductNotFoundException;
import com.Shawn.dream_shops.model.Category;
import com.Shawn.dream_shops.model.Image;
import com.Shawn.dream_shops.model.Product;
import com.Shawn.dream_shops.repository.CategoryRepository;
import com.Shawn.dream_shops.repository.ImageRepository;
import com.Shawn.dream_shops.request.AddProductRequest;
import com.Shawn.dream_shops.repository.ProductRepository;
import com.Shawn.dream_shops.request.ProductUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ProductService implements IProductService{

    @Autowired
    private  ProductRepository prodRepo;
    @Autowired
    private CategoryRepository cateRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private ModelMapper modelMapper;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Product addProduct(AddProductRequest request) {

        if( productExists(request.getName(), request.getBrand()) ){
            throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " already exits, you may update this product instead");
        }

        Category category = Optional.ofNullable(cateRepo
                .findByName( request.getCategory().getName() ))
                .orElseGet(() -> {
                    Category newCategory = new Category( request.getCategory().getName() );
                    // 這裡相當於
                    // Category category = request.getCategory();
                    // String name = category.getName();
                    // 最後再丟到 Category Constructor

                    return cateRepo.save(newCategory);
                });

        request.setCategory( category );
        return prodRepo.save( createProduct(request,category) );
        // 注意：這裡不用 request 的 category 而是抓新的 category 用

        // 如果有此類別，則直接加入 Product
        // 若沒有，則新增一個類別，再加入 Product
    }

    private boolean productExists(String name , String brand)
    {
        return prodRepo.existsByNameAndBrand(name, brand);
    }


    // 最好要提供一個頁面(AddProductRequest)，來負責吃掉繳交到資料庫的資料
    // DTO (Data Transfer Object)  資料傳輸對象
    private Product createProduct(AddProductRequest request, Category category)
    {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
        // Product 必須要自定義建構子，因為沒有 id 跟 image
    }

    @Override
    public Product getProductById(Long id) {
        return prodRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        // Lamda Expression
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteProductById(Long id) {
        prodRepo.findById(id)
                .ifPresentOrElse(prodRepo::delete,
                        () -> {throw new ProductNotFoundException("Product Not Found！");});
        // 如果存在，則呼叫 repo::id = repo.delete(id)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId)
    {
        return prodRepo
                .findById(productId) //回傳的是 Optional<Product>，可能會有值 (找到產品) 或空的 (找不到)。
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                // 對於所有existingProduct(這個名子可以隨便訂)，如果找到 Product，就用 map() 做轉換。
                .map(product -> prodRepo.save(product))
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request)
    {
        // 放入 其他資訊
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        // 放入 Category
        Category category = cateRepo.findByName( request.getCategory().getName() );
        // 因為只是更新 所以Category必定存在，所以不用驗證是否有新的Category
        // 直接取即可
        existingProduct.setCategory(category);

        // 最後 return
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return prodRepo.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return prodRepo.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return prodRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return prodRepo.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return prodRepo.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return prodRepo.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsBrandAndName(String brand, String name) {
        return prodRepo.countByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products)
    {
        return products.stream()
                .map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product)
    {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepo.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
