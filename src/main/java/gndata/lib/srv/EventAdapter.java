package gndata.lib.srv;


import java.time.LocalDateTime;
import java.util.*;

import gndata.app.ui.util.Dates;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public interface EventAdapter extends Appointment {

    public abstract LocalDateTime getEventStart();

    public abstract LocalDateTime getEventEnd();

    public abstract String getSummary();

    public abstract String getDescription();


    @Override
    public default Calendar getStartTime() {
        return Dates.toCalendar(getEventStart());
    }

    @Override
    public default Calendar getEndTime() {
        return Dates.toCalendar(getEventEnd());
    }

    @Override
    public default void setStartTime(Calendar var1) {
        // Do nothing
    }

    @Override
    public default void setEndTime(Calendar var1) {
        // Do nothing
    }

    @Override
    public default Boolean isWholeDay() {
        return false;
    }

    @Override
    public default void setWholeDay(Boolean var1) {
        // Do nothing
    }

    @Override
    public default void setSummary(String var1) {
        // Do nothing
    }

    @Override
    public default void setDescription(String var1) {
        // Do nothing
    }

    @Override
    public default String getLocation() {
        return "";
    }

    @Override
    public default void setLocation(String var1) {
        // Do nothing
    }

    @Override
    public default Agenda.AppointmentGroup getAppointmentGroup() {
        return new Agenda.AppointmentGroupImpl();
    }

    @Override
    public default void setAppointmentGroup(Agenda.AppointmentGroup var1) {
        // Do nothing
    }
}
