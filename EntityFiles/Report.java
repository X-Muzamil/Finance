package Finance.EntityFiles;

import java.time.LocalDate;


public class Report {

    private int       reportId;
    private String    type;
    private LocalDate generatedDate;
    private String    filePath;
    private int       userId;

    public Report() {}

    public Report(int reportId, String type, LocalDate generatedDate,
                  String filePath, int userId) {
        this.reportId      = reportId;
        this.type          = type;
        this.generatedDate = generatedDate;
        this.filePath      = filePath;
        this.userId        = userId;
    }

    public int       getReportId()                         { return reportId; }
    public void      setReportId(int reportId)             { this.reportId = reportId; }

    public String    getType()                             { return type; }
    public void      setType(String type)                  { this.type = type; }

    public LocalDate getGeneratedDate()                    { return generatedDate; }
    public void      setGeneratedDate(LocalDate date)      { this.generatedDate = date; }

    public String    getFilePath()                         { return filePath; }
    public void      setFilePath(String filePath)          { this.filePath = filePath; }

    public int       getUserId()                           { return userId; }
    public void      setUserId(int userId)                 { this.userId = userId; }

    @Override
    public String toString() {
        return "Report{type='" + type + "', date=" + generatedDate + ", file='" + filePath + "'}";
    }
}
