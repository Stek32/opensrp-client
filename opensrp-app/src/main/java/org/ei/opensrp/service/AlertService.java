package org.ei.opensrp.service;


import org.apache.commons.lang3.StringUtils;
import org.ei.drishti.dto.Action;
import org.ei.opensrp.commonregistry.AllCommonsRepository;
import org.ei.opensrp.commonregistry.CommonFtsObject;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.domain.AlertStatus;
import org.ei.opensrp.repository.AlertRepository;
import org.ei.opensrp.util.Log;

import java.util.List;
import java.util.Map;

public class AlertService {
    private AlertRepository repository;
    private CommonFtsObject commonFtsObject;
    private Map<String, AllCommonsRepository> allCommonsRepositoryMap;

    public AlertService(AlertRepository repository) {
        this.repository = repository;
    }

    public AlertService(AlertRepository repository, CommonFtsObject commonFtsObject, Map<String, AllCommonsRepository> allCommonsRepositoryMap) {
        this.repository = repository;
        this.commonFtsObject = commonFtsObject;
        this.allCommonsRepositoryMap = allCommonsRepositoryMap;
    }


    public void create(Action action) {
        if (action.isActionActive() == null || action.isActionActive()) {
            Alert alert = new Alert(action.caseID(), action.get("scheduleName"), action.get("visitCode"),
                    AlertStatus.from(action.get("alertStatus")), action.get("startDate"), action.get("expiryDate"));
            repository.createAlert(alert);

            updateFtsSearch(alert, false);
        }
    }

    public void create(Alert alert) {
        if (alert != null) {
            repository.createAlert(alert);
        }
    }

    public void close(Action action) {
        repository.markAlertAsClosed(action.caseID(), action.get("visitCode"), action.get("completionDate"));
        updateFtsSearchAfterStatusChange(action.caseID(), action.get("visitCode"));
    }

    public void deleteAll(Action action) {
        repository.deleteAllAlertsForEntity(action.caseID());
    }

    public List<Alert> findByEntityId(String entityId) {
        return repository.findByEntityId(entityId);
    }

    public List<Alert> findByEntityIdAndAlertNames(String entityId, String... names) {
        return repository.findByEntityIdAndAlertNames(entityId, names);
    }

    public List<Alert> findByEntityIdAndOffline(String entityId, boolean offline) {
        return repository.findByEntityIdAndOffline(entityId, offline);
    }

    public Alert findByEntityIdAndScheduleName(String entityId, String scheduleName) {
        return repository.findByEntityIdAndScheduleName(entityId, scheduleName);
    }

    public void changeAlertStatusToInProcess(String entityId, String alertName) {
        repository.changeAlertStatusToInProcess(entityId, alertName);
        updateFtsSearchAfterStatusChange(entityId, alertName);
    }

    public void changeAlertStatusToComplete(String entityId, String alertName) {
        repository.changeAlertStatusToComplete(entityId, alertName);
        updateFtsSearchAfterStatusChange(entityId, alertName);
    }

    public void deleteOfflineAlerts(String entityId) {
        repository.deleteOfflineAlertsForEntity(entityId);
    }

    // FTS methods
    public void updateFtsSearchAfterStatusChange(String entityId, String alertName) {
        try {
            if (commonFtsObject != null && allCommonsRepositoryMap != null) {
                List<Alert> alerts = findByEntityIdAndAlertNames(entityId, alertName);
                if (alerts != null && !alerts.isEmpty()) {
                    for (Alert alert : alerts) {
                        updateFtsSearch(alert, true);

                    }
                }
            }
        }catch (Exception e){
           Log.logError(android.util.Log.getStackTraceString(e));
        }

    }

    public void updateFtsSearch(Alert alert, boolean statusChange) {
        try {
            if (commonFtsObject != null && allCommonsRepositoryMap != null) {
                String entityId = alert.caseId();
                String scheduleName = alert.scheduleName();
                String visitCode = alert.visitCode();
                AlertStatus status = alert.status();

                String bindType = commonFtsObject.getAlertBindType(scheduleName);

                if (StringUtils.isNotBlank(bindType) && status != null && StringUtils.isNotBlank(scheduleName) && StringUtils.isNotBlank(entityId)) {
                    String field = scheduleName.replace(" ", "_");
                    // update alert status
                    updateFtsSearchInACR(bindType, entityId, field, status.value());
                    if (!statusChange && StringUtils.isNotBlank(visitCode) && commonFtsObject.alertUpdateVisitCode(scheduleName)) {
                        // update alert visit code
                        updateFtsSearchInACR(bindType, entityId, CommonFtsObject.phraseColumn, visitCode);
                    }
                }

            }
        } catch (Exception e){
            Log.logError(android.util.Log.getStackTraceString(e));
        }
    }


    public boolean updateFtsSearchInACR(String bindType, String entityId, String field, String value) {
        AllCommonsRepository allCommonsRepository = getAllCommonRepository(bindType);
        if (allCommonsRepository != null) {
            return allCommonsRepository.updateSearch(entityId, field, value, commonFtsObject.getAlertFilterVisitCodes());
        }
        return false;
    }

    private AllCommonsRepository getAllCommonRepository(String bindType) {
        if (allCommonsRepositoryMap != null && !allCommonsRepositoryMap.isEmpty()) {
            return allCommonsRepositoryMap.get(bindType);
        }
        return null;
    }

}
