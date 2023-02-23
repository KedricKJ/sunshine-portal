package com.vishcom.laundry.print;

import com.vishcom.laundry.api.response.OrderCreateResponseList;

import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;


public class Printer implements Printable {

    private OrderCreateResponseList.InvoiceData invoice;

    int startX = 5;
    int startY = 0;

    //int startYInvoice = 0;
    //int startXInvoice = 0;

    //int startYInvoiceNum = 0;
    int startXInvoiceNum = 0;

    int customerYInvoice = 2;
    int customerXInvoice = 0;

    int orderItemYInvoice = 10;
    int orderItemXInvoice = 5;

    int footerYInvoice = 0;
    int footerXInvoice = 0;

    int gap=0;
    int distanceX = 0;

    int gapX = 275;


    public Printer(OrderCreateResponseList.InvoiceData invoice) {
        this.invoice = invoice;

    }

    public Printer() {}


    @Override
    public int print(Graphics graphics, PageFormat format, int pageIndex) throws PrinterException {

        startX = 5;
        startY = 97;
        startXInvoiceNum =startX + 245;

        customerYInvoice = startY + 5;
        customerXInvoice = startX + 5;

        orderItemYInvoice = startY + 105;
        orderItemXInvoice = startX-10;

        footerYInvoice = startY + 300;
        footerXInvoice = startX;

        gap=15;
        distanceX = 170;

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Paper paper = format.getPaper();
        format.setPaper(paper);
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(format.getImageableX(), format.getImageableY());
        if(invoice.getCustomer().getName() != null) {
            graphics.drawString(invoice.getCustomer().getName(), customerXInvoice, customerYInvoice);
            customerYInvoice = customerYInvoice + gap;
        }
        graphics.drawString(""+invoice.getCustomer().getMobile(), customerXInvoice, customerYInvoice);
        if(invoice.getCreatedDate() != null) {
            graphics.drawString(invoice.getCreatedDate(), customerXInvoice+distanceX, customerYInvoice-15);
        }
        if(invoice.getCustomer().getEmail() != null) {
            customerYInvoice = customerYInvoice + gap;
            graphics.drawString(""+invoice.getCustomer().getEmail(), customerXInvoice, customerYInvoice);
            //startY = startY + 10;
        }
        graphics.drawString("Customer ID : "+invoice.getCustomer().getId(), customerXInvoice, customerYInvoice+35);
        graphics.drawString("Order receipt :"+invoice.getId(), customerXInvoice+160, customerYInvoice+35);

        customerYInvoice = customerYInvoice + gap;

        if(invoice.getOrderItems() != null &invoice.getOrderItems().size() > 0) {

            for (OrderCreateResponseList.InvoiceData.OrderItemData orderItem:invoice.getOrderItems()) {

                orderItemXInvoice = startX;

                graphics.drawString(orderItem.getDeliveryTime(), customerXInvoice + distanceX, customerYInvoice+3);

                graphics.drawString(""+orderItem.getQuantity() , orderItemXInvoice, orderItemYInvoice+5);
                orderItemXInvoice= orderItemXInvoice+45;
                graphics.drawString(""+orderItem.getItem().getName(), orderItemXInvoice-20, orderItemYInvoice+5);
                orderItemXInvoice= orderItemXInvoice+25;
                orderItemYInvoice = orderItemYInvoice + 5;

                System.out.println("================ orderItem.getColor ================="+orderItem.getColor());
                if(orderItem.getColor() != null) {
                    graphics.drawString(""+orderItem.getColor(), orderItemXInvoice+65, orderItemYInvoice);
                }
                System.out.println("================ orderItem.getColor ================="+orderItem.getDamageType());
                if(orderItem.getDamageType() != null) {
                    graphics.drawString(""+orderItem.getDamageType(), orderItemXInvoice+120, orderItemYInvoice);
                }
                if(orderItem.getReturnType() != null) {
                    graphics.drawString("" + orderItem.getReturnType(), orderItemXInvoice+150, orderItemYInvoice);
                }
                orderItemXInvoice= orderItemXInvoice+163;
                graphics.drawString(""+orderItem.getUnitPrice(), orderItemXInvoice, orderItemYInvoice);
                orderItemXInvoice= orderItemXInvoice+45;
                graphics.drawString(""+ orderItem.getAmount(), orderItemXInvoice, orderItemYInvoice);
                orderItemYInvoice = orderItemYInvoice + gap;
            }
            orderItemXInvoice = startX;
            System.out.println("invoice.getReturnHanger -->"+invoice.getReturnHanger());
            if(invoice.getReturnHanger() != null && invoice.getReturnHanger() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Return Hangers : ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString(invoice.getReturnHangerAmount() +"("+invoice.getReturnHanger()+")", orderItemXInvoice, orderItemYInvoice);
                //orderItemXInvoice = startX;
            }
            System.out.println("invoice.getIssuedHanger -->"+invoice.getIssuedHanger());
            if(invoice.getIssuedHanger() != null && invoice.getIssuedHanger() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Issued Hangers : ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString(invoice.getIssuedHangerAmount() +"("+invoice.getIssuedHanger()+")", orderItemXInvoice, orderItemYInvoice);
                //orderItemXInvoice = startX;
            }

            System.out.println("invoice.getDiscount -->"+invoice.getDiscount());
            if(invoice.getDiscount() != null && invoice.getDiscount().signum() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Discount : ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString("" + invoice.getDiscount(), orderItemXInvoice, orderItemYInvoice);
                //orderItemXInvoice = startX;
            }

            System.out.println("invoice.getExtraCharge -->"+invoice.getExtraCharge());

            if(invoice.getExtraCharge() != null && invoice.getExtraCharge().signum() != 0) {
                orderItemYInvoice = orderItemYInvoice + gap;
                graphics.drawString("Extra charge : ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString("" + invoice.getExtraCharge(), orderItemXInvoice, orderItemYInvoice);
                //orderItemXInvoice = startX;
            }


            orderItemYInvoice = orderItemYInvoice + 40;
            graphics.drawString("Total Qty", startX, orderItemYInvoice);
            orderItemXInvoice= startX+gapX;
            graphics.drawString(""+invoice.getTotalQuantity(), orderItemXInvoice, orderItemYInvoice);
            orderItemYInvoice = orderItemYInvoice + gap;

            graphics.drawString("Gross Total", startX, orderItemYInvoice);
            orderItemXInvoice= startX+gapX;
            graphics.drawString(""+invoice.getGrossTotal(), orderItemXInvoice, orderItemYInvoice);

            orderItemYInvoice = orderItemYInvoice + gap;
            graphics.drawString("Total", startX, orderItemYInvoice);

            orderItemXInvoice= startX+gapX;
            graphics.drawString(""+invoice.getNetAmount(), orderItemXInvoice, orderItemYInvoice);
            orderItemXInvoice = startX;

            if(invoice.getPayments() != null && invoice.getPayments().size() > 0) {

                BigDecimal totalPayment = new BigDecimal(0.0);
                for(OrderCreateResponseList.InvoiceData.PaymentData paymentData : invoice.getPayments()) {
                    totalPayment = totalPayment.add(paymentData.getAmount());
                }
                orderItemYInvoice = orderItemYInvoice + gap + gap;
                graphics.drawString("Payments: ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString("" + totalPayment, orderItemXInvoice, orderItemYInvoice);

                orderItemYInvoice = orderItemYInvoice + gap + gap;
                graphics.drawString("Balance Payment: ", startX, orderItemYInvoice);
                orderItemXInvoice= startX+gapX;
                graphics.drawString("" + invoice.getRemainAmount(), orderItemXInvoice, orderItemYInvoice);

                if(invoice.getPaymentStatus() != null) {
                    orderItemYInvoice = orderItemYInvoice + gap;
                    graphics.drawString("Payment Status", startX, orderItemYInvoice);
                    orderItemXInvoice= startX+gapX;
                    if(invoice.getPaymentStatus().equalsIgnoreCase("PA")) {
                        graphics.drawString("" + "PAID", orderItemXInvoice, orderItemYInvoice);
                    } else if(invoice.getPaymentStatus().equalsIgnoreCase("AD")) {
                        graphics.drawString("" + "ADVANCE", orderItemXInvoice, orderItemYInvoice);
                    } else if(invoice.getPaymentStatus().equalsIgnoreCase("PE")) {
                        graphics.drawString("" + "PENDING", orderItemXInvoice, orderItemYInvoice);
                    }

                }
            }

        }

        footerYInvoice = footerYInvoice + gap;
        graphics.drawString(invoice.getType(), footerXInvoice, footerYInvoice);

        if(invoice.getCustomer() != null) {
            footerYInvoice = footerYInvoice + gap;
            //graphics.drawString("Customer ID : "+invoice.getCustomer().getId(), footerXInvoice, footerYInvoice);
            if(invoice.getCustomer().getRedeemTotalAmount() != null && invoice.getCustomer().getRedeemTotalAmount().compareTo(BigDecimal.ZERO) != 0) {
                footerYInvoice = footerYInvoice + gap;
                graphics.drawString("Accumulated points Rs  :"+invoice.getCustomer().getRedeemTotalAmount(), footerXInvoice, footerYInvoice);
            }
            if(invoice.getCustomer().getFreeWash() != null && invoice.getCustomer().getFreeWash() != 0) {
                footerYInvoice = footerYInvoice + gap;
                graphics.drawString("Number Of Free wash    :"+invoice.getCustomer().getFreeWash(), footerXInvoice, footerYInvoice);
            }
            if(invoice.getCustomer().getRemainFocAmount() != null && invoice.getCustomer().getRemainFocAmount().compareTo(BigDecimal.ZERO) != 0) {
                footerYInvoice = footerYInvoice + gap;
                graphics.drawString("FOC Amount                  :"+invoice.getCustomer().getRemainFocAmount(), footerXInvoice, footerYInvoice);
            }
        }

        if(invoice.getBranchId() != null) {
            footerYInvoice = footerYInvoice + gap;
            graphics.drawString("Branch : "+invoice.getBranch().toUpperCase(), footerXInvoice, footerYInvoice+100);
            graphics.drawString("Branch Contact : "+invoice.getBranchId().getContactNo(), footerXInvoice, footerYInvoice+115);
        }

        /*if(invoice.getColor() != null) {
            footerYInvoice = footerYInvoice + gap;
            graphics.drawString("Color : "+invoice.getColor(), footerXInvoice, footerYInvoice);
        }

        if(invoice.getDamageType() != null) {
            footerYInvoice = footerYInvoice + gap;
            graphics.drawString("Damage Type : "+invoice.getDamageType(), footerXInvoice, footerYInvoice);
        }
        if(invoice.getDamageLocation() != null) {
            footerYInvoice = footerYInvoice + gap;
            graphics.drawString("Damage Location : "+invoice.getDamageLocation(), footerXInvoice, footerYInvoice);
        }*/

        return PAGE_EXISTS;
    }

}
