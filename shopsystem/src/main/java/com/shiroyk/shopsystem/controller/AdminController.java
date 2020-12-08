/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.constant.UserRole;
import com.shiroyk.shopsystem.entity.*;
import com.shiroyk.shopsystem.entity.getEntity.OrderMsg;
import com.shiroyk.shopsystem.entity.getEntity.ResponseMsg;
import com.shiroyk.shopsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final Integer PAGESIZE = 5;
    private final UserService userService;
    private final OrderTotalService orderTotalService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final AdvertService advertService;
    private final FileService fileService;
    private final StatisticService statisticService;

    public AdminController(UserService userService,
                           OrderTotalService orderTotalService,
                           OrderDetailService orderDetailService,
                           ProductService productService,
                           CommentService commentService,
                           CategoryService categoryService,
                           AdvertService advertService,
                           FileService fileService,
                           StatisticService statisticService) {
        this.userService = userService;
        this.orderTotalService = orderTotalService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.advertService = advertService;
        this.fileService = fileService;
        this.statisticService = statisticService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUser() {
        return userService.findAll();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> searchUser(@RequestParam("username") String username) {
        return userService.searchUserByUsername(username);
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMsg> createUser(String username,
                                                  String password,
                                                  String nickname,
                                                  String phone,
                                                  String question,
                                                  String answer,
                                                  Integer role) {
        //管理员创建用户
        User user = userService.findUserByUsername(username);
        if (user != null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "用户名已存在！"));
        } else {
            user = new User();
            if (StringUtils.isEmpty(password)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "请输入密码！"));
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            return saveUser(nickname, phone, question, answer, role, user);
        }
    }

    @PutMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMsg> updateUser(@PathVariable String username,
                                                  String nickname,
                                                  String password,
                                                  String phone,
                                                  String question,
                                                  String answer,
                                                  Integer role) {
        //管理员修改用户
        User user = userService.findUserByUsername(username);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMsg(HttpStatus.NOT_FOUND, "用户不存在！"));
        if (password != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
        }
        return saveUser(nickname, phone, question, answer, role, user);
    }

    private ResponseEntity<ResponseMsg> saveUser(String nickname,
                                                 String phone,
                                                 String question,
                                                 String answer,
                                                 Integer role,
                                                 User user) {
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setQuestion(question);
        user.setAnswer(answer);
        user.setRole(UserRole.values()[role]);
        userService.save(user);
        return ResponseEntity.ok()
                .body(new ResponseMsg(HttpStatus.OK, "保存用户成功！"));
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMsg> deleteUser(@PathVariable String username) {
        //管理员删除用户
        User user = userService.findUserByUsername(username);
        userService.delete(user);
        return ResponseEntity.ok()
                .body(new ResponseMsg(HttpStatus.OK, "删除用户成功！"));
    }

    @GetMapping("/order")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE','WAREHOUSE')")
    public List<OrderMsg> getAllOrder(@RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        //管理员客服获得所有订单，仓库管理员获得已支付订单
        User user = userService.getCurrentUser();
        List<OrderMsg> msgList = null;
        Pageable pageable = PageRequest.of(page, PAGESIZE, Sort.by("createTime").descending());
        switch (user.getRoleName()) {
            case Admin:
            case Service:
                msgList = orderTotalService.findAllOrder(pageable);
                break;
            case Warehouse:
                msgList = orderTotalService.findOrdersWarehouse(pageable);
                break;
        }
        return msgList;
    }

    @GetMapping("/order/pageSize")
    public Long getOrderPageSize() {
        switch (userService.getCurrentUser().getRoleName()) {
            case Admin:
            case Service:
                return orderTotalService.getOrderCount();
            case Warehouse:
                return orderTotalService.getOrderCountByWarehouse();
            default:
                return 0L;
        }
    }

    @GetMapping("/order/search")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE','WAREHOUSE')")
    public List<OrderMsg> searchUserOrder(@RequestParam(required = false, defaultValue = "0", value="page") Integer page,
                                                          @RequestParam("username") String username) {
        //管理员客服搜索某用户订单详情，仓库管理员搜索某用户已支付订单
        User user = userService.getCurrentUser();
        User normalUser = userService.findUserByUsername(username);
        List<OrderMsg> msgList = null;
        Pageable pageable = PageRequest.of(page, PAGESIZE, Sort.by("createTime").descending());
        switch (user.getRoleName()) {
            case Admin:
            case Service:
                msgList = orderTotalService.searchUserOrder(pageable, normalUser);
                break;
            case Warehouse:
                msgList = orderTotalService.searchUserOrderMsgByWarehouse(pageable, normalUser);
                break;
        }
        return msgList;
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE','WAREHOUSE')")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        User user = userService.getCurrentUser();
        return orderTotalService.findOrderMsgById(orderId)
                .map(orderMsg -> {
                    if (UserRole.Warehouse.equals(user.getRole())) {
                        if (OrderStatus.Payed.equals(orderMsg.getStatus())
                            || OrderStatus.Shipped.equals(orderMsg.getStatus())) {
                            return ResponseEntity.ok().body(orderMsg);
                        } else {
                            return ResponseEntity.notFound().build();
                        }
                    }
                    return ResponseEntity.ok().body(orderMsg);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE','WAREHOUSE')")
    public ResponseEntity<ResponseMsg> updateUserOrder(@PathVariable Long orderId,
                                                       OrderStatus status,
                                                       BigDecimal price,
                                                       String payMethod,
                                                       String express,
                                                       String logistics,
                                                       Boolean isDelete
                                                         ) {
        User user = userService.getCurrentUser();
        logger.warn("User role: "+user.getRoleAuthority().toString());

        return orderTotalService.findById(orderId)
                .map(orderTotal -> {
                    String successMsg = null;
                    String failMsg = null;
                    switch (user.getRoleName()) {
                        case Admin:
                            orderTotal.setPrice(price);
                            orderTotal.setStatus(status);
                            orderTotal.setExpress(express);
                            orderTotal.setLogistics(logistics);
                            orderTotal.setPayMethod(payMethod);
                            orderTotal.setDelete(isDelete);
                            break;
                        case Service:
                            orderTotal.setStatus(status);
                            orderTotal.setExpress(express);
                            orderTotal.setLogistics(logistics);
                            orderTotal.setPayMethod(payMethod);
                            orderTotal.setDelete(isDelete);
                            break;
                        case Warehouse:
                            //仓库管理员发货已支付订单
                            if (orderTotal.isPayed() || orderTotal.isShipped()) {
                                orderTotal.setStatus(OrderStatus.Shipped);
                                orderTotal.setExpress(express);
                                orderTotal.setLogistics(logistics);
                                successMsg = "更新订单物流成功！";
                            } else {
                                failMsg = "订单未支付或已出库，不能修改订单！";
                            }
                    }
                    orderTotal.setUpdateTime();
                    orderTotalService.save(orderTotal);
                    if (successMsg == null) {
                        successMsg = "更新订单成功！";
                    }
                    if (failMsg != null) {
                        return ResponseEntity.badRequest()
                                .body(new ResponseMsg(HttpStatus.BAD_REQUEST, failMsg));
                    }
                    return ResponseEntity.ok()
                            .body(new ResponseMsg(HttpStatus.OK, successMsg));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMsg(HttpStatus.NOT_FOUND, "订单不存在！")));

    }

    @PutMapping("/order/{orderDetailId}/comment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMsg> updateUserComment(@PathVariable Long orderDetailId,
                                                         Long userId,
                                                         Integer rate,
                                                         String comment) {
        //管理员更改用户评论
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMsg(HttpStatus.NOT_FOUND, "未找到该用户！"));
        }
        User user = optionalUser.get();
        return orderDetailService.findById(orderDetailId)
                .map(orderDetail -> {
                    Comment cmt = orderDetail.getCommentId();
                    if (cmt != null) {
                        cmt.setProductId(orderDetail.getProductId());
                        cmt.setUserId(user);
                        cmt.setRate(rate);
                        cmt.setComment(comment);
                        commentService.save(cmt);

                        orderDetail.setCommentId(cmt);
                        orderDetailService.save(orderDetail);
                        return ResponseEntity.ok()
                                .body(new ResponseMsg(HttpStatus.OK, "更新评价成功！"));
                    } else {
                        return ResponseEntity.badRequest()
                                .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "订单未完成，创建评价失败！"));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMsg(HttpStatus.NOT_FOUND, "未找到该订单！")));
    }

    @PostMapping("/category")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> createCategory(String name,
                                                      Long parentId) {
        return categoryService.findCategoryByName(name)
                .map(category ->
                    ResponseEntity.badRequest().body(
                    new ResponseMsg(HttpStatus.BAD_REQUEST, "分类已存在！")))
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    category.setStatus(true);
                    category.setParentId(parentId);
                    categoryService.save(category);

                    return ResponseEntity.ok()
                            .body(new ResponseMsg(HttpStatus.OK, "创建分类成功！"));
                });
    }

    @PutMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> updateCategory(@PathVariable Long categoryId,
                                                      String name,
                                                      Boolean status,
                                                      Long parentId) {
        return categoryService.findById(categoryId).map(category -> {
            category.setName(name);
            category.setStatus(status);
            category.setParentId(parentId);
            categoryService.save(category);
            return ResponseEntity.ok()
                    .body(new ResponseMsg(HttpStatus.OK, "更新分类成功！"));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseMsg(HttpStatus.NOT_FOUND, "分类不存在！")));
    }

    @PostMapping("/product")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> createProduct(Long categoryId,
                                                     String name,
                                                     String subtitle,
                                                     String image,
                                                     String detail,
                                                     BigDecimal price,
                                                     Integer stock,
                                                     Boolean status) {
        if (productService.findProductByName(name).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "商品已存在！"));
        } else {
            return categoryService.findById(categoryId).map(category -> {
                Product product = new Product();
                return saveProduct(name, subtitle,
                                    image, detail,
                                    price, stock,
                                    status, product, category);
            }).orElseGet(() -> ResponseEntity.badRequest()
                    .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "新建商品失败！")));
        }
    }

    @PutMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> updateProduct(@PathVariable Long productId,
                                                     Long categoryId,
                                                     String name,
                                                     String subtitle,
                                                     String image,
                                                     String detail,
                                                     BigDecimal price,
                                                     Integer stock,
                                                     Boolean status) {
        return productService.findById(productId).map(
                product ->
                        categoryService.findById(categoryId).map(
                                category ->
                                        saveProduct(name, subtitle,
                                                    image, detail,
                                                    price, stock,
                                                    status, product, category))
                                .orElseGet(() -> ResponseEntity.badRequest()
                                .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "分类不存在！")))
                ).orElseGet(() -> ResponseEntity.badRequest()
                .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "商品不存在！")));
    }

    private ResponseEntity<ResponseMsg> saveProduct(String name,
                                                    String subtitle,
                                                    String image,
                                                    String detail,
                                                    BigDecimal price,
                                                    Integer stock,
                                                    Boolean status,
                                                    Product product,
                                                    Category category) {
        product.setCategoryId(category);
        product.setName(name);
        product.setSubtitle(subtitle);
        product.setImage(image);
        product.setDetail(detail);
        product.setPrice(price);
        product.setStock(stock);
        product.setStatus(status);
        productService.save(product);
        return ResponseEntity.ok()
                .body(new ResponseMsg(HttpStatus.OK, "保存商品成功！"));
    }

    @PostMapping("/product/uploadImage")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> uploadImages(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            return ResponseEntity.badRequest().body(new ResponseMsg(HttpStatus.BAD_REQUEST,"文件为空！"));
        }

        String fileName = fileService.storeImage(file);
        if (StringUtils.isEmpty(fileName)) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMsg(HttpStatus.BAD_REQUEST,"上传失败，请检查文件！"));
        }
        String fileDownloadUri = "product/image/" + fileName;
        return ResponseEntity.ok().body(new ResponseMsg(HttpStatus.OK,fileDownloadUri));
    }

    @GetMapping("/statistic")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public List<Statistic> getStatistic(@RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by("timestamp").descending());
        return statisticService.findAll(pageable);
    }

    @PutMapping("/banner")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE')")
    public ResponseEntity<ResponseMsg> updateAdvert(String image) {
        Advert advert = advertService.getAdvert();
        if (image != null) {
            advert.setImage(image);
            advertService.save(advert);
        }
        return ResponseEntity.ok().body(new ResponseMsg(HttpStatus.OK,"保存首页Banner成功！"));
    }

}
