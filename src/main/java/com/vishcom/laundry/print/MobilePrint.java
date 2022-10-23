package com.vishcom.laundry.print;

import com.vishcom.laundry.api.response.MobileOrderCreateResponseList;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigDecimal;

public class MobilePrint implements Printable {
  int startX = 5;
  int startY = 0;
  int startXInvoiceNum = 0;
  int customerYInvoice = 2;
  int customerXInvoice = 0;
  int orderItemYInvoice = 10;
  int orderItemXInvoice = 5;
  int footerYInvoice = 0;
  int footerXInvoice = 0;
  int gap = 0;
  int distanceX = 0;
  int gapX = 260;

  private MobileOrderCreateResponseList.InvoiceData invoice;

  public MobilePrint(MobileOrderCreateResponseList.InvoiceData invoice) {
    this.invoice = invoice;
  }

  public MobilePrint() {}

  @Override
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException {

    startX = 70;
    startY = 155;

    startXInvoiceNum = startX + 245;

    customerYInvoice = startY + 20;
    customerXInvoice = startX + 5;

    orderItemYInvoice = startY + 105;
    orderItemXInvoice = startX;

    footerYInvoice = startY + 300;
    footerXInvoice = startX;

    gap = 15;
    distanceX = 140;

    if (pageIndex > 0) {
      return NO_SUCH_PAGE;
    }
    Paper paper = pageFormat.getPaper();
    // Remove borders from the paper
    //paper.setImageableArea(0.0, 0.0, 500, 500);
    pageFormat.setPaper(paper);
    Graphics2D g2d = (Graphics2D) graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    graphics.drawString("Order receipt :" + invoice.getId(), startXInvoiceNum - 100, startY + 8);

    if (invoice.getCustomer().getUsername() != null) {
      graphics.drawString(
          invoice.getCustomer().getUsername(), customerXInvoice, customerYInvoice + 5);
      customerYInvoice = customerYInvoice + gap;
    }

    //graphics.drawString("" + invoice.getCustomer().getMobile(), customerXInvoice, customerYInvoice);

    /*if (invoice.getCreatedDate() != null) {
      graphics.drawString(
          invoice.getCreatedDate(), customerXInvoice + distanceX, customerYInvoice - 15);
    }*/

    if (invoice.getCreatedDate() != null) {
      graphics.drawString(
          invoice.getCreatedDate(), customerXInvoice + distanceX, customerYInvoice - 15);
    }

//    if (invoice.getCustomer().getAddress() != null) {
//      customerYInvoice = customerYInvoice + gap;
//      graphics.drawString(
//          "" + invoice.getCustomer().getAddress(), customerXInvoice, customerYInvoice);
//      // startY = startY + 10;
//    }

    if (invoice.getCustomer().getEmail() != null) {
      customerYInvoice = customerYInvoice + gap;
      graphics.drawString(
          "" + invoice.getCustomer().getEmail(), customerXInvoice, customerYInvoice);
      // startY = startY + 10;
    }

    /*if(invoice.getCustomer() != null) {
        customerYInvoice = customerYInvoice + gap;
        graphics.drawString("Customer ID : "+invoice.getCustomer().getId(), customerXInvoice, customerYInvoice);
    }*/

    customerYInvoice = customerYInvoice + gap;
    if (invoice.getOrderItems() != null & invoice.getOrderItems().size() > 0) {

      for (MobileOrderCreateResponseList.InvoiceData.OrderItemData orderItem :
          invoice.getOrderItems()) {

        orderItemXInvoice = startX ;
        //graphics.drawString(orderItem.getQuantity().toString(), orderItemXInvoice, orderItemYInvoice);
        //orderItemXInvoice = orderItemXInvoice + 50;
        graphics.drawString(
          orderItem.getQuantity().toString() +"-" + orderItem.getItem().getName(), orderItemXInvoice, orderItemYInvoice);

        orderItemXInvoice = orderItemXInvoice + 60;
        if (orderItem.getReturnType() != null) {

          if (orderItem.getReturnType().equalsIgnoreCase("FP")) {
            graphics.drawString("" + "FOLDPACK", orderItemXInvoice, orderItemYInvoice);
          } else if (orderItem.getReturnType().equalsIgnoreCase("NC")) {
            graphics.drawString("" + "NOCREASE", orderItemXInvoice, orderItemYInvoice);
          } else if (orderItem.getReturnType().equalsIgnoreCase("OH")) {
            graphics.drawString("" + "ONHANGER", orderItemXInvoice, orderItemYInvoice);
          } else if (orderItem.getReturnType().equalsIgnoreCase("WC")) {
            graphics.drawString("" + "WITHCREASE", orderItemXInvoice, orderItemYInvoice);
          } else if (orderItem.getReturnType().equalsIgnoreCase("SP")) {
            graphics.drawString("" + "SEPARATEPACK", orderItemXInvoice, orderItemYInvoice);
          }
        }
        orderItemXInvoice = orderItemXInvoice + 140;
        graphics.drawString("" + orderItem.getUnitPrice(), orderItemXInvoice, orderItemYInvoice);
        orderItemXInvoice = orderItemXInvoice + 45;
        graphics.drawString("" + orderItem.getAmount(), orderItemXInvoice, orderItemYInvoice);
        orderItemYInvoice = orderItemYInvoice + gap;
      }
      orderItemXInvoice = startX;

      System.out.println("invoice.getDiscount -->" + invoice.getDiscount());
      if (invoice.getDiscount() != null && invoice.getDiscount().signum() != 0) {
        orderItemYInvoice = orderItemYInvoice + gap;
        graphics.drawString("Discount : ", startX, orderItemYInvoice);
        orderItemXInvoice = startX + gapX;
        graphics.drawString("" + invoice.getDiscount(), orderItemXInvoice, orderItemYInvoice);
        // orderItemXInvoice = startX;
      }

      System.out.println("invoice.getExtraCharge -->" + invoice.getExtraCharge());

      if (invoice.getExtraCharge() != null && invoice.getExtraCharge().signum() != 0) {
        orderItemYInvoice = orderItemYInvoice + gap;
        graphics.drawString("Extra charge : ", startX, orderItemYInvoice);
        orderItemXInvoice = startX + gapX;
        graphics.drawString("" + invoice.getExtraCharge(), orderItemXInvoice, orderItemYInvoice);
        // orderItemXInvoice = startX;
      }

      orderItemYInvoice = orderItemYInvoice + 40;
      graphics.drawString("Total Qty", startX, orderItemYInvoice);
      orderItemXInvoice = startX + gapX;
      graphics.drawString("" + invoice.getTotalQuantity(), orderItemXInvoice-15, orderItemYInvoice);
      orderItemYInvoice = orderItemYInvoice + gap;

      graphics.drawString("Gross Total", startX, orderItemYInvoice);
      orderItemXInvoice = startX + gapX;
      graphics.drawString("" + invoice.getGrossTotal(), orderItemXInvoice-15, orderItemYInvoice);

      orderItemYInvoice = orderItemYInvoice + gap;
      graphics.drawString("Total", startX, orderItemYInvoice);

      orderItemXInvoice = startX + gapX;
      graphics.drawString("" + invoice.getNetAmount(), orderItemXInvoice-15, orderItemYInvoice);
      orderItemXInvoice = startX;

      if (invoice.getPayments() != null && invoice.getPayments().size() > 0) {

        BigDecimal totalPayment = new BigDecimal(0.0);
        for (MobileOrderCreateResponseList.InvoiceData.PaymentData paymentData :
            invoice.getPayments()) {
          totalPayment = totalPayment.add(paymentData.getAmount());
        }
        orderItemYInvoice = orderItemYInvoice + gap + gap;
        graphics.drawString("Payments: ", startX, orderItemYInvoice);
        orderItemXInvoice = startX + gapX;
        graphics.drawString("" + totalPayment, orderItemXInvoice, orderItemYInvoice);

        orderItemYInvoice = orderItemYInvoice + gap + gap;
        graphics.drawString("Balance Payment: ", startX, orderItemYInvoice);
        orderItemXInvoice = startX + gapX;
        graphics.drawString("" + invoice.getRemainAmount(), orderItemXInvoice, orderItemYInvoice);
        // graphics.drawString("" + invoice.getNetAmount().subtract(totalPayment),
        // orderItemXInvoice, orderItemYInvoice);

        if (invoice.getPaymentStatus() != null) {
          orderItemYInvoice = orderItemYInvoice + gap;
          graphics.drawString("Payment Status", startX, orderItemYInvoice);
          orderItemXInvoice = startX + gapX;
          if (invoice.getPaymentStatus().equalsIgnoreCase("PA")) {
            graphics.drawString("" + "PAID", orderItemXInvoice, orderItemYInvoice);
          } else if (invoice.getPaymentStatus().equalsIgnoreCase("AD")) {
            graphics.drawString("" + "ADVANCE", orderItemXInvoice, orderItemYInvoice);
          } else if (invoice.getPaymentStatus().equalsIgnoreCase("PE")) {
            graphics.drawString("" + "PENDING", orderItemXInvoice, orderItemYInvoice);
          }
        }
        // orderItemXInvoice = startX;

      }
    }

    /*startY = startY + 20;
    graphics.drawString("BRANCH INVO :"+invoice.getBranch(), startX, startY);*/

    footerYInvoice = footerYInvoice + gap;
    graphics.drawString(invoice.getType(), footerXInvoice, footerYInvoice);
    if (invoice.getBranch() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString("Branch : " + invoice.getBranch(), footerXInvoice, footerYInvoice);
    }

    if (invoice.getCustomer() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString(
          "Customer ID : " + invoice.getCustomer().getId(), footerXInvoice, footerYInvoice);
    }
    System.out.println("invoice.getRemainFocAmount -->" + invoice.getRemainFocAmount());
    if (invoice.getRemainFocAmount() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString(
          "FOC Amount : " + invoice.getRemainFocAmount(), footerXInvoice, footerYInvoice);
    }
    System.out.println("invoice.getFreeWash -->" + invoice.getFreeWash());
    if (invoice.getFreeWash() != null && invoice.getFreeWash() != 0) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString("Free Wash : " + invoice.getFreeWash(), footerXInvoice, footerYInvoice);
    }

    if (invoice.getColor() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString("Color : " + invoice.getColor(), footerXInvoice, footerYInvoice);
    }

    if (invoice.getDamageType() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString(
          "Damage Type : " + invoice.getDamageType(), footerXInvoice, footerYInvoice);
    }
    if (invoice.getDamageLocation() != null) {
      footerYInvoice = footerYInvoice + gap;
      graphics.drawString(
          "Damage Location : " + invoice.getDamageLocation(), footerXInvoice, footerYInvoice);
    }

    return PAGE_EXISTS;
  }
}
