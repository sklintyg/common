package se.inera.intyg.common.support.modules.registry;

import java.util.List;

import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;

public interface IntygModuleRegistry {

    ModuleApi getModuleApi(String id) throws ModuleNotFoundException;

    ModuleEntryPoint getModuleEntryPoint(String id) throws ModuleNotFoundException;

    IntygModule getIntygModule(String id) throws ModuleNotFoundException;

    List<IntygModule> listAllModules();

    List<ModuleEntryPoint> getModuleEntryPoints();

    boolean moduleExists(String moduleId);
}
