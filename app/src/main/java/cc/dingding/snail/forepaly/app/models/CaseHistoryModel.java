package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-8-20.
 */
public class CaseHistoryModel extends CaseModel {

    private String historyId = null;
    private String historyTimes = null;
    private String historyFrequency = null;

    public CaseHistoryModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url, String historyId, String historyTimes, String historyFrequency) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
        setHistoryId(historyId);
        setHistoryTimes(historyTimes);
        setHistoryFrequency(historyFrequency);
    }
    public CaseHistoryModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getHistoryTimes() {
        return historyTimes;
    }

    public void setHistoryTimes(String historyTimes) {
        this.historyTimes = historyTimes;
    }

    public String getHistoryFrequency() {
        return historyFrequency;
    }

    public void setHistoryFrequency(String historyFrequency) {
        this.historyFrequency = historyFrequency;
    }
}
