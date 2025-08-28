package com.Shawn.dream_shops.service.category;

import com.Shawn.dream_shops.exceptions.AlreadyExistsException;
import com.Shawn.dream_shops.exceptions.ResourceNotFoundException;
import com.Shawn.dream_shops.model.Category;
import com.Shawn.dream_shops.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    @Autowired
    CategoryRepository cateRepo;

    @Override
    public Category getCategoryById(Long id) {
        return cateRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        // 看到 find() 就已經是 Optional 物件了，不用再用Optional開頭
        // 記得要搭配 orElse or orElseThrow
        // orElse(value)回傳你指定的預設值; orElseThrow()丟出你指定的例外
    }

    @Override
    public Category getCategoryByName(String name) {
        return cateRepo.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return cateRepo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !cateRepo.existsByName( c.getName() ) )
                // 取符合後面那段話的 c，若我們又一個 list 那 c 就會是其中的每個元素
                // 但我們丟入的 c 只有一個，所以只有是否之分
                .map(c -> cateRepo.save(c)) // 如果上面成功(有剩東西) 也就是 新的category 則 map
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + "already exists"));

        // 備註 1：
        // Optional.of(category) → 把 category 包裝成一個 Optional 物件，且值不能為 null

        //備註 2：
        //最後的 .orElseThrow(...) 會從 Optional 中「取出實際值」，或在沒有值的情況下拋出例外。
        // 所以必定執行 .orElseThrow()
        //取出它的回傳型別就是原本包在 Optional 裡的型別，也就是 Category。
    }
    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)) // 先變成 optional 物件再做後面
                .map(oldCategory ->
                {   // If found 就做 map
                    oldCategory.setName( category.getName() );
                    return cateRepo.save(oldCategory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        // Optional.ofNullable(getCategoryById(id)) → 把 category 包裝成一個 Optional 物件，且值能為 null
    }

    @Override
    public void deleteCategoryById(Long id) {
        cateRepo.findById(id)
                .ifPresentOrElse(cateRepo::delete
                        ,() -> { throw new ResourceNotFoundException("Category not found!");}
                );
                // 有找到就是做左邊的，沒做到就是跑右邊的
    }
}
