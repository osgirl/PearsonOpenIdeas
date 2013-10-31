package com.pearson.openideas.cq5.components.services;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Set;

@Component(immediate = true, metatype = true, label = "Pearson Open Ideas Workflow Manager Service", description = "Service used to manage workflows for Pearson Open Ideas")
@Service(WorkflowService.class)
public class WorkflowService {

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    protected SlingRepository repository;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    protected com.day.cq.workflow.WorkflowService workflowService;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    private SlingSettingsService slingSettings;

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowService.class);

    public void startWorkflow(final String runMode, final String workflowModelPath, final String payloadPath)
            throws WorkflowException {

        LOG.debug("We've reached workflow service!");

        String instanceRunMode = null;

        // Get run mode of server
        final Set<String> runModes = slingSettings.getRunModes();
        for (String rm : runModes) {
            if (rm.equalsIgnoreCase("publish") || rm.equalsIgnoreCase("author")) {
                instanceRunMode = rm;
                break;
            }
        }

        if (runMode.equals(instanceRunMode)) {

            // Start working
            Session adminSession = null;

            try {

                adminSession = repository.loginAdministrative(null);

                LOG.info("Starting workflow {} on path {}", workflowModelPath, payloadPath);
                final WorkflowSession wfs = workflowService.getWorkflowSession(adminSession);
                final WorkflowModel model = wfs.getModel(workflowModelPath);
                if (model == null) {
                    LOG.error("Workflow Model with ID '" + workflowModelPath + "' not found");
                    throw new WorkflowException("Workflow Model with ID '" + workflowModelPath + "' not found");
                }

                final WorkflowData data = wfs.newWorkflowData("JCR_PATH", payloadPath);
                wfs.startWorkflow(model, data);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            } finally {
                if (adminSession != null) {
                    adminSession.logout();
                }
            }

        }

    }

}
