package se.inera.intyg.common.pdf.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import se.inera.intyg.common.pdf.UPComponent;

/**
 * Instances of this class must only be used once.
 *
 * @author eriklupander
 */
public class ComponentTreeFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentTreeFactory.class);
    private ScriptEngine engine;
    private ScriptObjectMirror jsIntygModel;

    public ComponentTreeFactory(ScriptEngine engine, ScriptObjectMirror jsIntygModel) {
        this.engine = engine;
        this.jsIntygModel = jsIntygModel;
    }

    public UPComponent decorateWithComponentTree(ScriptObjectMirror viewConfig) throws ScriptException {
        UPComponent root = new UPComponent();

        for (Map.Entry<String, Object> entry : viewConfig.entrySet()) {
            buildTree(root, entry.getValue());
        }

        return root;
    }

    private void buildTree(UPComponent parent, Object value) throws ScriptException {
        if (value instanceof ScriptObjectMirror) {

            ScriptObjectMirror obj = (ScriptObjectMirror) value;
            UPComponent upc = new UPComponent();
            upc.setType((String) obj.get("type"));
            upc.setLabelKey((String) obj.get("labelKey"));

            upc.setContentUrl((String) obj.get("contentUrl"));
            upc.setKvLabelKeys(fromStringArray(obj.get("kvLabelKeys"), obj));
            upc.setKvModelProps(fromStringArray(obj.get("kvModelProps"), obj));
            upc.setContentUrl((String) obj.get("contentUrl"));
            upc.setContentUrl((String) obj.get("contentUrl"));
            upc.setHeaders(fromStringArray(obj.get("headers"), obj));

            upc.setColProps(fromStringArray(obj.get("colProps"), obj));
            upc.setValueProps(fromStringArray(obj.get("valueProps"), obj));

            // hide / show expressions are evaluated immediately.
            if (obj.containsKey("hideExpression")) {
                Object hideExpression = obj.get("hideExpression");
                if (hideExpression instanceof ScriptObjectMirror) {
                    Object result = ((ScriptObjectMirror) hideExpression).call(null, jsIntygModel);
                    upc.setRender(isTrue(result));
                } else {
                    // handle as predicate expression...
                }
            } else if (obj.containsKey("showExpression")) {
                Object showExpression = obj.get("showExpression");
                if (showExpression instanceof ScriptObjectMirror) {
                    Object result = ((ScriptObjectMirror) showExpression).call(null, jsIntygModel);
                    upc.setRender(isTrue(result));
                } else {
                    // handle as predicate expression...
                }
            }

            // List keys can be either plain strings or functions
            Object listKey = obj.get("listKey");
            if (listKey instanceof String) {
                upc.setListKey((String) listKey);
            }
            if (listKey instanceof ScriptObjectMirror) {
                // We MAY need to pass an index here as well...
                ScriptObjectMirror function = (ScriptObjectMirror) listKey;

                if (obj.get("modelProp") instanceof ScriptObjectMirror && ((ScriptObjectMirror) obj.get("modelProp")).getClassName().equalsIgnoreCase("Array")) {
                    List<String> modelProps = fromStringArray(obj.get("modelProp"), obj);
                    for (int a = 0; a < modelProps.size(); a++) {
                        Object callResult = function.call(null, jsIntygModel, a);
                        handleListKeyFunctionResult(upc, callResult);
                    }
                } else {
                    // If the modelProp contains a dot, we first must traverse to the correct submodel.
                    ScriptObjectMirror result = (ScriptObjectMirror) engine.eval("jsIntygModel." + obj.get("modelProp"));

                    // For each result, invoke the function once.
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        Object callResult = function.call(null, entry.getValue());
                        upc.getListKeyResults().add(callResult != null ? (String) callResult : null);
                    }
                }


            }

            // modelProp may be string or array.
            Object modelProp = obj.get("modelProp");
            if (modelProp instanceof String) {
                upc.getModelProp().add((String) modelProp);
            } else if (modelProp instanceof ScriptObjectMirror) {
                ScriptObjectMirror array = (ScriptObjectMirror) modelProp;
                if (!array.getClassName().equalsIgnoreCase("Array")) {
                    throw new IllegalArgumentException("modelProp must be string or array");
                }

                upc.setModelProp(fromStringArray(array, obj));

            }


            parent.getComponents().add(upc);
            LOG.info(upc.toString());
            if (obj.containsKey("components")) {
                Object components = obj.get("components");
                ScriptObjectMirror array = (ScriptObjectMirror) components;
                for (Map.Entry<String, Object> entry : array.entrySet()) {
                    buildTree(upc, entry.getValue());
                }
            }
        }
    }

    private void handleListKeyFunctionResult(UPComponent upc, Object callResult) {
        if (callResult == null) {
            upc.setListKey(null);
        } else if (callResult instanceof String) {
            upc.setListKey((String) callResult);
        } else if (callResult instanceof Number) {
            upc.setListKey(((Number) callResult).toString());
        } else {
            throw new IllegalArgumentException("Unknown result from listKey function");
        }
    }

    private List<String> fromStringArray(Object arrayValues, ScriptObjectMirror parentObj) throws ScriptException {
        List<String> results = new ArrayList<>();
        if (arrayValues != null) {
            ScriptObjectMirror valuesRoot = (ScriptObjectMirror) arrayValues;
            for (Map.Entry<String, Object> entry : valuesRoot.entrySet()) {
                // Typically string or function
                if (entry.getValue() instanceof String) {
                    results.add((String) entry.getValue());
                }

                if (entry.getValue() instanceof ScriptObjectMirror) {
                    ScriptObjectMirror function = (ScriptObjectMirror) entry.getValue();
                    if (function.getClassName().equalsIgnoreCase("Function")) {
                        Object result = function.call(null, engine.eval("jsIntygModel." + parentObj.get("modelProp")));
                        LOG.info("Result: " + result != null ? result.toString() : "null result");
                    }
                }
            }

//            return ((ScriptObjectMirror) arrayValues).entrySet().stream().map(entry -> {
//                if (entry.getValue() instanceof String) {
//                    return (String) entry.getValue();
//                }
//                if (entry.getValue() instanceof ScriptObjectMirror) {
//                    // Probably a function we need to invoke...
//                    ScriptObjectMirror function = (ScriptObjectMirror) entry.getValue();
//                    if (function.getClassName().equalsIgnoreCase("Function")) {
//                        LOG.info("FOUND A NESTED FUNCTION!!!");
//                    }
//                }
//                return "";
//            }).collect(Collectors.toList());
            return results;
        }
        return new ArrayList<>();
    }

    private boolean isTrue(Object res) {
        if (res instanceof Undefined) {
            return false;
        }
        if (res instanceof Boolean) {
            return ((Boolean) res).booleanValue();
        }
        return false;
    }
}
