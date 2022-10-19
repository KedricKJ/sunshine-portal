package com.vishcom.laundry.print;

import com.vishcom.laundry.dto.request.InvoicePrintRequest;
import lombok.Data;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

@Data
public class FactoryToBranchPrint implements Printable {

    private List<InvoicePrintRequest.InvoiceData> invoices;;

    public FactoryToBranchPrint(List<InvoicePrintRequest.InvoiceData> invoices) {
        this.invoices = invoices;

    }

    public FactoryToBranchPrint() {}

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int startX = 30;
        int startY = 100;
        int gap=50;


        if(invoices != null && invoices.size() > 0) {

            for (InvoicePrintRequest.InvoiceData invoice:invoices) {

                startX = 10;
                graphics.drawString(""+invoice.getId(), startX, startY);
                startX = startX + gap;
                graphics.drawString(""+invoice.getCustomerCode(), startX, startY);
                startX = startX + gap+40;
                graphics.drawString(""+invoice.getTotalQuantity(), startX, startY);
                if(invoice.getType() != null) {
                    startX = startX + 20;
                    graphics.drawString(""+invoice.getType(), startX, startY);
                }

                if(invoice.getHangerPkt() !=null) {
                    startX = startX + gap+gap+gap+gap+40;
                    graphics.drawString(""+invoice.getHangerPkt(), startX, startY);
                }
                if(invoice.getFolderPkt() !=null) {
                    startX = startX + 20;
                    graphics.drawString(""+invoice.getFolderPkt(), startX, startY);
                }

                startY = startY + 15;
            }

        }

        return PAGE_EXISTS;
    }


}
