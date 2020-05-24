package sample.Engine;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ButtonObject extends TextObject {

    protected Color buttonFillColor = Color.WHITE;
    protected Color buttonStrokeColor = Color.BLACK;
    protected double cornerRadius = 0;
    protected double borderSize = 0;
    protected boolean textOnly = false;

    public ButtonObject(String _text, double _width, double _height)
    {
        super(_text, _width, _height);

        setTextAlignment(TextAlignment.CENTER);
    }

    public ButtonObject(String _text, double _width, double _height, double _cornerRadius, double _borderSize)
    {
        super(_text, _width, _height);

        setCornerRadius(_cornerRadius);
        setBorderSize(_borderSize);
        setTextAlignment(TextAlignment.CENTER);
    }

    public ButtonObject(String _text, double _width, double _height, Font _font, TextAlignment _textAlignment, Color _textColor, Color _buttonFillColor, Color _buttonStrokeColor, double _cornerRadius, double _borderSize)
    {
        super(_text, _width, _height, _font, _textAlignment, _textColor);

        setButtonFillColor(_buttonFillColor);
        setButtonStrokeColor(_buttonStrokeColor);
        setCornerRadius(_cornerRadius);
        setBorderSize(_borderSize);
    }

    @Override
    protected void draw()
    {
        context.clearRect(0, 0, getWidth(), getHeight());

        if (!textOnly)
        {
            context.setFill(buttonFillColor);
            context.fillRoundRect(borderSize / 2, borderSize / 2, getWidth() - borderSize, getHeight() - borderSize, cornerRadius, cornerRadius);

            if (borderSize > 0)
            {
                context.setStroke(buttonStrokeColor);
                context.setLineWidth(borderSize);
                context.strokeRoundRect(borderSize / 2, borderSize / 2, getWidth() - borderSize, getHeight() - borderSize, cornerRadius, cornerRadius);
            }
        }

        context.setFill(textColor);
        context.fillText(text, getWidth() / 2, getFontSize() + getHeight() / 4);
    }

    public Color getButtonFillColor()
    {
        return buttonFillColor;
    }

    public void setButtonFillColor(Color _buttonFillColor)
    {
        buttonFillColor = _buttonFillColor;
        draw();
    }

    public Color getButtonStrokeColor()
    {
        return buttonStrokeColor;
    }

    public void setButtonStrokeColor(Color _buttonStrokeColor)
    {
        buttonStrokeColor = _buttonStrokeColor;
        draw();
    }

    public double getCornerRadius()
    {
        return cornerRadius;
    }

    public void setCornerRadius(double _cornerRadius)
    {
        cornerRadius = _cornerRadius;
        draw();
    }

    public double getBorderSize()
    {
        return borderSize;
    }

    public void setBorderSize(double _borderSize)
    {
        borderSize = _borderSize;
        draw();
    }

    public boolean isTextOnly() {
        return textOnly;
    }

    public void setTextOnly(boolean _textOnly)
    {
        textOnly = _textOnly;
        draw();
    }

}