package com.vishcom.laundry.api.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderCreateResponse {

    private Long id;

    //concat with service type and order type with customer primary id
    private String customerCode;

    private Set<OrderItemData> orderItems= new HashSet<>();

    private String type;

    private String branch;

    private String serviceType;

    private CustomerData customer;

    private Integer totalQuantity;

    private BigDecimal grossTotal;

    private BigDecimal netAmount;

    private BigDecimal remainAmount;

    private Integer hangerPkt;

    private Integer folderPkt;

    private String status;

    private Set<PaymentData> payments;

    private Integer returnHanger;

    private Integer issuedHanger;

    private LocalDateTime deliveryDate;

    private LocalDateTime createdDate;



    @Data
    public static class OrderItemData {

        private ItemData item;

        private Integer returnHanger;

        private Integer issuedHanger;

        private Integer quantity;

        private String quantityType;

        private String serviceType;

        private BigDecimal unitPrice;

        private String orderType;


        private String returnType;

        private String deliveryTime;


        private String deliveryType;

        private String paymentType;

        private String paymentOption;

        private BigDecimal amount;

        private PaymentData payment;

        @Data
        public static class ItemData  {


            private String id;

            private String name;

        }

        private ExtraChargeData extraCharge;
    }

    @Data
    public static class ExtraChargeData {

        private Boolean percentage;

        private BigDecimal value;

    }

    @Data
    public static class PaymentData {

        private String paymentType;

        private String paymentOption;

        private BigDecimal amount;

    }

    @Data
    public static class CustomerData {
        private Long id;

        private String address;

        private String name;

        private String mobile;

    }


}
