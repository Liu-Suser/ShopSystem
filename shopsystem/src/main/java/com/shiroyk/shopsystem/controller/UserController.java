/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;


import com.shiroyk.shopsystem.component.OrderStatusSender;
import com.shiroyk.shopsystem.component.StatisticSender;
import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import com.shiroyk.shopsystem.dto.request.NewOrder;
import com.shiroyk.shopsystem.dto.request.UserComment;
import com.shiroyk.shopsystem.dto.request.UserInfo;
import com.shiroyk.shopsystem.dto.response.APIResponse;
import com.shiroyk.shopsystem.dto.response.AddressDto;
import com.shiroyk.shopsystem.dto.response.OrderInfoDto;
import com.shiroyk.shopsystem.dto.response.UserDto;
import com.shiroyk.shopsystem.entity.*;
import com.shiroyk.shopsystem.exception.BadRequestException;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
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
    
    /** 
    * @Description: 修改密码
    * @Param: [newPassword] 
    * @return: 成功或失败消息
    */ 
    @PutMapping("/password")
    public APIResponse<Object> updatePassword(String newPassword) {
        //角色修改自己的密码
        return userService.getCurrentUser().map(user -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));
            userService.save(user);
            return APIResponse.ok("修改密码成功！");
        }).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取用户信息
    * @Param: [uid]
    * @return: 用户信息响应
    */
    @GetMapping("/{uid}")
    public APIResponse<UserDto> getUserInfoById(@PathVariable Long uid) {
        return userService.findById(uid)
                .map(user -> APIResponse.ok(user.toUserDtoS()))
                .orElseThrow(() -> new NotFoundResourceException("未找到该用户！"));
    }
    
    /** 
    * @Description: 获取当前用户信息
    * @return: 用户信息响应
    */ 
    @GetMapping()
    public APIResponse<UserDto> getUserInfo() {
        return userService.getCurrentUser()
                .map(user -> APIResponse.ok(user.toUserDtoM()))
                .orElse(APIResponse.badRequest("无权访问！"));
    }
    
    /** 
    * @Description: 更新用户信息
    * @Param: [userInfo] 
    * @return: 成功或失败消息
    */ 
    @PutMapping()
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<?> updateInfo(@Valid @RequestBody UserInfo userInfo) {
        return userService.getCurrentUser().map(user -> {
            user.setNickname(userInfo.getNickname());
            user.setPhone(userInfo.getPhone());
            user.setQuestion(userInfo.getQuestion());
            user.setAnswer(userInfo.getAnswer());
            userService.save(user);
            return APIResponse.ok("修改信息成功！");
        }).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取当前用户订单数量
    * @return: 订单数量
    */
    @GetMapping("/order/count")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Long> getOrderPageSize() {
        return userService.getCurrentUser()
                .map(user -> APIResponse.ok(orderTotalService.getOrderCountByUser(user.getId())))
                .orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取当前用户的所有订单
    * @Param: [page, size]
    * @return: 订单列表
    */
    @GetMapping("/order")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<?> getUserOrder(@RequestParam(required = false, defaultValue = "0", value="p") Integer page,
                                       @RequestParam(required = false, defaultValue = "10", value="s") Integer size) {
        //普通用户获得自己的所有订单
        return userService.getCurrentUser()
                .map(user -> APIResponse.ok(orderTotalService
                        .findUserOrderAndDeleteFalse(user.getId(), page, size)
                        .stream().map(OrderTotal::toOrderInfoDtoS).collect(Collectors.toList())))
                .orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取订单
    * @Param: [orderId]
    * @return: 订单信息
    */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<OrderInfoDto> getOrder(@PathVariable Long orderId) {
        //普通用户获得自己的订单详情
        return userService.getCurrentUser()
        .map(user ->
            orderTotalService.findInfoById(orderId)
            .map(orderTotal -> {
                if (user.getId().equals(orderTotal.getUserId())) {
                    return APIResponse.ok(orderTotal.toOrderInfoDto(orderDetailService.
                            findOrderDetailAndProductByOrderId(orderTotal.getId())));
                }
                throw new NotFoundResourceException("无法找到该订单！");
            }).orElseThrow(() -> new NotFoundResourceException("无法找到该订单！")))
        .orElse(APIResponse.badRequest("无权访问！"));
    }
    
    /** 
    * @Description: 创建新订单
    * @Param: [newOrder] 
    * @return: 成功或失败消息
    */ 
    @PostMapping("/order")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> createOrder(@RequestBody NewOrder newOrder) {
        //用户新建订单
        Optional<User> currentUser = userService.getCurrentUser();
        User user;
        if (currentUser.isPresent())
            user = currentUser.get();
        else
            return APIResponse.badRequest("无权访问！");

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
        for (NewOrder.Detail detail: newOrder.getDetails()) {
            Optional<Product> productOptional = productService.findById(detail.getPid());

            if (!productOptional.isPresent()) {
                throw new NotFoundResourceException("商品不存在！");
            }

            Product product = productOptional.get();
            int quantity = detail.getQuantity().intValue();

            //检查是否已下架
            if (!product.getStatus()) {
                throw new BadRequestException(product.getName() + "商品已下架！");
            }

            //检查库存是否足够
            if (product.getInventory() < quantity) {
                throw new BadRequestException(product.getName() + "商品库存不足！");
            }

            //商品单价*数量
            price = price.add(product.getPrice().multiply(new BigDecimal(quantity)));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(product.getId());
            orderDetail.setQuantity(quantity);
            detailList.add(orderDetail);
        }

        OrderTotal orderTotal = new OrderTotal();
        orderTotal.setAddressId(address.get().getId());
        orderTotal.setPrice(price); //保存总价格
        orderTotal.setUserId(user.getId());
        orderTotal.setIsDelete(false);
        orderTotal.setStatus(OrderStatus.Ordered);

        orderTotalService.save(orderTotal);

        Map<Long, Product> productMap = productService
                .findProductInventoryMapByIdList(detailList.stream()
                        .map(OrderDetail::getProductId)
                        .collect(Collectors.toList()));

        detailList.forEach(detail -> {
            //下单后减少商品库存
            productMap.get(detail.getProductId()).reduceInventory(detail.getQuantity());
            detail.setOrderId(orderTotal.getId());
        });

        log.info(productMap.values().toString());

        productService.updateInventory(new ArrayList<>(productMap.values()));

        log.info(detailList.toString());

        orderDetailService.insertAll(detailList);

        //统计新订单
        statisticSender.sendMessage(StatisticEnum.newOrder, 5000);

        return APIResponse.ok("创建订单成功！");
    }

    /**
    * @Description: 用户更新订单
    * @Param: [orderId, status, payMethod]
    * @return: 成功或失败消息
    */
    @PutMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> updateOrder(@PathVariable Long orderId,
                                           OrderStatus status,
                                           String payMethod) {

        Optional<User> currentUser = userService.getCurrentUser();

        if (!currentUser.isPresent())
            throw new AccessDeniedException("无权访问！");

        User user = currentUser.get();

        Optional<OrderTotal> totalOptional = orderTotalService.findById(orderId);
        if (!totalOptional.isPresent()) {
            throw new NotFoundResourceException("未找到该订单！");
        }

        OrderTotal orderTotal = totalOptional.get();

        String errorMsg = null;
        String successMsg = null;

        //用户只能操作自己的订单
        if (user.getId().equals(orderTotal.getUserId())) {
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
                        user.inUserpoint(orderTotal.getPrice().intValue());
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

        orderTotalService.save(orderTotal);

        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        if (successMsg == null) {
            successMsg = "更新订单成功！";
        }
        return APIResponse.ok(successMsg);
    }
    
    /** 
    * @Description: 用户删除订单
    * @Param: [orderId] 
    * @return: 成功或失败消息
    */ 
    @DeleteMapping("/order/{orderId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> deleteOrder(@PathVariable Long orderId) {
        //软删除用户订单
        return userService.getCurrentUser().map(user ->
                orderTotalService.findById(orderId).map(total -> {
                    if (total.isCompleted()) {
                        if (total.getIsDelete())
                            return APIResponse.badRequest("订单已在回收站中！");
                        total.setIsDelete(true);
                        orderTotalService.save(total);
                        return APIResponse.ok("删除成功！");
                    }
                    return APIResponse.badRequest("不能删除尚未完成的订单！");
                }).orElseThrow(() -> new NotFoundResourceException("未找到该订单！"))
            ).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 评价商品订单
    * @Param [orderDetailId, userComment]
    * @return 成功或失败消息
    */
    @PostMapping("/order/{orderId}/{orderDetailId}/comment")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> commentOrder(@PathVariable Long orderId,
                                            @PathVariable Long orderDetailId,
                                            @Valid UserComment userComment) {
        //用户创建评论
        return userService.getCurrentUser().map(user ->
            orderTotalService.findById(orderId).map(orderTotal ->
                orderDetailService.findById(orderDetailId).map(orderDetail -> {
                    if (orderDetail.getOrderId().equals(orderId) && orderTotal.isCompleted()) {
                        commentService.findByOrderId(orderDetail.getOrderId())
                            .map(cmt -> APIResponse.badRequest("订单已评价，不能重复评价！"))
                            .orElseGet(() -> {
                                Comment cmt = new Comment();
                                cmt.setProductId(orderDetail.getProductId());
                                cmt.setUserId(user.getId());
                                cmt.setRate(userComment.getRate());
                                cmt.setComment(userComment.getComment());
                                commentService.save(cmt);
                                orderDetail.setCommentId(cmt.getId());
                                orderDetailService.save(orderDetail);
                                return APIResponse.ok("评价成功！");
                            });
                    }
                    return APIResponse.badRequest("评价失败！");
                }).orElseThrow(() -> new NotFoundResourceException("未找到该订单！"))
            ).orElseThrow(() -> new NotFoundResourceException("未找到该订单！"))
        ).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取所有收货地址
    * @return: 收货地址列表
    */
    @GetMapping("/address")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<List<AddressDto>> getUserAddress() {
        return userService.getCurrentUser()
                .map(user -> APIResponse.ok(addressService
                        .findAllByUserIdAndNotDelete(user.getId())
                        .stream().map(Address::toAddressDtoS)
                        .collect(Collectors.toList())))
                .orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取单个收货地址
    * @Param: [addressId]
    * @return: 收货地址
    */
    @GetMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Address> getUserAddressById(@PathVariable Long addressId) {
        return addressService.findById(addressId)
                .map(APIResponse::ok)
                .orElseThrow(NotFoundResourceException::new);
    }

    /**
    * @Description: 获取默认收货地址
    * @return: 收货地址
    */
    @GetMapping("/address/default")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Address> getUserDefaultAddress() {
        return userService.getCurrentUser().map(user ->
                addressService.findAddressByUserIdAndIsDefault(user.getId())
                .map(APIResponse::ok)
                .orElseThrow(NotFoundResourceException::new)
        ).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 创建新收货地址
    * @Param: [name, phone, address, isDefault]
    * @return: 成功或失败消息
    */
    @PostMapping("/address")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> createUserAddress(String name,
                                                     String phone,
                                                     String address,
                                                     Boolean isDefault) {
        return userService.getCurrentUser().map(user -> {
            Address add = new Address();
            add.setUserId(user.getId());
            saveAddress(name, phone, address, isDefault, user, add);
            return APIResponse.ok("创建地址成功！");
        }).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 更新收货地址
    * @Param: [addressId, name, phone, address, isDefault]
    * @return: 成功或失败消息
    */
    @PutMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> updateUserAddress(@PathVariable Long addressId,
                                                     String name,
                                                     String phone,
                                                     String address,
                                                     Boolean isDefault) {
        return userService.getCurrentUser().map(user ->
            addressService.findById(addressId).map(add -> {
                if (!user.getId().equals(add.getUserId())) {
                    throw new AccessDeniedException("修改地址失败！");
                } else {
                    saveAddress(name, phone, address, isDefault, user, add);
                    return APIResponse.ok("修改地址成功！");
                }
            }).orElseThrow(() -> new NotFoundResourceException("地址不存在，修改地址失败！"))
        ).orElse(APIResponse.badRequest("无权访问！"));
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
        add.setIsDefault(false);
        add.setUpdateTime();
        if (isDefault != null) {
            if (isDefault) {
                addressService.findAddressByUserIdAndIsDefault(user.getId())
                        .ifPresent(oldDefault -> {
                    oldDefault.setIsDefault(false);
                    addressService.save(oldDefault);
                });
                add.setIsDefault(true);
            }
        }
        addressService.save(add);
    }

    /**
    * @Description: 删除收货地址
    * @Param: [addressId]
    * @return: 成功或失败消息
    */
    @DeleteMapping("/address/{addressId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> deleteUserAddress(@PathVariable Long addressId) {
        return userService.getCurrentUser().map(user ->
                    addressService.findById(addressId)
                        .map(address -> {
                            if (!user.getId().equals(address.getUserId())) {
                                throw new AccessDeniedException("删除地址失败！");
                            } else {
                                address.setIsDefault(false);
                                address.setIsDelete(true);
                                addressService.save(address);
                                return APIResponse.ok("删除地址成功！");
                            }
                        }).orElseThrow(() -> new NotFoundResourceException("地址不存在，删除地址失败！"))
                ).orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 获取购物篮
    * @return: 购物篮列表
    */
    @GetMapping("/cart")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<List<Cart>> getUserCart() {
        return userService.getCurrentUser()
                .map(user -> APIResponse.ok(cartService.findCartByUserId(user.getId())))
                .orElse(APIResponse.badRequest("无权访问！"));
    }

    /**
    * @Description: 添加新商品到购物篮
    * @Param: [productId, quantity, checked]
    * @return: 成功或失败消息
    */
    @PostMapping("/cart")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> addUserCart(Long productId,
                                               Integer quantity,
                                               Boolean checked) {
        return userService.getCurrentUser().map(user ->
                    productService.findById(productId).map(product -> {
                        if (!product.getStatus()) {
                            throw new BadRequestException("商品已下架！");
                        }
                        Cart cart = new Cart();
                        cart.setProductId(product.getId());
                        cart.setQuantity(quantity);
                        cart.setUserId(user.getId());
                        cart.setChecked(checked);
                        cartService.save(cart);
                        return APIResponse.ok("添加到购物车成功！");
                    }).orElseThrow(() -> new NotFoundResourceException("未找到该商品！"))
                ).orElse(APIResponse.badRequest("无权访问！"));
    }

    /***
    * @Description: 更新购物篮商品
    * @Param: [cartId, quantity, checked]
    * @return: 成功或失败消息
    */
    @PutMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> updateUserCart(@PathVariable Long cartId,
                                              Integer quantity,
                                              Boolean checked) {
        return userService.getCurrentUser().map(user ->
                    cartService.findById(cartId).map(cart -> {
                        if (cart.getUserId().equals(user.getId())) {
                            cart.setQuantity(quantity);
                            cart.setUserId(user.getId());
                            cart.setChecked(checked);
                            cartService.save(cart);
                            return APIResponse.ok("更新购物车成功！");
                        }
                        throw new BadRequestException("更新购物车失败！");
                    }).orElseThrow(() -> new NotFoundResourceException("更新购物车失败！"))
                ).orElse(APIResponse.badRequest("无权访问！"));
    }
    
    /** 
    * @Description: 删除购物篮商品
    * @Param: [cartId] 
    * @return: com.shiroyk.shopsystem.dto.response.APIResponse<java.lang.Object> 
    */ 
    @DeleteMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('NORMAL')")
    public APIResponse<Object> updateUserCart(@PathVariable Long cartId) {
        return userService.getCurrentUser().map(user ->
                cartService.findById(cartId).map(cart -> {
                    if (cart.getUserId().equals(user.getId())) {
                        cartService.delete(cartId);
                        return APIResponse.ok("删除成功！");
                    }
                    throw new BadRequestException("更新购物车失败！");
                }).orElseThrow(() -> new NotFoundResourceException("更新购物车失败！"))
        ).orElse(APIResponse.badRequest("无权访问！"));
    }
}
