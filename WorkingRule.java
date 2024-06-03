package com.sc.fss.payments.nemodemo.service;

import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

@Service
public class RuleService2 {

    private static final String RULES_FILE_PATH = "src/main/resources/rules/rules.drl";
    private final KieServices kieServices;
    private KieContainer kieContainer;

    public RuleService2() {
        this.kieServices = KieServices.Factory.get();
        loadKieContainer();
    }

    public synchronized String getAllRules() throws Exception {
        return new String(Files.readAllBytes(Paths.get(RULES_FILE_PATH)), StandardCharsets.UTF_8);
    }

    public synchronized void addRule(String rule) throws Exception {
        Path filePath = Paths.get(RULES_FILE_PATH);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        Files.write(filePath, rule.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        reloadRules();
        fireAllRules("ABC");
    }

    public synchronized void updateRule(String ruleName, String newRuleContent) throws Exception {
        String content = getAllRules();
        String updatedContent = content.replaceAll("(?s)rule \"" + ruleName + "\".*?end", newRuleContent);
        if (content.equals(updatedContent)) {
            throw new Exception("Rule not found: " + ruleName);
        }
        Files.write(Paths.get(RULES_FILE_PATH), updatedContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        reloadRules();
        fireAllRules("abc");
    }

    public synchronized void deleteRule(String ruleName) throws Exception {
        String content = getAllRules();
        String updatedContent = content.replaceAll("(?s)rule \"" + ruleName + "\".*?end", "");
        if (content.equals(updatedContent)) {
            throw new Exception("Rule not found: " + ruleName);
        }
        Files.write(Paths.get(RULES_FILE_PATH), updatedContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        reloadRules();
    }
    private void loadKieContainer() {
        this.kieContainer = kieServices.getKieClasspathContainer();
    }

    private void reloadRules() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource resource = kieServices.getResources().newFileSystemResource(new File(RULES_FILE_PATH));
        kieFileSystem.write(resource);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieRepository kieRepository = kieServices.getRepository();
        ReleaseId releaseId = kieBuilder.getKieModule().getReleaseId();
        this.kieContainer = kieServices.newKieContainer(releaseId);
    }

    public void fireAllRules(Object fact) {
        KieSession kieSession = kieContainer.newKieSession();
        FactHandle factHandle = kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.delete(factHandle);
        kieSession.dispose();
    }
}



package com.sc.fss.payments.nemodemo.controller;

import com.sc.fss.payments.nemodemo.service.RuleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rules")
public class RuleController2 {

    @Autowired
    private RuleService2 ruleService;

    @GetMapping
    public String getAllRules() throws Exception {
        return ruleService.getAllRules();
    }

    @PostMapping
    public void createRule(@RequestBody String rule) throws Exception {
        ruleService.addRule(rule);
    }

    @PutMapping("/{ruleName}")
    public void updateRule(@PathVariable String ruleName, @RequestBody String newRuleContent) throws Exception {
        ruleService.updateRule(ruleName, newRuleContent);
    }

    @DeleteMapping("/{ruleName}")
    public void deleteRule(@PathVariable String ruleName) throws Exception {
        ruleService.deleteRule(ruleName);
    }
}


<!-- Drools dependencies -->
			<dependency>
				<groupId>org.kie</groupId>
				<artifactId>kie-api</artifactId>
				<version>7.72.0.Final</version>
			</dependency>
			<dependency>
				<groupId>org.drools</groupId>
				<artifactId>drools-core</artifactId>
				<version>7.72.0.Final</version>
			</dependency>
			<dependency>
				<groupId>org.drools</groupId>
				<artifactId>drools-compiler</artifactId>
				<version>7.72.0.Final</version>
			</dependency>
			<dependency>
				<groupId>org.kie</groupId>
				<artifactId>kie-spring</artifactId>
				<version>7.72.0.Final</version>
			</dependency>
