package model.dictionary.application;

import java.util.HashMap;

import model.dictionary.exception.DictionaryException;
import model.dictionary.exception.NotImplementedError;
import model.dictionary.model.ActionType;
import model.dictionary.model.BaseAction;
import model.dictionary.model.BaseWord;
import model.dictionary.model.CustomWord;
import model.dictionary.model.ExecutePlaceType;
import model.dictionary.model.InputAction;
import model.dictionary.model.NatureLanguageType;

public class PythonDictionary implements BaseDictionaryInterface {
    private HashMap<BaseWord, BaseAction> mDictionary;
    private static PythonDictionary mDictReference = new PythonDictionary();
    private PythonDictionary() {
        mDictionary = new HashMap<BaseWord, BaseAction>();
        initDictionary();
    }
    public static PythonDictionary createDictionary() {
        return mDictReference;
    }
    @Override
    public void initDictionary() {
        try {
            mDictionary.put(new CustomWord("import", NatureLanguageType.ENGLISH),
                            new InputAction(ActionType.INPUT, ExecutePlaceType.SHELL, "import"));
            mDictionary.put(new CustomWord("math", NatureLanguageType.ENGLISH),
                new InputAction(ActionType.INPUT, ExecutePlaceType.SHELL, "math"));
            mDictionary.put(new CustomWord("sin", NatureLanguageType.ENGLISH),
                new InputAction(ActionType.INPUT, ExecutePlaceType.SHELL, "sin"));
            mDictionary.put(new CustomWord("cos", NatureLanguageType.ENGLISH),
                new InputAction(ActionType.INPUT, ExecutePlaceType.SHELL, "cos"));
            mDictionary.put(new CustomWord("exit", NatureLanguageType.ENGLISH),
                new InputAction(ActionType.INPUT, ExecutePlaceType.SHELL, "exit"));

            /*
            add("import");
        add("math");
        add("sin");
        add("cos");
        add("exit");
             */
        } catch (DictionaryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initDictionary(String config_path) throws DictionaryException {
        throw new NotImplementedError();
    }

    public BaseAction lookUpAction(BaseWord key) {
        if (mDictionary.containsKey(key)) {
            return mDictionary.get(key);
        } else {
            return null;
        }
    }
}
