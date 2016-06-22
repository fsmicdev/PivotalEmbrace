package org.micg.pivotalembrace.model.auxiliary;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 *
 * @author fsmicdev
 */
public class DiaryNote {

    private GeoJsonPoint locationGeoJsonPoint;
    private LocalDate diaryDate;
    private LocalTime diaryTime;
    private String diaryNoteText;
    private boolean keepPrivate;

    public DiaryNote() {
    }

    public DiaryNote(final LocalDate diaryDate, final LocalTime diaryTime,
                     final String diaryNoteText, final boolean keepPrivate) {
        this.diaryDate = diaryDate;
        this.diaryTime = diaryTime;
        this.diaryNoteText = diaryNoteText;
        this.keepPrivate = keepPrivate;
    }

    @Override
    public String toString() {
        return "DiaryNote{" +
                "locationGeoJsonPoint=" + locationGeoJsonPoint +
                ", diaryDate=" + diaryDate +
                ", diaryTime=" + diaryTime +
                ", diaryNoteText='" + diaryNoteText + '\'' +
                ", keepPrivate=" + keepPrivate +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiaryNote diaryNote = (DiaryNote) o;

        if (keepPrivate != diaryNote.keepPrivate) return false;
        if (locationGeoJsonPoint != null ? !locationGeoJsonPoint.equals(diaryNote.locationGeoJsonPoint) : diaryNote.locationGeoJsonPoint != null)
            return false;
        if (diaryDate != null ? !diaryDate.equals(diaryNote.diaryDate) : diaryNote.diaryDate != null) return false;
        if (diaryTime != null ? !diaryTime.equals(diaryNote.diaryTime) : diaryNote.diaryTime != null) return false;
        return diaryNoteText != null ? diaryNoteText.equals(diaryNote.diaryNoteText) : diaryNote.diaryNoteText == null;

    }

    @Override
    public int hashCode() {
        int result = locationGeoJsonPoint != null ? locationGeoJsonPoint.hashCode() : 0;
        result = 31 * result + (diaryDate != null ? diaryDate.hashCode() : 0);
        result = 31 * result + (diaryTime != null ? diaryTime.hashCode() : 0);
        result = 31 * result + (diaryNoteText != null ? diaryNoteText.hashCode() : 0);
        result = 31 * result + (keepPrivate ? 1 : 0);
        return result;
    }

    public LocalDate getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(final LocalDate diaryDate) {
        this.diaryDate = diaryDate;
    }

    public LocalTime getDiaryTime() {
        return diaryTime;
    }

    public void setDiaryTime(final LocalTime diaryTime) {
        this.diaryTime = diaryTime;
    }

    public String getDiaryNoteText() {
        return diaryNoteText;
    }

    public void setDiaryNoteText(final String diaryNoteText) {
        this.diaryNoteText = diaryNoteText;
    }

    public boolean isKeepPrivate() {
        return keepPrivate;
    }

    public void setKeepPrivate(final boolean keepPrivate) {
        this.keepPrivate = keepPrivate;
    }

    public GeoJsonPoint getLocationGeoJsonPoint() {
        return locationGeoJsonPoint;
    }

    public void setLocationGeoJsonPoint(final GeoJsonPoint locationGeoJsonPoint) {
        this.locationGeoJsonPoint = locationGeoJsonPoint;
    }
}
