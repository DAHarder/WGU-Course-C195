package utilities;

import javafx.scene.control.TableView;
import model.Appointment;
import model.AppointmentView;

/**
 * Used for misc reporting methods
 */
public class ReportUtilities {
    /**
     * Counts specific types of appointment types currently in the applications appointment Table.
     * @param tableAppointments JavaFX table object
     * @return returns a string for use with reporting.
     */
    public static String appointmentReport(TableView<AppointmentView> tableAppointments) {
        int debriefing = 0;
        int meeting = 0;
        int luncheon = 0;
        int customerInterview = 0;

        for (Appointment appointment: tableAppointments.getItems()) {
            if(appointment.getType().contains("De-Briefing"))
                debriefing++;
            else if(appointment.getType().contains("Meeting"))
                meeting++;
            else if(appointment.getType().contains("Luncheon"))
                luncheon++;
            else if(appointment.getType().contains("Customer interview"))
                customerInterview++;
            else
                System.out.println("APPOINTMENT TYPE DATA ERROR");
        }

        String appointmentReport = Integer.toString(tableAppointments.getItems().size()) + " | " + debriefing + " De-Briefing, " + meeting + " Meeting, " + luncheon + " Luncheon, " + customerInterview + " Customer interview";

        return appointmentReport;
    }
}
