package it.verde.view.core;

import java.math.BigDecimal;
import java.util.List;

public interface InputView {
    String readString(String prompt);
    String readRequiredString(String prompt);
    int readInt(String prompt);
    int readPositiveInt(String prompt);
    BigDecimal readPositiveDecimal(String prompt);
    boolean readBoolean(String prompt);
    boolean readBoolean(String prompt, Boolean defaultValue);
    int readMenuChoice(String title, List<String> options);
    void waitForEnter();
}
