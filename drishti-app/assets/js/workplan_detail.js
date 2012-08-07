function WorkplanDetail(workplanDetailBridge) {
    return {
        populateInto: function (cssIdentifierOfRootElement) {
            $(cssIdentifierOfRootElement).html(Handlebars.templates.workplan_detail(workplanDetailBridge.getWorkplanDetail()));
        }
    };
}

function WorkplanDetailBridge() {
    var workplanDetailContext = window.context;
    if (typeof workplanDetailContext === "undefined" && typeof FakeWorkplanDetailContext() !== "undefined") {
        workplanDetailContext = new FakeWorkplanDetailContext();
    }

    return {
        getWorkplanDetail: function () {
            return JSON.parse(workplanDetailContext.get());
        }
    };
}

function FakeWorkplanDetailContext() {
    return {
        get: function () {
            return "{\"village\": \"VillageNameVeryVeryVeryLongLong \", \"alerts\": [{\"beneficiaryName\": \"Gheredaha\", \"dueDate\": \"2012-12-12\", \"description\": \"some detail\"}, {\"beneficiaryName\": \"Bindu\", \"dueDate\": \"2012-12-12\", \"description\": \"some description.\"}]}";
        }
    }
}
