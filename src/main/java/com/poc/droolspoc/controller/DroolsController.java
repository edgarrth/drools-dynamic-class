package com.poc.droolspoc.controller;

import com.poc.droolspoc.domain.GenericEntity;
import com.poc.droolspoc.domain.TransformedEntity;
import com.poc.droolspoc.service.DroolsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

public class DroolsController {
    @RestController
    @RequestMapping("/api")
    static
    class TransformController {
        private final DroolsService droolsService;

        public TransformController(DroolsService droolsService) {
            this.droolsService = droolsService;
        }

        @PostMapping("/transform")
        public Map<String, Object> transformEntity(@RequestBody GenericEntity genericEntity) throws IOException {

            TransformedEntity transformedEntity = droolsService.applyRules(genericEntity);
            return Map.of(
                    "type", genericEntity.getType(),
                    "transformedEntity", transformedEntity
            );
        }
    }
}
