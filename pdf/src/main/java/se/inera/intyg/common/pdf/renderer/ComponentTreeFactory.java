package se.inera.intyg.common.pdf.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private ScriptObjectMirror jsIntygModel;

    public ComponentTreeFactory(ScriptObjectMirror jsIntygModel) {
        this.jsIntygModel = jsIntygModel;
    }

    public UPComponent decorateWithComponentTree(ScriptObjectMirror viewConfig) {
        UPComponent root = new UPComponent();

        viewConfig.entrySet().stream().forEach(entry -> buildTree(root, entry.getValue()));

        return root;
    }

    private void buildTree(UPComponent parent, Object value) {
        if (value instanceof ScriptObjectMirror) {

            ScriptObjectMirror obj = (ScriptObjectMirror) value;
            UPComponent upc = new UPComponent();
            upc.setType((String) obj.get("type"));
            upc.setLabelKey((String) obj.get("labelKey"));
            upc.setModelProp((String) obj.get("modelProp"));
            upc.setContentUrl((String) obj.get("contentUrl"));
            upc.setHeaders(fromStringArray(obj.get("headers")));
            upc.setValueProps(fromStringArray(obj.get("valueProps")));

            // hide / show expressions are evaluated immediately.
            if (obj.containsKey("hideExpression")) {

                ScriptObjectMirror expression = (ScriptObjectMirror) obj.get("showExpression");
                if (expression.getClassName().equalsIgnoreCase("Function")) {
                    Object result = expression.call(null, jsIntygModel);
                    upc.setRender(isTrue(result));
                } else {
                    // handle as predicate expression...
                }
            } else if (obj.containsKey("showExpression")) {
                // evaluate directly
                ScriptObjectMirror expression = (ScriptObjectMirror) obj.get("showExpression");
                if (expression.getClassName().equalsIgnoreCase("Function")) {
                    Object result = expression.call(null, jsIntygModel);
                    upc.setRender(isTrue(result));
                } else {
                    // handle as predicate expression...
                }

            }

            parent.getComponents().add(upc);
            LOG.info(upc.toString());
            if (obj.containsKey("components")) {
                Object components = obj.get("components");
                ScriptObjectMirror array = (ScriptObjectMirror) components;
                array.entrySet().stream().forEach(entry -> buildTree(upc, entry.getValue()));
            }
        }
    }

    private List<String> fromStringArray(Object headers) {
        if (headers != null) {
            return ((ScriptObjectMirror) headers).entrySet().stream().map(entry -> (String) entry.getValue()).collect(Collectors.toList());
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
