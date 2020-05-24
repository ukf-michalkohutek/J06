package sample.Engine;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import sample.Engine.Core.GameObject2D;

public class TextObject extends GameObject2D {

    protected String text = "";
    protected Color textColor = Color.BLACK;

    public TextObject(String _text, double _width, double _height)
    {
        super(0, 0);

        setText(_text);
        setBaseWidth(_width);
        setBaseHeight(_height);
        setTextAlignment(TextAlignment.LEFT);
    }

    public TextObject(String _text, double _width, double _height, Font _font, TextAlignment _textAlignment, Color _textColor)
    {
        super(0, 0);

        setText(_text);
        setBaseWidth(_width);
        setBaseHeight(_height);
        setFont(_font);
        setTextAlignment(_textAlignment);
        setTextColor(_textColor);
    }

    protected void draw()
    {
        context.clearRect(0, 0, getWidth(), getHeight());

        context.setFill(textColor);
        switch (getTextAlignment())
        {
            case LEFT:
                context.fillText(text, 0, getFontSize());
                break;
            case CENTER:
                context.fillText(text, getWidth() / 2, getFontSize());
                break;
            case RIGHT:
                context.fillText(text, getWidth(), getFontSize());
                break;
        }
    }

    public void setBaseWidth(double _width) {
        setWidth(_width);
        draw();
    }

    public void setBaseHeight(double _height) {
        setHeight(_height);
        draw();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String _text)
    {
        text = _text;
        draw();
    }

    public Font getFont()
    {
        return context.getFont();
    }

    public void setFont(Font _font)
    {
        context.setFont(_font);
        draw();
    }

    public double getFontSize()
    {
        return context.getFont().getSize();
    }

    public void setFontSize(double _fontSize)
    {
        context.setFont(new Font(context.getFont().getName(), _fontSize));
        draw();
    }

    public TextAlignment getTextAlignment()
    {
        return context.getTextAlign();
    }

    public void setTextAlignment(TextAlignment _textAlignment)
    {
        context.setTextAlign(_textAlignment);
        draw();
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public void setTextColor(Color _textColor)
    {
        textColor = _textColor;
        draw();
    }

}