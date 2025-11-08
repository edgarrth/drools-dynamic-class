package com.poc.droolspoc.service;

import com.poc.droolspoc.domain.GenericEntity;
import com.poc.droolspoc.domain.TransformedEntity;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DroolsService {

    private static final  String RULES_DIRECTORY = "C:/drools-rules/";

    public TransformedEntity applyRules(GenericEntity genericEntity) throws IOException {

        // Load the rule file based on the type of entity
        Path ruleFilePath = Path.of(RULES_DIRECTORY, genericEntity.getType() + ".drl");
        if (!Files.exists(ruleFilePath)) {
            throw new IOException("Rule file not found for type: " + genericEntity.getType());
        }

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        //String rules = "src/main/resources/rules/" + genericEntity.getType() + ".drl";
        String rules = Files.readString(ruleFilePath);

        //kieFileSystem.write(ResourceFactory.newClassPathResource(rules));
        kieFileSystem.write("src/main/resources/rules/" + genericEntity.getType() + ".drl",
                kieServices.getResources().newReaderResource(new StringReader(rules)).setResourceType(ResourceType.DRL));


        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieRepository kieRepository = kieServices.getRepository();

        ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();
        KieContainer kieContainer
                = kieServices.newKieContainer(krDefaultReleaseId);

        KieSession kieSession = kieContainer.newKieSession();

        //KieSession kieSession = kieContainer.newKieSession();

        TransformedEntity transformedEntity = new TransformedEntity();
        kieSession.insert(genericEntity);
        kieSession.setGlobal("transformedEntity",transformedEntity);
        kieSession.fireAllRules();
        kieSession.dispose();

        return transformedEntity;


    }
}
