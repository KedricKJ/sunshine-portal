package com.vishcom.laundry.api.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateRequest {

    private ExtraChargeData extraCharge;

    private DeductionData deduction;

    private List<OrderItemData> orderItems;

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
        public static class ItemData {

            private String id;
        }
    }

    @Data
    public static class ExtraChargeData {

        private Boolean percentage;

        private BigDecimal value;

    }

    @Data
    public static class DeductionData {

        private Boolean percentage;

        private BigDecimal value;

    }

    @Data
    public static class PaymentData {

        private String paymentType;

        private String paymentOption;

        private BigDecimal amount;

    }



}
