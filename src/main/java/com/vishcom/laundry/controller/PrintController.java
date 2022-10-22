package com.vishcom.laundry.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vishcom.laundry.api.response.MobileOrderCreateResponseList;
import com.vishcom.laundry.api.response.OrderCreateResponseList;
import com.vishcom.laundry.dto.request.InvoicePrintFactoryRequest;
import com.vishcom.laundry.dto.request.InvoicePrintRequest;
import com.vishcom.laundry.dto.response.StatusResponse;
import com.vishcom.laundry.print.BranchToFactoryPrint;
import com.vishcom.laundry.print.FactoryToBranchPrint;
import com.vishcom.laundry.print.MobilePrint;
import com.vishcom.laundry.print.Printer;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.springframework.web.bind.annotation.*;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class PrintController {

    Font f=new Font(Font.FontFamily.COURIER,9.0f,Font.NORMAL, BaseColor.BLACK);

    @CrossOrigin(origins = "*")
    //@PostMapping("${app.endpoint.ordersPrint}")
    public StatusResponse saveOrderAndPrint(@RequestBody OrderCreateResponseList request ) {

        log.info("request :{}",request);
        log.info("request :{}",request.getInvoices().size());

        StatusResponse statusResponse = new StatusResponse();

        if(request != null && request.getInvoices() != null && request.getInvoices().size() > 0) {
            for (OrderCreateResponseList.InvoiceData invoice : request.getInvoices()) {
                //pdfBillPrint(invoice);
                log.info("invoice  :{}",invoice.getId());
                log.info("invoice foc amount :{}",invoice.getRemainFocAmount());
                try {
                    billPrint(invoice);
                } catch (PrinterException e) {
                    e.printStackTrace();
                    statusResponse.setStatus("Failed");
                }
            }
            statusResponse.setStatus("Success");
        } else {
            statusResponse.setStatus("Print Failed");
        }


        return  statusResponse;
    }

    @CrossOrigin(origins = "*")
    //@PostMapping("${app.endpoint.mobileOrdersPrint}")
    @PostMapping("${app.endpoint.ordersPrint}")
    public StatusResponse printMobileOrder(@RequestBody MobileOrderCreateResponseList request ) {

        log.info("request :{}",request);
        log.info("request :{}",request.getInvoices().size());

        StatusResponse statusResponse = new StatusResponse();

        if(request != null && request.getInvoices() != null && request.getInvoices().size() > 0) {

            for (MobileOrderCreateResponseList.InvoiceData invoice : request.getInvoices()) {

                log.info("invoice  :{}",invoice.getId());
                log.info("invoice foc amount :{}",invoice.getRemainFocAmount());
                try {
                    mobileBillPrint(invoice);

                } catch (PrinterException e) {
                    e.printStackTrace();
                    statusResponse.setStatus("Failed");
                }
            }
            statusResponse.setStatus("Success");
        } else {
            statusResponse.setStatus("Print Failed");
        }


        return  statusResponse;
    }

    @PostMapping("${app.endpoint.invoicesBranchPrint}")
    public StatusResponse branchPrint(
            @RequestBody InvoicePrintRequest request) {

        StatusResponse statusResponse = new StatusResponse();
        log.info("invoices date =>{}",request.getInvoices());
        log.info("invoice size =>{}",request.getInvoices().size());

        //pdfBranchPrint(request.getInvoices());

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new BranchToFactoryPrint(request.getInvoices()));
            job.print();

        } catch (Exception e) {
            e.printStackTrace();
            statusResponse.setStatus("Failed");
        }

        statusResponse.setStatus("Success");

        return statusResponse;
    }

    @PostMapping("${app.endpoint.invoicesFactoryPrint}")
    public StatusResponse factoryPrint(
            @RequestBody InvoicePrintFactoryRequest request) {
        StatusResponse statusResponse = new StatusResponse();
        log.info("invoices date =>{}",request.getInvoices());
        log.info("invoice size =>{}",request.getInvoices().size());

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new FactoryToBranchPrint(request.getInvoices()));
            job.print();

        } catch (Exception e) {
            e.printStackTrace();
            statusResponse.setStatus("Failed");
        }

        statusResponse.setStatus("Success");
        return statusResponse;
    }

    private void billPrint(OrderCreateResponseList.InvoiceData invoice) throws PrinterException {

        //PrintService myPrintService = findPrintService("EPSON LQ-300+ /II ESC/P 2");

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = job.getPageFormat(null);
        Paper paper = format.getPaper();
        //Remove borders from the paper
        paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
        format.setPaper(paper);



        //job.setPrintService(myPrintService);
        job.setPrintable(new Printer(invoice),format);
        //boolean doPrint = job.printDialog();
        //if (doPrint) {
        try {
            log.info("invoices :"+"printing ...");
            job.print();
            log.info("invoices :"+"printed ...");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
        //}

    }

    private void mobileBillPrint(MobileOrderCreateResponseList.InvoiceData invoice) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = job.getPageFormat(null);
        Paper paper = format.getPaper();
        //Remove borders from the paper
        paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
        format.setPaper(paper);
        job.setPrintable(new MobilePrint(invoice),format);
        try {
            log.info("invoices :"+"printing ...");
            job.print();
            log.info("invoices :"+"printed ...");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    private void pdfBranchPrint(List<InvoicePrintRequest.InvoiceData> invoices) {

        String path ="/home/nuwan/Desktop/QR/Branch"+ LocalDateTime.now() +".pdf";
        Document document = new Document(PageSize.A5,1,30,1,1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfPTable pdfBranchTableInvoice = new PdfPTable(4);
            if(invoices != null && invoices.size() > 0) {
                for (InvoicePrintRequest.InvoiceData invoice:invoices) {
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getId().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getCustomerCode().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getTotalQuantity().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getType().toString()));
                }
            }
            document.add(pdfBranchTableInvoice);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private void pdfFactoryPrint(List<InvoicePrintRequest.InvoiceData> invoices) {

        String path ="/home/nuwan/Desktop/QR/Branch"+ LocalDateTime.now() +".pdf";
        Document document = new Document(PageSize.A5,1,30,1,1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfPTable pdfBranchTableInvoice = new PdfPTable(6);
            if(invoices != null && invoices.size() > 0) {
                for (InvoicePrintRequest.InvoiceData invoice:invoices) {
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getId().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getCustomerCode().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getTotalQuantity().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getType().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getFolderPkt().toString()));
                    pdfBranchTableInvoice.addCell(createTextCell(invoice.getHangerPkt().toString()));
                }
            }
            document.add(pdfBranchTableInvoice);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private void pdfBillPrint(OrderCreateResponseList.InvoiceData invoice) {
        String path ="C:\\Users\\EF\\Desktop\\nuwan\\sunshine-portal\\pdf\\"+invoice.getId()+".pdf";
        Document document = new Document(PageSize.A5,1,30,1,1);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            PdfPTable pdfPTableInvoiceNum = new PdfPTable(1);
            pdfPTableInvoiceNum.setSpacingBefore(15);
            pdfPTableInvoiceNum.addCell(createSingleCell( "INV :"+invoice.getId().toString()));

            PdfPTable pdfPTableCustomerInfo = new PdfPTable(2);
            pdfPTableCustomerInfo.setWidthPercentage(75);
            pdfPTableCustomerInfo.setWidths(new int[]{1, 1});

            OrderCreateResponseList.InvoiceData.CustomerData customerData = invoice.getCustomer();
            pdfPTableCustomerInfo.addCell(createTextCell(customerData.getName()));
            pdfPTableCustomerInfo.addCell(createTextCell(invoice.getCreatedDate()));
            pdfPTableCustomerInfo.addCell(createTextCell(customerData.getMobile()));
            pdfPTableCustomerInfo.addCell(createTextCell(invoice.getDeliveryDate()));

            if(customerData.getAddress() != null) {
                pdfPTableCustomerInfo.addCell(createTextCell(customerData.getAddress()));
                pdfPTableCustomerInfo.addCell(createTextCell(""));
            }



            PdfPTable orderItemInfo = new PdfPTable(4);
            orderItemInfo.setWidths(new int[]{1, 5,1,1});
            orderItemInfo.setSpacingBefore(15);

            if(invoice.getOrderItems() != null && invoice.getOrderItems().size() > 0) {
                for (OrderCreateResponseList.InvoiceData.OrderItemData orderItemData : invoice.getOrderItems()) {
                    orderItemInfo.addCell(createTextCell(orderItemData.getQuantity().toString()));
                    orderItemInfo.addCell(createTextCell(orderItemData.getItem().getName()));
                    orderItemInfo.addCell(createTextCell(orderItemData.getUnitPrice().toString()));
                    orderItemInfo.addCell(createSingleCell(orderItemData.getAmount().toString()));
                }
            }


            PdfPTable extraInfo = new PdfPTable(1);
            //extraInfo.setWidths(new int[]{2, 1});

            extraInfo.setSpacingBefore(5);

            log.info("invoice.getReturnHanger --> {}",invoice.getReturnHanger());

            if(invoice.getReturnHanger() != null && invoice.getReturnHanger() != 0) {
                extraInfo.addCell(createTextCell("Return Hangers :"+invoice.getReturnHanger() * 3 +"("+invoice.getReturnHanger()+")"));
                //extraInfo.addCell(createTextCell();
            }

            log.info("invoice.getIssuedHanger --> {}",invoice.getIssuedHanger());
            if(invoice.getIssuedHanger() != null && invoice.getIssuedHanger() != 0) {
                extraInfo.addCell(createTextCell("Issued Hangers :"+invoice.getIssuedHanger() * 9 +"("+invoice.getIssuedHanger()+")"));

            }

            if(invoice.getDiscount() != null) {
                extraInfo.addCell(createTextCell("Discount :"+invoice.getDiscount().toString()));


            }

            if(invoice.getExtraCharge() != null) {
                extraInfo.addCell(createTextCell("Extra Charges :"+invoice.getExtraCharge().toString()));
            }

            PdfPTable totalQty = new PdfPTable(1);
            totalQty.setSpacingBefore(5);
            totalQty.addCell(createTextCell("Total Qty : "+invoice.getTotalQuantity()));

            PdfPTable total = new PdfPTable(1);
            //total.setSpacingBefore(10);
            total.addCell(createSingleCell("Gross Total  : "+invoice.getGrossTotal()));
            total.addCell(createSingleCell("Total        : "+invoice.getNetAmount()));

            PdfPTable branchInfo = new PdfPTable(1);
            branchInfo.setSpacingBefore(5);
            branchInfo.addCell(createTextCell("Branch  : "+invoice.getBranch()));
            branchInfo.addCell(createTextCell("Your Id : "+invoice.getCustomer().getId()));
            branchInfo.addCell(createTextCell("W/Plant Matthegoda"));
            branchInfo.addCell(createTextCell("Vat Reg No : 114676101-70000"));

            PdfPTable orderType = new PdfPTable(1);
            //orderType.setSpacingBefore(5);
            orderType.addCell(createSingleCell(invoice.getType()));

            document.add(pdfPTableInvoiceNum);
            document.add(pdfPTableCustomerInfo);
            document.add(orderItemInfo);
            document.add(extraInfo);
            document.add(totalQty);
            document.add(total);
            document.add(branchInfo);
            document.add(orderType);


            document.close();


            try {
                print(path);
            } catch (PrinterException e) {
                e.printStackTrace();
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


    }




    public PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text,f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        //cell.setCalculatedHeight(2);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public PdfPCell createSingleCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        f.setStyle(Font.BOLD);
        Paragraph p = new Paragraph(text,f);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        //cell.setCalculatedHeight(2);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }


    public static void print(String path) throws PrinterException, IOException {

        PDDocument document = PDDocument.load(new File(path));
        printWithPaper(document);
        /*System.out.println("path :"+path);


        PrintService myPrintService = findPrintService("LQ-300-");

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = job.getPageFormat(null);
        Paper paper = format.getPaper();
        //Remove borders from the paper
        paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
        format.setPaper(paper);



        job.setPageable(new PDFPageable(document),format);
        job.setPrintService(myPrintService);
        job.print();

        PrinterJob pj = PrinterJob.getPrinterJob();



        job.setPrintable(new Printer(invoice),format);
        //boolean doPrint = job.printDialog();
        //if (doPrint) {
        try {
            log.info("invoices :"+"printing ...");
            job.print();
            log.info("invoices :"+"printed ...");
        } catch (PrinterException e) {
            e.printStackTrace();
        }*/

    }

    private static void printWithPaper(PDDocument document)
            throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        // define custom paper
        Paper paper = new Paper();
        paper.setSize(306, 396); // 1/72 inch
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);

        // override the page format
        Book book = new Book();
        // append all pages
        book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
        job.setPageable(book);

        job.print();
    }



    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {

            System.out.println(printService.getName());

            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}
