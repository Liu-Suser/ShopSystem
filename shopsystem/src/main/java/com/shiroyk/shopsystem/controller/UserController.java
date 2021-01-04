/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;


import com.shiroyk.shopsystem.component.OrderStatusSender;
import com.shiroyk.shopsystem.component.StatisticSender;
import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import com.shiroyk.shopsystem.entity.*;
import com.shiroyk.shopsystem.dto.response.OrderResponse;
import com.shiroyk.shopsystem.dto.response.CommonResponse;
import com.shiroyk.shopsystem.dto.response.UserNormal;
import com.shiroyk.shopsystem.dto.request.NewOrder;
import com.shiroyk.shopsystem.exception.BadRequestException;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final Integer PAGESIZE = 5;
    private final UserService userService;
    private final OrderTotalService orderTotalService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final AddressService addressService;
    private final CommentService commentService;
    private final CartService cartService;
    private final OrderStatusSender orderStatusSender;
    private final StatisticSender statisticSender;

    public UserController(UserService userService,
                          OrderTotalService orderTotalService,
                          OrderDetailService orderDetailService,
                          ProductService productService,
                          AddressService addressService,
                          CommentService commentService,
                          CartService cartService,
                          OrderStatusSender orderStatusSender,
                          StatisticSender statisticSender) {
        this.userService = userService;
        this.orderTotalService = orderTotalService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.addressService = addressService;
        this.commentService = commentService;
        this.cartService = cartService;
        this.orderStatusSender = orderStatusSender;
        this.statisticSender = statisticSender;
    }

    @PutMapping("/password")
    public CommonResponse<Object> updatePassword(String newPassword) {
        //角色修改自己的密码
        User user = userService.getCurrentUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        userService.save(user);
        return CommonResponse.create("修改密码成功！");
    }

    @GetMapping("/{userId}")
    public CommonResponse<User> getUserInfo(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(CommonResponse::create)
                .orElseThrow(() -> new NotFoundResourceException("未找到该用户！"));
    }

    @GetMapping("/info")
    public CommonResponse<UserNormal> getInfo() {
        return CommonResponse.create(new UserNormal(userService.getCurrentUser()));
    }

    @PutMapping("/info")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> updateInfo(String nickname,
                                             String phone,
                                             String question,
                                             String answer) {
        User user = userService.getCurrentUser();

        if (!StringUtils.isEmpty(nickname)) {
            user.setNickname(nickname);
        }
        if (!StringUtils.isEmpty(phone)) {
            user.setPhone(phone);
        }
        if (!StringUtils.isEmpty(question)) {
            user.setQuestion(question);
        }
        if (!StringUtils.isEmpty(answer)) {
            user.setAnswer(answer);
        }
        userService.save(user);
        return CommonResponse.create("修改信息成功！");
    }

    @GetMapping("/order/pageSize")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Long> getOrderPageSize() {
        User user = userService.getCurrentUser();
        return CommonResponse.create(orderTotalService.getOrderCountByUser(user));
    }

    @GetMapping("/order")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<List<OrderResponse>> getUserOrder(@RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        //普通用户获得自己的所有订单
        User user = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, PAGESIZE, Sort.by(Sort.Direction.DESC, "createTime"));
        return CommonResponse.create(orderTotalService.searchUserOrderAndDeleteFalse(pageable, user));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<OrderResponse> getOrder(@PathVariable Long orderId) {
        //普通用户获得自己的订单详情
        User user = userService.getCurrentUser();
        return orderTotalService.findOrderResponseById(orderId)
        .map(orderResponse -> {
            if (user.getId().equals(orderResponse.getUser().getId())) {
                return CommonResponse.create(orderResponse);
            }
            throw new NotFoundResourceException();
        }).orElseThrow(NotFoundResourceException::new);
    }

    @PostMapping("/order")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> createOrder(@RequestBody NewOrder newOrder) {
        //用户新建订单
        User user = userService.getCurrentUser();
        Optional<Address> address = addressService.findById(newOrder.getAid());
        if (!address.isPresent()) {
            throw new NotFoundResourceException("未找到该地址！");
        } else {
            if (!user.getId().equals(address.get().getUserId())) {
                throw new BadRequestException("地址错误！");
            }
        }


        BigDecimal price = new BigDecimal(0);
        List<OrderDetail> detailList = new ArrayList<>();

        //检查订单与计算价格
        for (Map<String, Long> o: newOrder.getDetails()) {
            Optional<Product> productOptional = productService.findById(o.get("pid"));

            if (!productOptional.isPresent()) {
                throw new NotFoundResourceException("商品不存在！");
            }

            Product product = productOptional.get();

            //检查是否已下架
            if (!product.getStatus()) {
                throw new BadRequestException(product.getName() + "商品已下架！");
            }

            //检查库存是否足够
            if (product.getStock() < o.get("quantity").intValue()) {
                throw new BadRequestException(product.getName() + "商品库存不足！");
            }

            //商品单价*数量
            price = price.add(product.getPrice().multiply(new BigDecimal(o.get("quantity").intValue())));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(product);
            orderDetail.setQuantity(o.get("quantity").intValue());
            detailList.add(orderDetail);
        }

        OrderTotal orderTotal = new OrderTotal();
        orderTotal.setAddressId(address.get());
        orderTotal.setPrice(price); //保存总价格
        orderTotal.setUserId(user);
        orderTotal.setStatus(OrderStatus.Ordered);

        orderTotalService.save(orderTotal);
        detailList.forEach(detail -> {
            //下单后减少商品库存
            Product product = detail.getProductId();
            product.minusStock(detail.getQuantity());
            productService.save(product);
            detail.setOrderId(orderTotal);
        });
        orderDetailService.save(detailList);

        //统计新订单
        statisticSender.sendMessage(StatisticEnum.newOrder, 5000);

        return CommonResponse.create("创建订单成功！");
    }

    @PutMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> updateOrder(@PathVariable Long orderId,
                                              OrderStatus status,
                                              String payMethod) {
        User user = userService.getCurrentUser();
        Optional<OrderTotal> totalOptional = orderTotalService.findById(orderId);
        if (!totalOptional.isPresent()) {
            throw new NotFoundResourceException("未找到该订单！");
        }

        OrderTotal orderTotal = totalOptional.get();

        String errorMsg = null;
        String successMsg = null;

        //用户只能操作自己的订单
        if (user.getId().equals(orderTotal.getUserId().getId())) {
            if (status == null) {
                throw new BadRequestException("更新订单失败！");
            }
            switch (status) {
                case Cancel:
                    //用户取消已下单订单
                    if (orderTotal.isOrdered() || orderTotal.isPayed()) {
                        orderTotal.setStatus(status);
                        if (orderTotal.isOrdered()) {
                            successMsg = "取消订单成功！";
                        } else {
                            successMsg = "取消订单成功，稍后系统将会自动退款！";
                        }

                        //取消订单后返还商品库存
                        orderStatusSender.sendMessage(orderTotal, 5000);

                        //统计取消订单
                        statisticSender.sendMessage(StatisticEnum.CancelOrder, 5000);

                    } else if (orderTotal.isShipped() || orderTotal.isTransit()) {
                        errorMsg = "订单已发货，不能取消订单，请联系客服！";
                    }
                    break;
                case Payed:
                    //用户支付已下单订单
                    if (orderTotal.isOrdered()) {
                        orderTotal.setStatus(status);
                        orderTotal.setPayMethod(payMethod);
                        orderTotal.setPaymentTime();
                        successMsg = "支付订单成功！";
                    } else {
                        errorMsg = "订单已支付，不能支付订单！";
                    }
                    break;
                case Completed:
                    //用户完成已发货订单
                    if (orderTotal.isShipped() || orderTotal.isTransit()) {
                        orderTotal.setStatus(status);

                        //增加商品销量
                        orderStatusSender.sendMessage(orderTotal, 5000);

                        //统计完成订单
                        statisticSender.sendMessage(StatisticEnum.CompleteOrder, 5000);

                        //增加用户积分, 计算方式为价格的1/100
                        user.inUserpoint(orderTotal.getPrice().intValue() / 100);
                        userService.save(user);

                        successMsg = "完成订单成功！";
                    } else {
                        errorMsg = "订单未发货或已收货，不能完成订单！";
                    }
                    break;
                case AfterSale:
                    if (orderTotal.isCompleted()) {
                        orderTotal.setStatus(status);
                        successMsg = "申请售后服务成功！";
                    } else {
                        errorMsg = "订单未完成，不能申请售后！";
                    }
                    break;
                default:
                    errorMsg = "更新订单失败！";
            }
        } else {
            errorMsg = "订单号错误，更新订单失败！";
        }

        orderTotal.setUpdateTime();
        orderTotalService.save(orderTotal);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        if (successMsg == null) {
            successMsg = "更新订单成功！";
        }
        return CommonResponse.create(successMsg);
    }

    @DeleteMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> deleteOrder(@PathVariable Long orderId) {
        //软删除用户订单
        User user = userService.getCurrentUser();
        return orderTotalService.findById(orderId).map(total -> {
            total.setDelete(true);
            orderTotalService.save(total);
            return CommonResponse.create("删除成功！");
        }).orElseThrow(() -> new NotFoundResourceException("未找到该订单！"));
    }

    @PostMapping("/order/{orderDetailId}/comment")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> commentOrder(@PathVariable Long orderDetailId,
                                               Integer rate,
                                               String comment) {
        //用户创建评论
        User user = userService.getCurrentUser();
        return orderDetailService.findById(orderDetailId).map(orderDetail -> {
            Comment cmt = orderDetail.getCommentId();
            String errorMsg;
            if (cmt != null) {
                errorMsg = "订单已评价，不能重复评价！";
            } else if (orderDetail.getOrderId().isCompleted()) {
                cmt = new Comment();
                cmt.setProductId(orderDetail.getProductId());
                cmt.setUserId(user);
                cmt.setRate(rate);
                cmt.setComment(comment);
                commentService.save(cmt);

                orderDetail.setCommentId(cmt);
                orderDetailService.save(orderDetail);
                return CommonResponse.create("评价成功！");
            } else {
                errorMsg = "订单未完成，评价失败！";
            }
            throw new BadRequestException(errorMsg);
        }).orElseThrow(() -> new NotFoundResourceException("未找到该订单！"));
    }

    @GetMapping("/address")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<List<Address>> getUserAddress() {
        return CommonResponse.create(addressService.findAllByUserIdAndNotDelete(userService.getCurrentUser()));
    }

    @GetMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Address> getUserAddressById(@PathVariable Long addressId) {
        return addressService.findById(addressId)
                .map(CommonResponse::create)
                .orElseThrow(NotFoundResourceException::new);
    }

    @GetMapping("/address/default")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Address> getUserDefaultAddress() {
        return addressService.findAddressByUserDefault(userService.getCurrentUser())
                .map(CommonResponse::create)
                .orElseThrow(NotFoundResourceException::new);
    }

    @PostMapping("/address")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> createUserAddress(String name,
                                                    String phone,
                                                    String address,
                                                    Boolean isDefault) {
        User user = userService.getCurrentUser();
        Address add = new Address();
        add.setUserId(user);
        saveAddress(name, phone, address, isDefault, user, add);

        return CommonResponse.create("创建地址成功！");
    }

    @PutMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> updateUserAddress(@PathVariable Long addressId,
                                                    String name,
                                                    String phone,
                                                    String address,
                                                    Boolean isDefault) {
        User user = userService.getCurrentUser();
        return addressService.findById(addressId).map(add -> {
            if (!user.getId().equals(add.getUserId())) {
                throw new AccessDeniedException("修改地址失败！");
            } else {
                saveAddress(name, phone, address, isDefault, user, add);
                return CommonResponse.create("修改地址成功！");
            }
        }).orElseThrow(() -> new NotFoundResourceException("地址不存在，修改地址失败！"));
    }

    private void saveAddress(String name,
                             String phone,
                             String address,
                             Boolean isDefault,
                             User user,
                             Address add) {
        add.setName(name);
        add.setPhone(phone);
        add.setAddress(address);
        add.setDefault(false);
        add.setUpdateTime();
        if (isDefault != null) {
            if (isDefault) {
                addressService.findAddressByUserDefault(user).ifPresent(oldDefault -> {
                    oldDefault.setDefault(false);
                    addressService.save(oldDefault);
                });
                add.setDefault(true);
            }
        }
        addressService.save(add);
    }

    @DeleteMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> deleteUserAddress(@PathVariable Long addressId) {
        User user = userService.getCurrentUser();
        return addressService.findById(addressId)
                .map(address -> {
                    if (!user.getId().equals(address.getUserId())) {
                        throw new AccessDeniedException("删除地址失败！");
                    } else {
                        address.setDefault(false);
                        address.setDelete(true);
                        addressService.save(address);
                        return CommonResponse.create("删除地址成功！");
                    }
                }).orElseThrow(() -> new NotFoundResourceException("地址不存在，删除地址失败！"));
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<List<Cart>> getUserCart() {
        return CommonResponse.create(cartService.findCartByUserId(userService.getCurrentUser()));
    }

    @PostMapping("/cart")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> addUserCart(Long productId,
                                              Integer quantity,
                                              Boolean checked) {
        User user = userService.getCurrentUser();
        return productService.findById(productId).map(product -> {
            if (!product.getStatus()) {
                throw new BadRequestException("商品已下架！");
            }
            Cart cart = new Cart();
            cart.setProductId(product);
            cart.setQuantity(quantity);
            cart.setUserId(user);
            cart.setChecked(checked);
            cartService.save(cart);
            return CommonResponse.create("添加到购物车成功！");
        }).orElseThrow(() -> new NotFoundResourceException("未找到该商品！"));

    }

    @PutMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('NORMAL')")
    public CommonResponse<Object> updateUserCart(@PathVariable Long cartId,
                                                 Integer quantity,
                                                 Boolean checked,
                                                 Boolean isDelete) {
        User user = userService.getCurrentUser();
        return cartService.findById(cartId).map(cart -> {
            if (cart.getUserId().getId().equals(user.getId())) {
                String msg = "更新购物车成功！";
                if (isDelete) {
                    cartService.delete(cartId);
                    msg = "删除成功！";
                } else {
                    cart.setQuantity(quantity);
                    cart.setUserId(user);
                    cart.setChecked(checked);
                    cartService.save(cart);
                }
                return CommonResponse.create(msg);
            }
            throw new BadRequestException("更新购物车失败！");
        }).orElseThrow(() -> new NotFoundResourceException("更新购物车失败！"));
    }
}
