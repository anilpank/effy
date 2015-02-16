

package org.effy.pdfbox.boxable;

import java.awt.Color;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Creates a pdfCell in a table. This cell needs to be added in pdf Row.
 * @author averma
 *
 */
public class PdfCell {
    float width;
    String text;
    private PDFont font = PDType1Font.HELVETICA;
    private float fontSize = 8;
    private Color fillColor;
    private Color textColor;

    /**
     * Create a pdf cell
     * @param width - width of cell
     * @param text - text to be written inside cell
     */
    public PdfCell(float width, String text) {
        this.width = width;
        this.text = text;
    }

    public Color getTextColor() {
        return textColor;
    }

    /**
     * Sets the text color
     * @param textColor - Sets the text color An example would be java.awt.Color.White
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(Color fillColor)
    {
        this.fillColor = fillColor;
    }


    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public PDFont getFont()
    {
        return font;
    }

    public void setFont(PDFont font)
    {
        this.font = font;
    }

    public float getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
    }

    public PdfParagraph getParagraph()
    {
         return new PdfParagraph( text,  font,  (int)fontSize,  (int)width);
    }

}
