package sample;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;
import sample.Engine.FramerateCounter;
import sample.Engine.TextObject;

public class LocationTextField extends TextObject {

    public interface TextFieldEnterEventListener
    {
        void onEnterPress();
    }

    private TextFieldEnterEventListener event;

    public LocationTextField(double _width, double _height)
    {
        super("", _width, _height);

        setTextAlignment(TextAlignment.CENTER);
    }

    public void setOnEnter(LocationTextField.TextFieldEnterEventListener _event)
    {
        event = _event;
    }

    @Override
    public void start() {
        super.start();

        node.setFocusTraversable(true);
        node.requestFocus();

        node.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)
                {
                    if (getText().length() > 0) event.onEnterPress();
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE)
                {
                    if (getText().length() > 0) setText(getText().substring(0, getText().length() - 1));
                } else {
                    setText(getText() + keyEvent.getText());
                }
            }
        });
    }

}
