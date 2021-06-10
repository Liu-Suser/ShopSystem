package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface ProductMapper {
    int save(Product product);

    void updateInventory(List<Product> product);

    Optional<Product> findById(Long id);

    Long getProductCount();

    List<Product> findAll();

    List<Product> findProductsByIdList(List<Long> idList);

    List<Product> findAllByCategoryId(long categoryId);

    List<Product> findProductsByStatusIsTrue();

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByNameAndStatusIsTrue(String name);
}
