package org.ei.opensrp.steppingstones.ss_children;

import org.ei.opensrp.Context;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.service.formSubmissionHandler.FormSubmissionHandler;

import java.util.List;

public class CensusEnrollmentHandler implements FormSubmissionHandler {


    public CensusEnrollmentHandler() {

    }

    @Override
    public void handle(FormSubmission submission) {
        String entityID = submission.entityId();
        List<Alert> alertlist_for_client = Context.getInstance().alertService().findByEntityIdAndAlertNames(entityID, "FW CENSUS");
        if(alertlist_for_client.size() == 0){

        }else{
            for(int i = 0;i<alertlist_for_client.size();i++){
                Context.getInstance().alertService().changeAlertStatusToComplete(entityID, "FW CENSUS");
            }
        }
    }
}
