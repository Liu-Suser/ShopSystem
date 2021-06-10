package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class OrderInfoDto {
    private Long id;
    private Long userId;
    private UserDto user;
    private Long addressId;
    private AddressDto address;
    private List<OrderDetailDto> orderDetails;
    private OrderStatus status;
    private BigDecimal price;
    private String payMethod;
    private String express;
    private String logistics;
    private LocalDateTime paymentTime;
    private LocalDateTime createTime;

    @Data
    public static class OrderDetailDto {
        Long id;
        Long productId;
        Long commentId;
        boolean isComment;
        Integer quantity;
        String name;
        Set<String> image;

        public OrderDetailDto(Long id, Long productId, Long commentId, Integer quantity, Product product) {
            this.id = id;
            this.productId = productId;
            this.commentId = commentId;
            this.isComment = commentId != null;
            this.quantity = quantity;
            if (product != null)
                this.name = product.getName();
            if (product != null)
                this.image = product.getImage().getNames();
        }
    }

    public OrderInfoDto(Long id, OrderStatus status, BigDecimal price,
                        LocalDateTime paymentTime, LocalDateTime createTime) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.paymentTime = paymentTime;
        this.createTime = createTime;
    }

    public OrderInfoDto(Long id, Long userId, User user,
                        Long addressId, Address address, List<OrderDetail> orderDetails,
                        OrderStatus status, BigDecimal price, String payMethod,
                        String express, String logistics, LocalDateTime paymentTime,
                        LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.user = user.toUserDtoS();
        this.addressId = addressId;
        this.address = address.toAddressDtoS();
        this.orderDetails = orderDetails.stream().map(OrderDetail::toOrderDetailDto).collect(Collectors.toList());
        this.status = status;
        this.price = price;
        this.payMethod = payMethod;
        this.express = express;
        this.logistics = logistics;
        this.paymentTime = paymentTime;
        this.createTime = createTime;
    }
}
