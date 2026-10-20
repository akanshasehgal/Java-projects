package com.shopsphere.web.tag;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Custom tag handler (Tag Extension) declared in WEB-INF/shopsphere.tld.
 *
 *   <ss:price value="${p.finalPrice}" mrp="${p.price}"/>
 *
 * prints the price in Indian Rupees, and when an MRP is given, the struck-through MRP
 * and the percentage saved.
 */
public class PriceTag extends SimpleTagSupport {

    private static final Locale INDIA = Locale.forLanguageTag("en-IN");

    private double value;
    private Double mrp;
    private String size = "normal";

    public void setValue(double value) { this.value = value; }
    public void setMrp(Double mrp) { this.mrp = mrp; }
    public void setSize(String size) { this.size = size; }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.write("<span class=\"price price-" + ("large".equals(size) ? "large" : "normal") + "\">");
        out.write("<span class=\"price-now\">" + format(value) + "</span>");
        if (mrp != null && mrp > value) {
            long off = Math.round((mrp - value) * 100 / mrp);
            out.write(" <span class=\"price-mrp\">" + format(mrp) + "</span>");
            out.write(" <span class=\"price-off\">" + off + "% off</span>");
        }
        out.write("</span>");
    }

    static String format(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(INDIA);
        nf.setMinimumFractionDigits(amount == Math.floor(amount) ? 0 : 2);
        nf.setMaximumFractionDigits(2);
        return nf.format(amount);
    }
}
