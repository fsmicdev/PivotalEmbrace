package org.micg.pivotalembrace.model.document;

import org.micg.pivotalembrace.model.auxiliary.DiaryNote;
import org.micg.pivotalembrace.model.auxiliary.PriorityToAttain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Model abstraction for [Goal]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
@Document(collection = "goal")
public class Goal {

    @Id
    private Long _id;

    private String goalTitle;
    private String description;
    private List<DiaryNote> diaryNotes;
    private PriorityToAttain priorityToAttain;
    private Date toAchieveByTargetDate;
    private BigDecimal percentageAchieved; // 0-100

    public Goal() {
    }

    public Goal(final String goalTitle, final String description, final List<DiaryNote> diaryNotes,
                final PriorityToAttain priorityToAttain, final Date toAchieveByTargetDate, final BigDecimal percentageAchieved) {
        this.goalTitle = goalTitle;
        this.description = description;
        this.diaryNotes = diaryNotes;
        this.priorityToAttain = priorityToAttain;
        this.toAchieveByTargetDate = toAchieveByTargetDate;
        this.percentageAchieved = percentageAchieved;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "_id=" + _id +
                ", goalTitle='" + goalTitle + '\'' +
                ", description='" + description + '\'' +
                ", diaryNotes=" + diaryNotes +
                ", priorityToAttain=" + priorityToAttain +
                ", toAchieveByTargetDate=" + toAchieveByTargetDate +
                ", percentageAchieved=" + percentageAchieved +
                '}';
    }

    public Long getId() {
        return _id;
    }

    public void setId(final Long _id) {
        this._id = _id;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(final String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<DiaryNote> getDiaryNotes() {
        return diaryNotes;
    }

    public void setDiaryNotes(final List<DiaryNote> diaryNotes) {
        this.diaryNotes = diaryNotes;
    }

    public PriorityToAttain getPriorityToAttain() {
        return priorityToAttain;
    }

    public void setPriorityToAttain(final PriorityToAttain priorityToAttain) {
        this.priorityToAttain = priorityToAttain;
    }

    public Date getToAchieveByTargetDate() {
        return toAchieveByTargetDate;
    }

    public void setToAchieveByTargetDate(final Date toAchieveByTargetDate) {
        this.toAchieveByTargetDate = toAchieveByTargetDate;
    }

    public BigDecimal getPercentageAchieved() {
        return percentageAchieved;
    }

    public void setPercentageAchieved(final BigDecimal percentageAchieved) {
        this.percentageAchieved = percentageAchieved;
    }
}
