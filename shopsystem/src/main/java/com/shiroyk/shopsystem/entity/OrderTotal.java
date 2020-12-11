package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.dto.response.UserLite;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_total")
public class OrderTotal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address addressId;

    private OrderStatus status;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    private BigDecimal price;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    private String express;

    private String logistics;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLite getUserId() {
        return new UserLite(userId);
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status != null)
            this.status = status;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        if (delete != null)
            isDelete = delete;
    }

    public boolean isOrdered() {
        return status == OrderStatus.Ordered;
    }

    public boolean isCancel() {
        return status == OrderStatus.Cancel;
    }

    public boolean isPayed() {
        return status == OrderStatus.Payed;
    }

    public boolean isShipped() {
        return status == OrderStatus.Shipped;
    }

    public boolean isTransit() {
        return status == OrderStatus.Transit;
    }

    public boolean isReceived() {
        return status == OrderStatus.Received;
    }

    public boolean isCompleted() {
        return status == OrderStatus.Completed;
    }

    public boolean isAfterSale() {
        return status == OrderStatus.AfterSale;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price != null)
            this.price = price;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        if (!StringUtils.isEmpty(payMethod))
            this.payMethod = payMethod;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime() {
        this.paymentTime = LocalDateTime.now();
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        if (!StringUtils.isEmpty(express))
            this.express = express;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        if (!StringUtils.isEmpty(logistics))
            this.logistics = logistics;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = LocalDateTime.now();
    }
}
