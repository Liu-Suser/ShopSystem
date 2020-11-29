/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "statistic")
public class Statistic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "new_order")
    private Integer newOrder = 0;

    @Column(name = "cancel_order")
    private Integer cancelOrder = 0;

    @Column(name = "complete_order")
    private Integer completeOrder = 0;

    private LocalDate timestamp = LocalDate.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    public void updateNewOrder() {
        this.newOrder++;
    }

    public Integer getCancelOrder() {
        return cancelOrder;
    }

    public void setCancelOrder(Integer cancelOrder) {
        this.cancelOrder = cancelOrder;
    }

    public void updateCancelOrder() {
        this.cancelOrder++;
    }

    public Integer getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(Integer completeOrder) {
        this.completeOrder = completeOrder;
    }

    public void updateCompleteOrder() {
        this.completeOrder++;
    }

    public String getTimestamp() {
        return timestamp.getMonth().getValue() + "/" + timestamp.getDayOfMonth();
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }
}
