package com.warrantybee.api.services.interfaces;

import java.util.Map;

public interface ITemplateService {

    String process(String templatePath, Map<String,String> macros);
}
