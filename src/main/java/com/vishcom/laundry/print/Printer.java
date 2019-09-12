package com.vishcom.laundry.print;

import com.vishcom.laundry.api.response.OrderCreateResponseList;

import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;


public class Printer implements Printable {

    private OrderCreateResponseList.InvoiceData invoice;

    int startX = 0;
    int startY = 0;

    //int startYInvoice = 0;
    //int startXInvoice = 0;

    //int startYInvoiceNum = 0;
    int startXInvoiceNum = 0;

    int customerYInvoice = 0;
    int customerXInvoice = 0;

    int orderItemYInvoice = 0;
    int orderItemXInvoice = 0;

    int footerYInvoice = 0;
    int footerXInvoice = 0;

    int gap=0;
    int distanceX = 0;


    public Printer(OrderCreateResponseList.InvoiceData invoice) {
        this.invoice = invoice;

    }

    public Printer() {}


    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        startX = 60;
        startY = 130;
        //startY = 142;


        //startYInvoice =515;
        //startXInvoice =80;

        //startYInvoiceNum =145;
        startXInvoiceNum =startX + 245;

        customerYInvoice = startY + 20;
        customerXInvoice = startX + 5;

        orderItemYInvoice = startY + 95;
        orderItemXInvoice = startX;

        footerYInvoice = startY + 300;
        footerXInvoice = startX;

        gap=15;
        distanceX = 170;

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        //double margin = 1 * 72;

        Paper paper = pageFormat.getPaper();
        //Remove borders from the paper
        //paper.setImageableArea(30.0, 10.0, 500, 500);
        pageFormat.setPaper(paper);

        Graphics2D g2d = (Graphics2D)graphics;

        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());


        //startYInvoice = startYInvoice + gap;
        graphics.drawString("INV :"+invoice.getId(), startXInvoiceNum, startY);

        graphics.drawString(invoice.getCustomer().getName(), customerXInvoice, customerYInvoice);
        customerYInvoice = customerYInvoice + gap;
        graphics.drawString(""+invoice.getCustomer().getMobile(), customerXInvoice, customerYInvoice);

        if(invoice.getCreatedDate() != null) {
            graphics.drawString(invoice.getCreatedDate(), customerXInvoice+distanceX, customerYInvoice);
        }

        if(invoice.getCustomer().getAddress() != null) {
            customerYInvoice = customerYInvoice + gap;
            graphics.drawString(""+invoice.getCustomer().getAddress(), customerXInvoice, customerYInvoice);
            //startY = startY + 10;
        }

        if(invoice.getCustomer().getEmail() != null) {
            customerYInvoice = customerYInvoice + gap;
            graphics.drawString(""+invoice.getCustomer().getEmail(), customerXInvoice, customerYInvoice);
            //startY = startY + 10;
        }
        customerYInvoice = customerYInvoice + gap;
        if(invoice.getOrderItems() != null &invoice.getOrderItems().size() > 0) {

            for (OrderCreateResponseList.InvoiceData.OrderItemData orderItem:invoice.getOrderItems()) {

                orderItemXInvoice = startX;

                graphics.drawString(orderItem.getDeliveryTime(), customerXInvoice + distanceX, customerYInvoice-30);
                graphics.drawString(""+orderItem.getQuantity() , orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+40;
                graphics.drawString(""+orderItem.getItem().getName(), orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+190;
                graphics.drawString(""+orderItem.getUnitPrice(), orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+30;
                graphics.drawString(""+ orderItem.getAmount(), orderItemXInvoice, orderItemYInvoice);
                orderItemYInvoice = orderItemYInvoice + gap;
            }
            orderItemXInvoice = startX;
            if(invoice.getReturnHanger() != null && invoice.getReturnHanger() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Return Hangers : ", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+240;
                graphics.drawString(invoice.getReturnHangerAmount() +"("+invoice.getReturnHanger()+")", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice = startX;
            }
            if(invoice.getIssuedHanger() != null && invoice.getIssuedHanger() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Issued Hangers : ", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+240;
                graphics.drawString(invoice.getIssuedHangerAmount() +"("+invoice.getIssuedHanger()+")", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice = startX;
            }

            if(invoice.getDiscount() != null && invoice.getDiscount().signum() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Discount : ", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+240;
                graphics.drawString("" + invoice.getDiscount(), orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice = startX;
            }

            if(invoice.getExtraCharge() != null && invoice.getExtraCharge().signum() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Extra charge : ", orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+240;
                graphics.drawString("" + invoice.getExtraCharge(), orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice = startX;
            }

            if(invoice.getPayments() != null && invoice.getPayments().size() > 0) {

                BigDecimal totalPayment = new BigDecimal(0.0);
                for(OrderCreateResponseList.InvoiceData.PaymentData paymentData : invoice.getPayments()) {
                    totalPayment = totalPayment.add(paymentData.getAmount());
                }
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Payments: " + totalPayment, orderItemXInvoice, orderItemYInvoice);
                if(invoice.getPaymentStatus() != null) {
                    graphics.drawString("Payment Status: " + invoice.getPaymentStatus(), orderItemXInvoice, orderItemYInvoice);
                }

            }
            orderItemYInvoice = orderItemYInvoice + 40;
            graphics.drawString("Total Qty : "+invoice.getTotalQuantity(), orderItemXInvoice, orderItemYInvoice);
            orderItemYInvoice = orderItemYInvoice + gap;
            graphics.drawString("Gross Total    : "+invoice.getGrossTotal(), orderItemXInvoice+distanceX, orderItemYInvoice);

            orderItemYInvoice = orderItemYInvoice + gap;
            graphics.drawString("Total          : "+invoice.getNetAmount(), orderItemXInvoice+distanceX, orderItemYInvoice);

        }

        if(invoice.getBranch() != null) {
            //footerYInvoice = startSecodY + 40;
            graphics.drawString("Branch : "+invoice.getBranch(), footerXInvoice, footerYInvoice);
        }
                /*startY = startY + 20;
                graphics.drawString("BRANCH INVO :"+invoice.getBranch(), startX, startY);*/

        if(invoice.getCustomer() != null) {
            footerYInvoice = footerYInvoice + gap;
            graphics.drawString("Your ID : "+invoice.getCustomer().getId(), footerXInvoice, footerYInvoice);
        }
        footerYInvoice = footerYInvoice + gap;
        graphics.drawString("W/Plant Mattegoda", footerXInvoice, footerYInvoice);
        footerYInvoice = footerYInvoice + gap;
        graphics.drawString("Vat Reg No : 114676101-7000", footerXInvoice, footerYInvoice);
        footerYInvoice = footerYInvoice + gap + gap;
        graphics.drawString(invoice.getType(), footerXInvoice, footerYInvoice);

        return PAGE_EXISTS;
    }

}
