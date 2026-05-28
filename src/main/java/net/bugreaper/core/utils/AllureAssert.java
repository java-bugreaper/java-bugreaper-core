package net.bugreaper.core.utils;
import com.fasterxml.jackson.databind.JsonNode;
import net.bugreaper.core.exceptions.AllureValidatorException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("java:S5960")
public class AllureAssert {

    private final JsonNode root;
    private JsonNode currentStep;
    private static final String STEPS = "steps";

    private AllureAssert(JsonNode root) {
        this.root = root;
    }

    public static AllureAssert assertThat(JsonNode result) {
        return new AllureAssert(result);
    }

    // STEP

    public AllureAssert hasStep(String stepName) {
        JsonNode step = findStep(root.get(STEPS), stepName);

        assertNotNull(
                step,
                "Step not found: '" + stepName + "'\nAvailable steps:\n" + getStepsTree()
        );

        this.currentStep = step;
        return this;
    }

    public AllureAssert hasSubStep(String stepName) {

        assertNotNull(currentStep,
                "No current step selected. Call hasStep() first.");

        JsonNode step = findStep(currentStep.get(STEPS), stepName);

        assertNotNull(
                step,
                "Sub-step not found: '" + stepName + "'\nAvailable sub-steps:\n"
                        + getSubStepsTree(currentStep)
        );

        this.currentStep = step;
        return this;
    }

    // ATTACHMENT

    public AllureAssert hasAttachment(String attachmentName) {
        assertNotNull(currentStep, "No current step selected.");

        JsonNode attachments = currentStep.get("attachments");
        assertNotNull(attachments, "No attachments found in step");

        boolean found = false;

        for (JsonNode att : attachments) {
            String name = att.get("name").asText();
            if (attachmentName.equals(name)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Attachment not found: " + attachmentName);

        return this;
    }

    public AllureAssert hasAttachment(String name, String expectedContent) {
        assertNotNull(currentStep, "No current step selected.");

        JsonNode attachments = currentStep.get("attachments");
        assertNotNull(attachments, "No attachments found in step");

        for (JsonNode att : attachments) {

            String attName = att.get("name").asText();

            if (name.equals(attName)) {

                String source = att.get("source").asText();

                String actualContent = readAttachment(source);

                assertEquals(expectedContent, actualContent,
                        "Attachment content mismatch for: " + name);

                return this;
            }
        }

        fail("Attachment not found: " + name);
        return this;
    }

    // INTERNAL

    private JsonNode findStep(JsonNode steps, String name) {
        if (steps == null) return null;

        for (JsonNode step : steps) {
            if (name.equals(step.get("name").asText())) {
                return step;
            }
        }
        return null;
    }

    private String readAttachment(String source) {
        try {
            File dir = AllureResultLoader.resultsDir();

            File file = new File(dir, source);

            return new String(
                    java.nio.file.Files.readAllBytes(file.toPath()),
                    java.nio.charset.StandardCharsets.UTF_8
            );

        } catch (Exception e) {
            throw new AllureValidatorException("Failed to read attachment: " + source, e);
        }
    }

    private String getStepsTree() {
        StringBuilder sb = new StringBuilder();

        printSteps(root.get(STEPS), sb, "");

        return sb.toString();
    }

    private String getSubStepsTree(JsonNode parentStep) {

        StringBuilder sb = new StringBuilder();

        printSteps(parentStep.get(STEPS), sb, "");

        return sb.toString();
    }

    private void printSteps(JsonNode steps, StringBuilder sb, String indent) {

        if (steps == null) {
            return;
        }

        for (JsonNode step : steps) {

            sb.append(indent)
                    .append("- ")
                    .append(step.path("name").asText())
                    .append(System.lineSeparator());

            printSteps(
                    step.get(STEPS),
                    sb,
                    indent + "  "
            );
        }
    }

}