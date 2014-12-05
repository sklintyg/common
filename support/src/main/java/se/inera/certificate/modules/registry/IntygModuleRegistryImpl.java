package se.inera.certificate.modules.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;

/*@Component*/
public class IntygModuleRegistryImpl implements IntygModuleRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(IntygModuleRegistryImpl.class);

    /*
     * The Autowired annotation will automagically pickup all registered beans with type ModuleEntryPoint and
     * insert into the moduleEntryPoints list.
     */
    @Autowired
    private List<ModuleEntryPoint> moduleEntryPoints;

    private Map<String, ModuleEntryPoint> moduleApiMap = new HashMap<String, ModuleEntryPoint>();

    private Map<String, IntygModule> intygModuleMap = new HashMap<String, IntygModule>();

    private ApplicationOrigin origin;

    @PostConstruct
    public void initModulesList() {

        for (ModuleEntryPoint entryPoint : moduleEntryPoints) {
            moduleApiMap.put(entryPoint.getModuleId(), entryPoint);
            IntygModule module = new IntygModule(entryPoint.getModuleId(), entryPoint.getModuleName(),
                    entryPoint.getModuleDescription(),
                    entryPoint.getModuleCssPath(origin), entryPoint.getModuleScriptPath(origin),
                    entryPoint.getModuleDependencyDefinitionPath(origin));

            intygModuleMap.put(module.getId(), module);
        }

        LOG.info("Module registry loaded with {} modules", moduleApiMap.size());
    }

    @Override
    public List<IntygModule> listAllModules() {
        List<IntygModule> moduleList = new ArrayList<IntygModule>(intygModuleMap.values());
        Collections.sort(moduleList);
        return moduleList;
    }

    @Override
    public ModuleApi getModuleApi(String id) throws ModuleNotFoundException {
        ModuleEntryPoint api = moduleApiMap.get(id);
        if (api == null) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return api.getModuleApi();
    }

    @Override
    public ModuleEntryPoint getModuleEntryPoint(String id) throws ModuleNotFoundException {
        ModuleEntryPoint entryPoint = moduleApiMap.get(id);
        if (entryPoint == null) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return entryPoint;
    }

    @Override
    public IntygModule getIntygModule(String id) throws ModuleNotFoundException {
        if (!intygModuleMap.containsKey(id)) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return intygModuleMap.get(id);
    }

    @Override
    public List<ModuleEntryPoint> getModuleEntryPoints() {
        return moduleEntryPoints;
    }

    @Override
    public boolean moduleExists(String moduleId) {
        return moduleApiMap.containsKey(moduleId);
    }

    public void setOrigin(ApplicationOrigin origin) {
        this.origin = origin;
    }

    public ApplicationOrigin getOrigin() {
        return origin;
    }
}
