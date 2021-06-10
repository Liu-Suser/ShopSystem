package com.shiroyk.shopsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.mapper.ProductMapper;
import com.shiroyk.shopsystem.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void save(Product product) {
        productMapper.save(product);
    }

    @Override
    public void updateInventory(List<Product> product) {
        productMapper.updateInventory(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productMapper.findById(id);
    }

    @Override
    public Long getProductCount() {
        return productMapper.getProductCount();
    }

    @Override
    public List<Product> findAll(int num, int size) {
        PageHelper.startPage(num, size);
        return productMapper.findAll();
    }

    @Override
    public Map<Long, Product> findProductInventoryMapByIdList(List<Long> idList) {
        return productMapper.findProductsByIdList(idList).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    @Override
    public List<Product> findAllByCategoryId(long categoryId, int num, int size) {
        PageHelper.startPage(num, size);
        return productMapper.findAllByCategoryId(categoryId);
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return productMapper.findProductByName(name);
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productMapper.findProductsByNameAndStatusIsTrue(name);
    }

}
