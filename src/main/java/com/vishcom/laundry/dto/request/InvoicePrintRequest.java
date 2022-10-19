package com.vishcom.laundry.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class InvoicePrintRequest {

    List<InvoiceData> invoices;

    @Data
    public static class InvoiceData {

        private Long id;

        private String customerCode;

        private Integer totalQuantity;

        private String type;

        private Integer hangerPkt;

        private Integer folderPkt;

        private String status;

    }

}
