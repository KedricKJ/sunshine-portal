package com.vishcom.laundry.print;


import com.vishcom.laundry.dto.request.InvoicePrintRequest;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

public class BranchToFactoryPrint implements Printable {

    private List<InvoicePrintRequest.InvoiceData> invoices;;

    public BranchToFactoryPrint(List<InvoicePrintRequest.InvoiceData> invoices) {
        this.invoices = invoices;

    }

    public BranchToFactoryPrint() {}


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

        int startX = 0;
        int startY = 100;
        int gap=80;


        if(invoices != null && invoices.size() > 0) {

            for (InvoicePrintRequest.InvoiceData invoice:invoices) {
                startX = 30;
                graphics.drawString(""+invoice.getId(), startX, startY);
                startX = startX + gap;
                graphics.drawString(""+invoice.getCustomerCode(), startX, startY);
                startX = startX + gap;
                graphics.drawString(""+invoice.getTotalQuantity(), startX, startY);
                startX = startX + gap;
                if(invoice.getType() != null)
                    graphics.drawString(""+invoice.getType(), startX, startY);
                startY = startY + 20;
            }

        }

        return PAGE_EXISTS;
    }

}
